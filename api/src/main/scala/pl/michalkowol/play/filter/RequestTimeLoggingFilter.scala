package pl.michalkowol.play.filter

import akka.stream.Materializer
import scala.concurrent.{ExecutionContext, Future}

import play.api.Logger
import play.api.http.HttpErrorHandler
import play.api.mvc._

import scala.concurrent.Future
import scala.util.control.NonFatal

class RequestTimeLoggingFilter(errorHandler: HttpErrorHandler)(implicit val mat: Materializer, ec: ExecutionContext) extends Filter {

  private val log = Logger("RequestTime")

  def apply(nextFilter: RequestHeader => Future[Result])(requestHeader: RequestHeader): Future[Result] = {
    val startTime = System.currentTimeMillis

    val result = try {
      nextFilter(requestHeader).recoverWith {
        case NonFatal(e) => errorHandler.onServerError(requestHeader, e)
      }
    } catch {
      case NonFatal(e) => errorHandler.onServerError(requestHeader, e)
    }
    result.map { result =>
      val endTime = System.currentTimeMillis
      val requestTime = endTime - startTime
      log.info(s"${requestHeader.method} ${requestHeader.uri} took ${requestTime}ms and returned ${result.header.status}")
      result.withHeaders("X-Request-Time" -> requestTime.toString)
    }
  }
}
