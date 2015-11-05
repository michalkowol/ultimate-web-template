package pl.michalkowol.db.anorm

import java.sql.Connection

import anorm._
import anorm.{SqlParser, Macro, RowParser}
import pl.michalkowol.db.Post

object Posts25 {
  val post: RowParser[Post] = Macro.namedParser[Post] // new in 2.5
}

object AnyTableParser25 {
  val parser: RowParser[Map[String, Any]] = {
    SqlParser.folder(Map.empty[String, Any]) { (acc, value, meta) =>
      Right(acc + (meta.column.qualified -> value))
    }
  }
  // new in 2.5
  def result(implicit connection: Connection): List[Map[String, Any]] = SQL"SELECT * FROM dyn_table".as(parser.*)
}
