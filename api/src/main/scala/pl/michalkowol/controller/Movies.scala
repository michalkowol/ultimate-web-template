package pl.michalkowol.controller

import play.api.mvc.{Action, Controller}
import pl.michalkowol.common.json.jackson._

// scalastyle:off public.methods.have.type

class Movies extends Controller {

  case class Movie(name: String, genre: String, forChildren: Boolean)

  def all = Action {
    val movies = Seq(
      Movie("Interstellar", "SciFi", forChildren = true),
      Movie("Titanic", "SciFi", forChildren = true),
      Movie("The Wolf of Wall Street", "Comedy", forChildren = false),
      Movie("Avengers", "SciFi", forChildren = true),
      Movie("Terminator Genisys", "SciFi", forChildren = false),
      Movie("Top Gun", "SciFi", forChildren = true),
      Movie("Iron Man", "Drama", forChildren = true),
      Movie("Forrest Gump", "Comedy", forChildren = true),
      Movie("Mission: Impossible", "Drama", forChildren = true)
    )
    Ok(movies.toJson)
  }
}
