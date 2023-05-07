package com.acme.service


import cats.effect.Sync
import com.acme.configuration.ServiceConfiguration
import com.acme.event.RetailProductSubmittedEvent
import com.acme.kafka.BasicKafkaProducer
case class Resources[F[_] : Sync](
                      serviceConfiguration: ServiceConfiguration,
                      kafkaClient: BasicKafkaProducer[RetailProductSubmittedEvent]
                    )
