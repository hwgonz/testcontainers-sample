package com.acme.api.response.failure

import cats.data.NonEmptyList
import com.acme.api.response.failure.ErrorCode.e100
import com.acme.api.response.failure.ErrorResponses.{ErrorResponse, MessageError}
import io.circe.generic.semiauto.deriveCodec
import io.circe.{Codec, CursorOp, DecodingFailure, ParsingFailure}
import sttp.tapir.Schema
case class DefaultErrorResponse(
                                 override val message: String = MessageError,
                                 override val code: Option[String] = Some(e100.toString),
                                 override val errors: Iterable[Error] = Iterable.empty,
                               ) extends ErrorResponse

object DefaultErrorResponse {

  implicit lazy val codec: Codec[DefaultErrorResponse] = deriveCodec
  implicit lazy val schemaError: Schema[Error] = Schema.derived
  implicit lazy val schemaErrorResponse: Schema[DefaultErrorResponse] = Schema.derived

  def apply[A](nelErrors: NonEmptyList[A]): DefaultErrorResponse =
    new DefaultErrorResponse(
      errors = nelErrors.map {
        case df: DecodingFailure =>
          Error(
            code = e100.toString,
            source = Some(CursorOp.opsToPath(df.history)),
            title = Option("A decoding failure has occurred"),
            detail = Option(df.message),
          )
        case parsingFailure: ParsingFailure =>
          Error(
            code = e100.toString,
            source = None,
            title = Option("A parsing failure has occurred"),
            detail = Option(parsingFailure.message),
          )
        case _ =>
          Error(
            code = e100.toString,
            source = None,
            title = Option("An error has occurred"),
            detail = None,
          )
      }.toList
    )

  def apply(
             throwable: Throwable,
           ): DefaultErrorResponse =
    DefaultErrorResponse(
      errors = Iterable(
        Error(
          code = e100.toString,
          source = None,
          title = Option("An error occurred"),
          detail = Option(throwable.getMessage)
        )
      ).toList
    )

  def apply(
             throwable: Throwable,
             errorCode: ErrorCode.Value,
           ): DefaultErrorResponse =
    DefaultErrorResponse(
      code = Some(errorCode.toString),
      errors = Iterable(
        Error(
          code = errorCode.toString,
          source = None,
          title = Option("An error occurred"),
          detail = Option(throwable.getMessage)
        )
      ).toList
    )
}
