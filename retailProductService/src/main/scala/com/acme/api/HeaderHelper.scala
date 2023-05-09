package com.acme.api

import cats.data.EitherT
import cats.effect.Sync
import com.acme.api.response.failure.ErrorResponses.ErrorType
object HeaderHelper {

  import cats.implicits._
  def logEntry[F[_] : Sync](message: String, log: String => Unit): F[Unit] =
    Sync[F].delay {
      log(s"$message")
    }

  /**
   * DO NOT log anything that is not a parsed header due to the risk of log4j exploit
   */
  def logEntryT[F[_] : Sync](message: String, log: String => Unit): EitherT[F, ErrorType, Unit] =
    EitherT(logEntry(message, log).map(_.asRight[ErrorType]))


}
