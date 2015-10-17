package pl.michalkowol.controller

import pl.michalkowol.play.{AccessLogging, Logging}
import play.api.Logger
import play.api.mvc._

// scalastyle:off public.methods.have.type

class Application extends Controller with AccessLogging with Logging {

  def index = Action {
    Ok("Your new application is ready.")
  }

  def test = Action { request =>
    request.session.get("connected").map { user =>
      Ok("Hello " + user)
    }.getOrElse {
      Redirect(routes.Application.login("Michal"))
    }
  }

  def login(user: String) = AccessLoggingAction { request =>
    Logger.debug("to default logger")
    log.debug("to class logger")
    log.debug(s"${request.cookies.find(_.name == "user").map(_.value)}")
    Ok("Hello " + user)
      .withSession(request.session + ("connected" -> user))
      .withCookies(Cookie("user", user))
      .withHeaders("X-Michal" -> "Michal")
  }

  def logout = Action { request =>
    Ok("Logout").withNewSession
  }
}
