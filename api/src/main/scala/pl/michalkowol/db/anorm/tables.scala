package pl.michalkowol.db.anorm

import anorm.SqlParser._
import pl.michalkowol.db._

// Workaround: (User.apply _).tupled

object People {
  val id = long("id")
  val name = str("name")
  val age = int("age")

  val person = name ~ age ~ id.? map flatten map Person.tupled
}

object Addresses {
  val id = long("id")
  val street = str("street")
  val cityId = long("city_id")

  val address = street ~ cityId ~ id.? map flatten map Address.tupled
}

object Cities {
  val id = long("id")
  val name = str("name")

  val city = name ~ id.? map flatten map City.tupled
}

object AddressesPeople {
  val id = long("id")
  val personId = long("person_id")
  val addressId = long("address_id")

  val addressPerson = personId ~ addressId ~ id.? map flatten map AddressPerson.tupled
}

object Posts {
  val id = long("id")
  val title = str("title")
  val body = str("body")

  val post = title ~ body ~ id.? map flatten map Post.tupled
}

object Movies {
  val id = long("id")
  val title = str("title")
  val mpaaFilmRateId = int("mpaa_film_rate_id")
  val genreId = int("genre_id")
}

object Genres {
  val id = long("id")
  val name = str("name")

  val genre = id ~ name map flatten
}

object MPAAFilmRatings {
  val id = long("id")
  val name = str("name")

  val rating = id ~ name map flatten
}
