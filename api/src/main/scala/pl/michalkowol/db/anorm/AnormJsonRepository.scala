package pl.michalkowol.db.anorm

import anorm.SqlParser._
import anorm._
import pl.michalkowol.db.anorm.dbanorm._
import play.api.Play.current
import play.api.db.Database
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

class AnormJsonRepository(db: Database)(implicit ec: ExecutionContext) {

  def selectByJson: Seq[(Int, String, JsValue)] = db.withConnection { implicit c =>
    val sql = SQL"SELECT * from cities_with_json WHERE data->>'a' > '5'"
    sql.as((int("id") ~ str("name") ~ json("data") map flatten).*)
  }

  def insertJson: Unit = db.withConnection { implicit c =>
    val name = "Gliwice" + Random.nextInt(100)
    val json = Json.obj("a" -> Random.nextInt(10), "b" -> Map("a" -> "b"))
    val sql = SQL"""
                INSERT INTO cities_with_json (name, data)
                VALUES ($name, $json)
              """
    sql.executeUpdate()
  }
}
