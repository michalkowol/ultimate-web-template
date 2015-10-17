package pl.michalkowol.db.anorm

import anorm._
import pl.michalkowol.common.db._
import pl.michalkowol.db.Post
import pl.michalkowol.repository.PostRepository
import play.api.db.DB

import scala.concurrent.{ExecutionContext, Future}

class AnormPostRepository(implicit ec: ExecutionContext) extends PostRepository {

  def all: Future[Seq[Post]] = DB.withAsyncConnection { implicit c =>
    AnormJsonRepository.insertJson
    val sql = SQL"SELECT * FROM posts"
    sql.as(Posts25.post.*)
  }
}