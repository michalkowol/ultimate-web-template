package pl.michalkowol.repository

import anorm._
import pl.michalkowol.common.db._
import pl.michalkowol.db.Post
import pl.michalkowol.db.anorm.Posts25
import play.api.db.DB

import scala.concurrent.{ExecutionContext, Future}

class PostsRepository(implicit ec: ExecutionContext) {

  def all: Future[Seq[Post]] = DB.withAsyncConnection { implicit c =>
    val sql = SQL"SELECT * FROM posts"
    sql.as(Posts25.post.*)
  }
}