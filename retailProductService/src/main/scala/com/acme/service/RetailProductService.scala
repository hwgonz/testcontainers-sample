package com.acme.service


import cats.effect.Sync
import com.acme.api.response.failure.ErrorResponses.ErrorResponse
import com.acme.event.RetailProductEvent
import com.acme.logging.Logging
import fs2.kafka.{KafkaProducer, ProducerRecord, ProducerRecords}
import cats.implicits._
import com.acme.api.response.failure.{BadRequestErrorResponse, DefaultErrorResponse}
import com.acme.model.RetailProduct
import io.circe.parser._

import java.time.Instant
import java.util.UUID
object RetailProductService extends Logging {

  import com.acme.model.RetailProductCodecsCamelCase._

  val retailProductTopic: String = "retail-product-submitted"
  def persistRetailProduct[F[_] : Sync](
                                         retailProductId: UUID,
                                         data: String
                                   )(implicit
                                     kafkaSuccess: KafkaProducer.Metrics[F, String, RetailProductEvent],
                                   ): F[Either[ErrorResponse, Boolean]] = {
    val productJson = parse(data)
    productJson match {
      case Left(ex) => Sync[F].delay(Left(BadRequestErrorResponse(ex)))
      case Right(json) =>
        val retailProduct = for {
          result <- json.as[RetailProduct]
        } yield result
        retailProduct match {
          case Right(product) =>
            val retailProductEvent = RetailProductEvent(product)
            val kafkaSubmit = sendToKafka(retailProductEvent, retailProductTopic)
            val kafkaLog = Sync[F].delay(log.info(s"Persisted retail product $retailProductId to kafka at ${Instant.now} with event id ${retailProductEvent.header.eventId.getOrElse("")}"))
            kafkaSubmit <* kafkaLog
          case Left(err) => Sync[F].delay(Left(BadRequestErrorResponse(err)))
        }

    }

  }

  private def sendToKafka[F[_] : Sync](
                                        retailProductEvent: RetailProductEvent,
                                        topic: String)(implicit
                                                       kafka: KafkaProducer.Metrics[F, String, RetailProductEvent],
                                      ): F[Either[ErrorResponse, Boolean]] = {
    val key = retailProductEvent.data.id.toString
    val record = ProducerRecords.one(ProducerRecord(topic, key, retailProductEvent))
    kafka.produce(record)
      .flatMap(_.map(f =>
        if (f.records.nonEmpty) Right[ErrorResponse, Boolean](true)
        else Left[ErrorResponse, Boolean](DefaultErrorResponse())
      ))
  }

}
