package pl.michalkowol.controller

import com.paypal.cascade.common.option._
import pl.michalkowol.common.json.jackson._
import pl.michalkowol.controller.Movies.Movie
import pl.michalkowol.play.JacksonParser._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.{Action, Controller}

// scalastyle:off public.methods.have.type

object Movies {
  object Movie {
    def apply(name: String, genre: String, forChildren: Boolean, id: Long): Movie = {
      Movie(name, genre, forChildren, id.some)
    }
  }
  case class Movie(name: String, genre: String, forChildren: Boolean, id: Option[Long])
}

class Movies extends Controller {

  private var movies = Seq(
    Movie("Interstellar", "SciFi", forChildren = true, 1),
    Movie("Titanic", "SciFi", forChildren = true, 2),
    Movie("The Wolf of Wall Street", "Comedy", forChildren = false, 3),
    Movie("Avengers", "SciFi", forChildren = true, 4),
    Movie("Terminator Genisys", "SciFi", forChildren = false, 5),
    Movie("Top Gun", "SciFi", forChildren = true, 6),
    Movie("Iron Man", "Drama", forChildren = true, 7),
    Movie("Forrest Gump", "Comedy", forChildren = true, 8),
    Movie("Mission: Impossible", "Drama", forChildren = true, 9)
  )

  def all = Action {
    Ok(movies.toJson)
  }

  def create = Action(as[Movie]) { req =>
    val movie = req.body
    val id = movies.flatMap(_.id).max + 1
    val movieWithId = movie.copy(id = id.some)
    movies = movieWithId +: movies
    Created(movieWithId.toJson)
  }

  def get(id: Long) = Action {
    val movie = movies.find(movie => movie.id == id.some)
    movie match {
      case Some(movie) => Ok(movie.toJson)
      case None => NotFound
    }
  }
}
