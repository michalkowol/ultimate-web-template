package pl.michalkowol.play

import play.api.mvc.BodyParser
import play.api.mvc.BodyParsers.parse

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import play.api.mvc.Results.{UnsupportedMediaType, UnprocessableEntity}

import pl.michalkowol.common.json.jackson._
import pl.michalkowol.common.xml._

object JacksonParser {

  // to marshall: private implicit val w = new Writeable[Movie](_.toJsonText.getBytes, Some("application/json"))

  def as[T: Manifest](implicit ec: ExecutionContext): BodyParser[T] = parse.using {
    case req if req.contentType.exists(m => m.equalsIgnoreCase("text/json") || m.equalsIgnoreCase("application/json")) =>
      tolerantJsonAs[T]
    case req if req.contentType.exists(m => m.equalsIgnoreCase("text/xml") || m.equalsIgnoreCase("application/xml")) =>
      tolerantXmlAs[T]
    case _ =>
      parse.error(Future.successful(UnsupportedMediaType))
  }

  def tolerantJsonAs[T: Manifest](implicit ec: ExecutionContext): BodyParser[T] = parse.tolerantText.flatMap { text =>
    val json = text.fromJson[T]
    json match {
      case Success(json) => parse.ignore(json)
      case Failure(ex) => parse.error(Future.successful(UnprocessableEntity))
    }
  }

  def tolerantXmlAs[T: Manifest](implicit ec: ExecutionContext): BodyParser[T] = parse.tolerantText.flatMap { text =>
    val json = text.fromXml[T]
    json match {
      case Success(json) => parse.ignore(json)
      case Failure(ex) => parse.error(Future.successful(UnprocessableEntity))
    }
  }
}
