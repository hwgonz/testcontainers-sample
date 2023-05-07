package com.acme.api.response.failure

import com.acme.api.response.failure.ErrorCode.e50
import com.acme.api.response.failure.ErrorResponses.ErrorResponse
import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import sttp.tapir.Schema
case class ServiceUnavailableErrorResponse(
                                            override val message: String = "The service is not currently available, retry later",
                                            override val code: Option[String] = Some(e50.toString),
                                            override val errors: Iterable[Error] = Iterable.empty,
                                          ) extends ErrorResponse

object ServiceUnavailableErrorResponse {

  implicit lazy val codec: Codec[ServiceUnavailableErrorResponse] = deriveCodec
  implicit lazy val schema: Schema[ServiceUnavailableErrorResponse] = Schema.derived

}
