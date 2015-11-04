package pl.michalkowol.common

import com.paypal.cascade.json.JsonUtil
import pl.michalkowol.play.JsonValue
import play.api.http.Writeable
import play.api.libs.iteratee.Execution
import play.api.libs.json._
import play.api.mvc.Codec

import scala.util.Try

// scalastyle:off object.name

package object json {
  object play {
    implicit class Marshallable[T](o: T)(implicit tjs: Writes[T]) {
      def toJson: JsValue = {
        Json.toJson(o)
      }
    }

    implicit class Unmarshallable(str: String) {
      def fromJson[T: Manifest](implicit fjs: Reads[T]): T = Json.parse(str).as[T]
      def validateJson[T: Manifest](implicit fjs: Reads[T]): JsResult[T] = Json.parse(str).validate[T]
    }
  }

  object jackson {
    implicit class Marshallable[T](o: T) {
      def toJson: Try[String] = JsonUtil.toJson(o)
      def renderJson: JsonValue = new JsonValue(toJson)
    }

    implicit class Convertible(convertMe: Any) {
      def convertJsonValue[T: Manifest]: Try[T] = JsonUtil.convertValue[T](convertMe)
    }

    implicit class Unmarshallable(str: String) {
      def fromJson[T: Manifest]: Try[T] = JsonUtil.fromJson[T](str)
    }

    implicit def writeableOfJson(implicit codec: Codec): Writeable[JsonValue] = {
      import Execution.Implicits.trampoline
      Writeable(json => codec.encode(json.value.get), Some("application/json"))
    }
  }
}
