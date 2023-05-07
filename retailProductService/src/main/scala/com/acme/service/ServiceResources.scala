package com.acme.service

import cats.effect.{Resource, Sync}
import com.acme.kafka.BasicKafkaProducer
import com.acme.event.RetailProductSubmittedEvent
import com.acme.configuration.ServiceConfiguration

object ServiceResources {

  def resources[F[_] : Sync]: Resource[F, Resources[F]] =
    for {
      serviceConfiguration <- ServiceConfiguration()
      kafkaClient = new BasicKafkaProducer[RetailProductSubmittedEvent](
        serviceConfiguration.kafkaProducerConfiguration,
      )

    } yield Resources[F](serviceConfiguration, kafkaClient)

}
