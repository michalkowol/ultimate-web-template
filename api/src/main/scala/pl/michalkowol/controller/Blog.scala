package pl.michalkowol.controller

import pl.michalkowol.common.json.jackson._
import pl.michalkowol.play.Logging
import pl.michalkowol.repository.PostRepository
import play.api.mvc.{Action, Controller}
import play.api.libs.concurrent.Execution.Implicits._

// scalastyle:off public.methods.have.type

class Blog(postRepository: PostRepository) extends Controller with Logging {

  def posts = Action.async {
    val posts = postRepository.all
    posts.map { posts => Ok(posts.renderJson) }
  }
}
