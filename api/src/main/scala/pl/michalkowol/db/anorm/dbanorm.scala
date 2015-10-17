package pl.michalkowol.db.anorm

import anorm.SqlParser._
import anorm._
import org.postgresql.util.PGobject
import play.api.libs.json.{Json, JsValue}
import scala.language.implicitConversions

package object dbanorm {

  def json(columnName: String): RowParser[JsValue] = {
    get[JsValue](columnName)(rowToJsValue)
  }

  private def rowToJsValue: Column[JsValue] = Column.nonNull1 { (value, meta) =>
    val MetaDataItem(qualified, _, _) = meta
    value match {
      case pgo: PGobject if pgo.getType == "json" => Right(Json.parse(pgo.getValue))
      case _ => Left(TypeDoesNotMatch(s"Cannot convert $value:${value.asInstanceOf[AnyRef].getClass} to JsValue for column $qualified"))
    }
  }

  implicit class RichJsValue(jsValue: JsValue) {
    def toAnorm: anorm.Object = {
      val pgObject = new PGobject()
      pgObject.setType("json")
      pgObject.setValue(Json.stringify(jsValue))
      anorm.Object(pgObject)
    }
  }

  implicit def jsValueToParameterValue(jsValue: JsValue): ParameterValue = {
    ParameterValue.toParameterValue(RichJsValue(jsValue).toAnorm)
  }
}
