package pl.michalkowol.db

case class City(name: String, id: Option[Long] = None)
case class Address(street: String, cityId: Long, id: Option[Long] = None)
case class Person(name: String, age: Int, id: Option[Long] = None)
case class AddressPerson(personId: Long, addressId: Long, id: Option[Long] = None)

case class Post(title: String, body: String, id: Option[Long] = None)

case class Movie(title: String, mpaaFilmRate: String, genre: String, id: Option[Long] = None) {
  val forChildren: Boolean = mpaaFilmRate == "G" || mpaaFilmRate == "PG"
}