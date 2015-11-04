package pl.michalkowol.play

import play.api.Logger
import play.api.mvc.{ActionBuilder, Request, Result}

import scala.concurrent.Future

trait AccessLogging {
  private val accessLogger = Logger("Access")
  object AccessLoggingAction extends ActionBuilder[Request] {
    def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] = {
      accessLogger.info(s"method=${request.method} uri=${request.uri} remote-address=${request.remoteAddress}")
      block(request)
    }
  }
}

trait Logging {
  val log = Logger(this.getClass)
}
