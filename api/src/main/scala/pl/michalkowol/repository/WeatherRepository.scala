package pl.michalkowol.repository

import dispatch.Defaults._
import dispatch._
import pl.michalkowol.common.json.play._
import pl.michalkowol.controller.Weather.{KrakowException, WarszawaException}
import pl.michalkowol.play.Logging
import pl.michalkowol.weather.City

import scala.concurrent.Future

class WeatherRepository extends Logging {

  object Metric {
    def weatherInCity(country: String, city: String): Future[City] = {
      WeatherRepository.this.weatherInCity(country, city, "metric")
    }
  }

  private def weatherInCity(country: String, city: String, units: String): Future[City] = {
    val request = url("http://api.openweathermap.org/data/2.5/weather")
      .addQueryParameter("q", s"$city,$country")
      .addQueryParameter("units", units)
      .addQueryParameter("appid", "1cdb641f9aefc3ea36888923aa99c069")
    log.info(s"Exec query: ${request.url}")
    val response = Http(request OK as.String)
    if (city == "Krakow") throw new KrakowException("I love Warszawa!")
    if (city == "Warszawa") throw new WarszawaException("I love Krakow!")
    response.map(_.fromJson[City])
  }
}
