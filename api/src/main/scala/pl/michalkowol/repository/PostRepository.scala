package pl.michalkowol.repository

import pl.michalkowol.db.Post

import scala.concurrent.Future

trait PostRepository {
  def all: Future[Seq[Post]]
}
