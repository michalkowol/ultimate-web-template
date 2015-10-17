package pl.michalkowol.controller

import pl.michalkowol.common.json.play._
import pl.michalkowol.play.Logging
import pl.michalkowol.repository.WeatherRepository
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.{Action, Controller}

// scalastyle:off public.methods.have.type

object Weather {
  sealed abstract class CityException(msg: String, cause: Throwable) extends RuntimeException(msg, cause)
  class KrakowException(msg: String, cause: Option[Throwable] = None) extends CityException(msg, cause.orNull)
  class WarszawaException(msg: String, cause: Option[Throwable] = None) extends CityException(msg, cause.orNull)
}

class Weather(weatherRepository: WeatherRepository) extends Controller with Logging {
  def weatherInCity(country: String, cityName: String) = Action.async {
    log.info(s"Search for (before): $cityName")
    val city = weatherRepository.Metric.weatherInCity(country, cityName)
    log.info(s"Search for (after): $cityName")
    city.map { city => Ok(city.toJson) }
  }
}
