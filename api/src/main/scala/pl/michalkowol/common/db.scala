package pl.michalkowol.common

import java.sql.Connection
import scala.concurrent.{ExecutionContext, Future}
import play.api.Play.current

package object db {
  implicit class RichDB(db: play.api.db.DB.type) {
    def withAsyncConnection[A](block: Connection => A)(implicit ec: ExecutionContext): Future[A] = Future {
      db.withConnection(block)
    }

    def withAsyncConnection[A](name: String = "default", autocommit: Boolean = true)(block: Connection => A)(implicit ec: ExecutionContext): Future[A] = Future {
      db.withConnection(name, autocommit)(block)
    }

    def withAsyncConnectionFlatMap[A](block: Connection => Future[A])(implicit ec: ExecutionContext): Future[A] = Future {
      db.withConnection(block)
    }.flatMap(identity)

    def withAsyncConnectionFlatMap[A](name: String = "default", autocommit: Boolean = true)(block: Connection => Future[A])(implicit ec: ExecutionContext): Future[A] = Future {
      db.withConnection(name, autocommit)(block)
    }.flatMap(identity)
  }
}