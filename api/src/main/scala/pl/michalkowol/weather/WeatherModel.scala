package pl.michalkowol.weather

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

object Location {
  implicit val fmt: Format[Location] = Json.format[Location]
}

object Weather {
  implicit val reads: Reads[Weather] = (
    (__ \ 'temp).read[Double] and
    (__ \ 'temp_min).read[Double] and
    (__ \ 'temp_max).read[Double] and
    (__ \ 'pressure).read[Double] and
    (__ \ 'humidity).read[Double]
  )(Weather.apply _)
  implicit val writes: Writes[Weather] = Json.writes[Weather]
}

object City {
  implicit val reads: Reads[City] = (
    (__ \ 'name).read[String] and
    (__ \ 'sys \ 'country).read[String] and
    (__ \ 'coord).read[Location] and
    (__ \ 'main).read[Weather]
  )(City.apply _)
  implicit val writes: Writes[City] = Json.writes[City]
}

case class Location(lon: Double, lat: Double)
case class Weather(temp: Double, tempMin: Double, tempMax: Double, pressure: Double, humidity: Double)
case class City(name: String, country: String, location: Location, weather: Weather)
