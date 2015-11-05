package pl.michalkowol.repository

import anorm.SqlParser._
import anorm._
import com.paypal.cascade.common.option._
import pl.michalkowol.db._
import pl.michalkowol.db.anorm.{Addresses, People}
import play.api.Play.current
import play.api.db.DB

import scala.concurrent.ExecutionContext
import scala.util.Try

class PeopleRepository(implicit ec: ExecutionContext) {

  def all: List[Person] = DB.withConnection { implicit c =>
    val sql = SQL"SELECT * FROM people"
    sql.as(People.person.*)
  }

  def allIds: List[Long] = DB.withConnection { implicit c =>
    val sql = SQL"SELECT id FROM people"
    sql.as(People.id.*)
  }

  def byId(id: Int): Option[Person] = DB.withConnection { implicit c =>
    val sql = SQL"SELECT * FROM people where id = $id"
    sql.as(People.person.singleOpt)
  }

  def allCustom: List[(Long, String, Option[String])] = DB.withConnection { implicit c =>
    val simpleParser = People.id ~ People.name ~ Addresses.street.? map flatten
    val sql = SQL"SELECT p.id, p.name, a.street FROM people as p LEFT JOIN addresses as a ON p.id = a.id"
    sql.as(simpleParser.*)
  }

  def create(person: Person): Try[Person] = Try {
    val insert: Option[Long] = DB.withConnection { implicit c =>
      val sql = SQL"INSERT INTO people(name, age) VALUES (${person.name}, ${person.age})"
      sql.executeInsert()
    }
    insert.flatMap(id => byId(id.toInt)).orThrow(new Exception("SQL Insert exception"))
  }

  object Advanced {
    private val peopleWithAddressParser = People.id ~ People.name ~ People.age ~ Addresses.street.? ~ str("city_name").? map {
      case id ~ name ~ age ~ Some(street) ~ Some(city) => (id, Person(name, age), Some((street, city)))
      case id ~ name ~ age ~ _ ~ _ => (id, Person(name, age), None)
    }

    def peopleWithAddresses: List[(Long, Person, Option[(String, String)])] = DB.withConnection { implicit c =>
      val sql =
        SQL"""
         SELECT p.id, p.name, p.age, a.street, c.name AS city_name FROM people AS p
         LEFT JOIN addresses_people AS ap ON ap.person_id = p.id
         LEFT JOIN addresses AS a ON a.id = ap.address_id
         LEFT JOIN cities AS c ON c.id = a.city_id
        """
      sql.as(peopleWithAddressParser.*)
    }
  }
}
