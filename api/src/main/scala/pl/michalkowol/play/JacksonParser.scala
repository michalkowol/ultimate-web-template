package pl.michalkowol.play

import play.api.mvc.BodyParser
import play.api.mvc.BodyParsers.parse

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import play.api.mvc.Results.{UnsupportedMediaType, UnprocessableEntity}

import pl.michalkowol.common.json.jackson._

object JacksonParser {

  def as[T: Manifest](implicit ec: ExecutionContext): BodyParser[T] = parse.when(
    _.contentType.exists(m => m.equalsIgnoreCase("text/json") || m.equalsIgnoreCase("application/json")),
    tolerantAs[T],
    req => Future.successful(UnsupportedMediaType)
  )

  def tolerantAs[T: Manifest](implicit ec: ExecutionContext): BodyParser[T] = parse.tolerantText.flatMap { text =>
    val json = text.fromJson[T]
    json match {
      case Success(json) => parse.ignore(json)
      case Failure(ex) => parse.error(Future.successful(UnprocessableEntity))
    }
  }
}
