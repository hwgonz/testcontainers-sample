package com.acme.api.response.success

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import sttp.tapir.Schema
case class DefaultSuccessResponse() extends Success

object DefaultSuccessResponse {

  implicit lazy val codec: Codec[DefaultSuccessResponse] = deriveCodec
  implicit lazy val schema: Schema[DefaultSuccessResponse] = Schema.derived

}
