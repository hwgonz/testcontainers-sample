package com.acme.kafka.serdes

import cats.effect.Sync
import fs2.kafka.{Deserializer, Serializer}
import io.circe.{Decoder, Encoder, Json}
import java.nio.charset.StandardCharsets

object DomainSerDes extends DomainSerDes
trait DomainSerDes {

  implicit def serializer[F[_], A](implicit encoder: Encoder[A], sync: Sync[F]): Serializer[F, A] = {
    import io.circe.syntax.EncoderOps
    Serializer.lift(event => Sync[F].pure(event.asJson.noSpaces.getBytes(StandardCharsets.UTF_8)))
  }

  def decodeJson[A](bytes: Array[Byte])(implicit d: Decoder[A]): Either[Throwable, A] = {
    import io.circe.parser.parse
    for {
      json <- parse(new String(bytes, StandardCharsets.UTF_8))
      app <- json.as[A]
    } yield app
  }

  def decodeJson[A](
                     bytes: Array[Byte],
                     f: Json => Either[Throwable, A],
                   ): Either[Throwable, A] = {
    import io.circe.parser.parse
    for {
      json <- parse(new String(bytes, StandardCharsets.UTF_8))
      app <- f(json)
    } yield app
  }

  implicit def jsonDeserializer[F[_], A](implicit
                                         decoder: Decoder[A],
                                         sync: Sync[F
                                         ]): Deserializer[F, A] = {
    Deserializer.lift { bytes =>
      decodeJson(bytes)(decoder) match {
        case Right(p) => sync.pure(p)
        case Left(e) => sync.raiseError(e)
      }
    }
  }

  implicit def jsonDeserializer[F[_], A](
                                          f: Json => Either[Throwable, A],
                                        )(implicit sync: Sync[F]): Deserializer[F, A] = {
    Deserializer.lift { bytes =>
      decodeJson(bytes, f) match {
        case Right(p) => sync.pure(p)
        case Left(e) => sync.raiseError(e)
      }
    }
  }

}
