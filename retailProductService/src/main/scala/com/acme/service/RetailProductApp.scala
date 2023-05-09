package com.acme.service

import com.acme.logging.Logging
import cats.effect.{ExitCode, IO, IOApp}
import com.acme.api.HttpServer
import com.acme.configuration.ServiceConfiguration
import com.acme.event.RetailProductEvent
import com.acme.kafka.BasicKafkaProducer
import com.acme.kafka.serdes.DomainSerDes
import fs2.kafka.{KafkaProducer, Serializer}
import com.acme.model.RetailProductCodecsCamelCase._

object RetailProductApp extends IOApp with DomainSerDes with Logging {

  implicit val serializer: Serializer[IO, RetailProductEvent] = serializer[IO, RetailProductEvent]

  override def run(args: List[String]): IO[ExitCode] = {
    val resources = for {
      serviceConfiguration <- ServiceConfiguration[IO]()
      kafkaClient = new BasicKafkaProducer[RetailProductEvent](
        serviceConfiguration.kafkaProducerConfiguration,
      )
      kafkaProducer <- kafkaClient.producerResource[IO]
    } yield Resources(serviceConfiguration, kafkaProducer)
    resources
      .use { r =>
        implicit val kafkaRetailProductProducer: KafkaProducer.Metrics[IO, String, RetailProductEvent] = r.kafkaClient
        IO(log.info(s"ENVIRONMENT = ${sys.env("ENVIRONMENT")}")) *>
          HttpServer[IO](
            r.serviceConfiguration,
          )

      }
  }

}
