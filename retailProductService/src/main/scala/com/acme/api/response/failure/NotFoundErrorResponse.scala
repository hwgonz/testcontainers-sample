package com.acme.api.response.failure

import com.acme.api.response.failure.ErrorCode.e30
import com.acme.api.response.failure.ErrorResponses.ErrorResponse
import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import sttp.tapir.Schema
case class NotFoundErrorResponse(
                                  override val message: String = "The object was not found",
                                  override val code: Option[String] = Some(e30.toString),
                                  override val errors: Iterable[Error] = Iterable.empty,
                                ) extends ErrorResponse

object NotFoundErrorResponse {

  implicit lazy val codec: Codec[NotFoundErrorResponse] = deriveCodec
  implicit lazy val schema: Schema[NotFoundErrorResponse] = Schema.derived

  def apply(notFoundException: NotFoundApiException): NotFoundErrorResponse =
    NotFoundErrorResponse(message = notFoundException.msg, code = Some(notFoundException.errorCode.toString))
}
