package com.acme.api.response.success

import cats.effect.Concurrent
import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf
import sttp.tapir.Schema

import java.util.UUID

case class RetailProductAccepted(
                                  receivedTimestamp: Long,
                                  id:UUID
                                ) extends Success

object RetailProductAccepted {

  implicit lazy val codec: Codec[RetailProductAccepted] = deriveCodec
  implicit lazy val schema: Schema[RetailProductAccepted] = Schema.derived

  implicit def entityDecoder[F[_] : Concurrent]: EntityDecoder[F, RetailProductAccepted] = jsonOf[F, RetailProductAccepted]

}
