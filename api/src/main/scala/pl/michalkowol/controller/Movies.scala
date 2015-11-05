package pl.michalkowol.controller

import pl.michalkowol.db.Movie
import pl.michalkowol.play.JacksonParser._
import pl.michalkowol.play.render._
import pl.michalkowol.repository.MoviesRepository
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.{Action, Controller}
import com.paypal.cascade.common.future._

import scala.util.control.NonFatal

// scalastyle:off public.methods.have.type

class Movies(moviesRepository: MoviesRepository) extends Controller {

  def all = Action.async { implicit req =>
    val movies = moviesRepository.all
    movies.map(movies => Ok(movies.render))
  }

  def create = Action.async(as[Movie]) { implicit req =>
    val movie = req.body
    val createdMovie = moviesRepository.create(movie)
    createdMovie.map(movie => Created(movie.render))
  }

  def get(id: Long) = Action.async { implicit req =>
    val movie = moviesRepository.byId(id)
    movie.map(movie => Ok(movie.render)).recover {
      case NonFatal(e) => NotFound
    }
  }
}
