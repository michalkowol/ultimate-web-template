package pl.michalkowol.play

import com.paypal.cascade.json.JsonUtil
import pl.michalkowol.common.xml.XmlUtil
import pl.michalkowol.common.yaml.YamlUtil
import play.api.http.{ContentTypeOf, MediaRange, Writeable}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.{Accepting, AcceptExtractors, Codec, RequestHeader}

import scala.util.Try

trait Renderable extends Any {
  def value: Try[String]
}
class JsonValue(val value: Try[String]) extends AnyVal with Renderable
class XmlValue(val value: Try[String]) extends AnyVal with Renderable
class YamlValue(val value: Try[String]) extends AnyVal with Renderable

package object render extends AcceptExtractors {

  private val AcceptsYaml = Accepting("application/yaml")

  private def contentNegotiation[T](f: PartialFunction[MediaRange, Option[T]])(implicit request: RequestHeader): Option[T] = {
    def render(ms: Seq[MediaRange]): Option[T] = ms match {
      case Nil => None
      case Seq(m, ms @ _*) =>
        f.applyOrElse(m, (m: MediaRange) => render(ms))
    }

    if (request.acceptedTypes.isEmpty) {
      render(Seq(new MediaRange("*", "*", Nil, None, Nil)))
    } else {
      render(request.acceptedTypes)
    }
  }

  implicit class Renderer[T](marshallMe: T) {

    def render(implicit request: RequestHeader): Renderable = {
      contentNegotiation[Renderable] {
        case Accepts.Json() => Some(new JsonValue(JsonUtil.toJson(marshallMe)))
        //        case Accepts.Xml() => Some(new XmlValue(XmlUtil.toXml(marshallMe)))
        case AcceptsYaml() => Some(new YamlValue(YamlUtil.toYaml(marshallMe)))
      }.getOrElse(throw new Exception("NotAcceptable"))
    }
  }

  implicit def writeableOfRenderable(implicit codec: Codec, request: RequestHeader): Writeable[Renderable] = {
    Writeable(renderable => codec.encode(renderable.value.get))
  }

  implicit def contentTypeOfRenderable(implicit codec: Codec, request: RequestHeader): ContentTypeOf[Renderable] = {
    val contentTypeName = contentNegotiation[String] {
      case Accepts.Json() => Some("application/json")
      //      case Accepts.Xml() => Some("application/xml")
      case AcceptsYaml() => Some("application/yaml")
    }
    ContentTypeOf(contentTypeName)
  }
}
