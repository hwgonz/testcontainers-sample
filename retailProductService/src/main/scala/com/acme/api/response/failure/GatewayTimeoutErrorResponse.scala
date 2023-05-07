package com.acme.api.response.failure

import com.acme.api.response.failure.ErrorCode.e12
import com.acme.api.response.failure.ErrorResponses.ErrorResponse
import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import sttp.tapir.Schema
case class GatewayTimeoutErrorResponse(
                                        override val message: String = "An internal operation timed out. Please try again",
                                        override val code: Option[String] = Some(e12.toString),
                                        override val errors: Iterable[Error] = Iterable.empty,
                                      ) extends ErrorResponse

object GatewayTimeoutErrorResponse {

  implicit lazy val codec: Codec[GatewayTimeoutErrorResponse] = deriveCodec
  implicit lazy val schema: Schema[GatewayTimeoutErrorResponse] = Schema.derived

}
