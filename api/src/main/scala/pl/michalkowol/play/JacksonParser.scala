package pl.michalkowol.play

import com.paypal.cascade.json.JsonUtil
import pl.michalkowol.common.xml._
import pl.michalkowol.common.yaml.YamlUtil
import play.api.mvc.BodyParser
import play.api.mvc.BodyParsers.parse
import play.api.mvc.Results.{UnprocessableEntity, UnsupportedMediaType}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

// scalastyle:off cyclomatic.complexity

object JacksonParser {

  def as[T: Manifest](implicit ec: ExecutionContext): BodyParser[T] = parse.using {
    case req if req.contentType.exists(m => m.equalsIgnoreCase("text/json") || m.equalsIgnoreCase("application/json")) =>
      tolerantJsonAs[T]
    case req if req.contentType.exists(m => m.equalsIgnoreCase("text/xml") || m.equalsIgnoreCase("application/xml")) =>
      tolerantXmlAs[T]
    case req if req.contentType.exists(m => m.equalsIgnoreCase("text/yaml") || m.equalsIgnoreCase("application/yaml")) =>
      tolerantYamlAs[T]
    case _ =>
      parse.error(Future.successful(UnsupportedMediaType))
  }

  def tolerantJsonAs[T: Manifest](implicit ec: ExecutionContext): BodyParser[T] = parse.tolerantText.validate { text =>
    val json = JsonUtil.fromJson[T](text)
    json match {
      case Success(json) => Right(json)
      case Failure(ex) => Left(UnprocessableEntity)
    }
  }

  def tolerantXmlAs[T: Manifest](implicit ec: ExecutionContext): BodyParser[T] = parse.tolerantText.validate { text =>
    val xml = XmlUtil.fromXml[T](text)
    xml match {
      case Success(xml) => Right(xml)
      case Failure(ex) => Left(UnprocessableEntity)
    }
  }

  def tolerantYamlAs[T: Manifest](implicit ec: ExecutionContext): BodyParser[T] = parse.tolerantText.validate { text =>
    val yaml = YamlUtil.fromYaml[T](text)
    yaml match {
      case Success(yaml) => Right(yaml)
      case Failure(ex) => Left(UnprocessableEntity)
    }
  }
}
