package pl.michalkowol.common

import java.sql.Connection
import scala.concurrent.{ExecutionContext, Future}
import play.api.Play.current
import play.api.db.Database

package object db {
  implicit class RichDB(db: Database) {
    def withAsyncConnection[A](block: Connection => A)(implicit ec: ExecutionContext): Future[A] = Future {
      db.withConnection(block)
    }

    def withAsyncConnection[A](autocommit: Boolean = true)(block: Connection => A)(implicit ec: ExecutionContext): Future[A] = Future { // scalastyle:ignore
      db.withConnection(autocommit)(block)
    }

    def withAsyncConnectionFlatMap[A](block: Connection => Future[A])(implicit ec: ExecutionContext): Future[A] = Future {
      db.withConnection(block)
    }.flatMap(identity)

    def withAsyncConnectionFlatMap[A](autocommit: Boolean = true)(block: Connection => Future[A])(implicit ec: ExecutionContext): Future[A] = Future { // scalastyle:ignore
      db.withConnection(autocommit)(block)
    }.flatMap(identity)
  }
}