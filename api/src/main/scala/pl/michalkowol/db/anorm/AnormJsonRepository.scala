package pl.michalkowol.db.anorm

import anorm.SqlParser._
import anorm._
import pl.michalkowol.db.anorm.dbanorm._
import play.api.Play.current
import play.api.db.DB
import play.api.libs.json._

import scala.util.Random

object AnormJsonRepository {

  def selectByJson: Seq[(Int, String, JsValue)] = DB.withConnection { implicit c =>
    val sql = SQL"SELECT * from cities_with_json WHERE data->>'a' > '5'"
    sql.as((int("id") ~ str("name") ~ json("data") map flatten).*)
  }

  def insertJson: Unit = DB.withConnection { implicit c =>
    val name = "Gliwice" + Random.nextInt(100)
    val json = Json.obj("a" -> Random.nextInt(10), "b" -> Map("a" -> "b"))
    val sql = SQL"""
                INSERT INTO cities_with_json (name, data)
                VALUES ($name, $json)
              """
    sql.executeUpdate()
  }
}
