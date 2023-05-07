package com.acme.kafka

import com.acme.kafka.configuration.KafkaProducerConfiguration

class BasicKafkaProducer[A](
                              override val kafkaProducerConfiguration: KafkaProducerConfiguration,
                            ) extends KafkaProducerBehavior[String, A]
