package org.thp.thehive.dto.v0

import java.util.Date

import org.scalactic.Accumulation._
import org.scalactic.{Bad, Good, One}
import org.thp.scalligraph.InvalidFormatAttributeError
import org.thp.scalligraph.controllers._
import play.api.libs.json._

case class InputCustomField(
    name: String,
    description: String,
    `type`: String,
    options: List[JsValue] = Nil,
    reference: String
)

object InputCustomField {
  implicit val format: Format[InputCustomField] = Json.format[InputCustomField]
}

case class InputCustomFieldOption(value: Any)

case class OutputCustomField(name: String, reference: String, description: String, `type`: String, options: Seq[JsValue])

object OutputCustomField {
  implicit val format: OFormat[OutputCustomField] = Json.format[OutputCustomField]
}

case class InputCustomFieldValue(name: String, value: Option[Any])

object InputCustomFieldValue {

  val parser: FieldsParser[Seq[InputCustomFieldValue]] = FieldsParser("customFieldValue") {
    case (_, FObject(fields)) =>
      fields
        .toSeq
        .validatedBy {
          case (name, FString(value))   => Good(InputCustomFieldValue(name, Some(value)))
          case (name, FNumber(value))   => Good(InputCustomFieldValue(name, Some(value)))
          case (name, FBoolean(value))  => Good(InputCustomFieldValue(name, Some(value)))
          case (name, FAny(value :: _)) => Good(InputCustomFieldValue(name, Some(value)))
          case (name, FNull)            => Good(InputCustomFieldValue(name, None))
          case (name, other) =>
            Bad(
              One(
                InvalidFormatAttributeError(name, "CustomFieldValue", Set("field: string", "field: number", "field: boolean", "field: string"), other)
              )
            )
        }
        .map(_.toSeq)
    case (_, FUndefined) => Good(Nil)
  }

  implicit val writes: Writes[Seq[InputCustomFieldValue]] = Writes[Seq[InputCustomFieldValue]] { icfv =>
    val fields = icfv.map {
      case InputCustomFieldValue(name, Some(s: String))  => name -> JsString(s)
      case InputCustomFieldValue(name, Some(l: Long))    => name -> JsNumber(l)
      case InputCustomFieldValue(name, Some(d: Double))  => name -> JsNumber(d)
      case InputCustomFieldValue(name, Some(i: Integer)) => name -> JsNumber(i.toLong)
      case InputCustomFieldValue(name, Some(f: Float))   => name -> JsNumber(f.toDouble)
      case InputCustomFieldValue(name, Some(b: Boolean)) => name -> JsBoolean(b)
      case InputCustomFieldValue(name, Some(d: Date))    => name -> JsNumber(d.getTime)
      case InputCustomFieldValue(name, None)             => name -> JsNull
      case InputCustomFieldValue(name, other)            => sys.error(s"The custom field $name has invalid value: $other (${other.getClass})")
    }
    JsObject(fields)
  }
}

case class OutputCustomFieldValue(name: String, description: String, tpe: String, value: Option[String])

object OutputCustomFieldValue {
  implicit val format: OFormat[OutputCustomFieldValue] = Json.format[OutputCustomFieldValue]
}