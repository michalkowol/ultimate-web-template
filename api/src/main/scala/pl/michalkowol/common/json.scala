package pl.michalkowol.common

import com.paypal.cascade.json.JsonUtil
import play.api.libs.json._

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
      def toJson: JsValue = {
        val jsonText = JsonUtil.toJson(o).get
        Json.parse(jsonText)
      }
    }

    implicit class Unmarshallable(str: String) {
      def fromJson[T: Manifest]: Try[T] = JsonUtil.fromJson[T](str)
    }
  }
}
