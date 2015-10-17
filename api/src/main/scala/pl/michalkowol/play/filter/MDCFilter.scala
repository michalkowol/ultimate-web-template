package pl.michalkowol.play.filter

import java.util.UUID

import play.api.http.HttpErrorHandler
import play.api.mvc._
import scala.concurrent.Future
import org.slf4j.MDC
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.util.control.NonFatal

class MDCFilter(errorHandler: HttpErrorHandler) extends Filter {

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
