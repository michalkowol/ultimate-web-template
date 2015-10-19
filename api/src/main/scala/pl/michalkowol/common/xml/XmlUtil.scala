package pl.michalkowol.common.xml

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.{DeserializationFeature, SerializationFeature}
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.scala._
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

import scala.util.Try

object XmlUtil {

  private val mapper = new XmlMapper() with ScalaObjectMapper

  mapper.registerModule(new OptionModule with TupleModule with SeqModule {})
  mapper.registerModule(DefaultScalaModule)
  mapper.registerModule(new JodaModule)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
  mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

  def toXml(value: Any): Try[String] = Try {
    mapper.writeValueAsString(value)
  }

  def fromXml[T: Manifest](json: String): Try[T] = Try {
    mapper.readValue[T](json)
  }

  def convertValue[T: Manifest](obj: Any): Try[T] = Try {
    mapper.convertValue[T](obj)
  }
}
