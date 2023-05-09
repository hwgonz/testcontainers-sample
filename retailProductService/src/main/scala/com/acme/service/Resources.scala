package com.acme.service

import com.acme.configuration.ServiceConfiguration
import com.acme.event.RetailProductEvent
import fs2.kafka.KafkaProducer
import cats.effect.IO
case class Resources(
                      serviceConfiguration: ServiceConfiguration,
                      kafkaClient: KafkaProducer.Metrics[IO, String, RetailProductEvent],
                    )
