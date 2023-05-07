package com.acme.api.response.failure

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import sttp.tapir.Schema
case class Error(
                  code: String,
                  source: Option[String],
                  title: Option[String],
                  detail: Option[String]
                )

object Error {

  implicit lazy val codec: Codec[Error] = deriveCodec
  implicit lazy val schema: Schema[Error] = Schema.derived

}
