package pl.michalkowol.play.filter

import java.util.UUID

import akka.stream.Materializer
import scala.concurrent.{ExecutionContext, Future}

import play.api.http.HttpErrorHandler
import play.api.mvc._
import scala.concurrent.Future
import org.slf4j.MDC

import scala.util.control.NonFatal

class MDCFilter(errorHandler: HttpErrorHandler)(implicit val mat: Materializer, ec: ExecutionContext) extends Filter {

  private val requestIdKey = "X-Request-Id"

  def apply(nextFilter: RequestHeader => Future[Result])(requestHeader: RequestHeader): Future[Result] = {
    val requestId = requestHeader.headers.get(requestIdKey).getOrElse(UUID.randomUUID().toString)
    MDC.put(requestIdKey, requestId)

    val result = try {
      nextFilter(requestHeader).recoverWith {
        case NonFatal(e) => errorHandler.onServerError(requestHeader, e)
      }
    } catch {
      case NonFatal(e) => errorHandler.onServerError(requestHeader, e)
    }
    result.map { result =>
      MDC.remove(requestIdKey)
      result.withHeaders("X-Request-Id" -> requestId)
    }
  }
}
