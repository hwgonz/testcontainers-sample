package com.acme.kafka

import com.acme.kafka.configuration.KafkaConsumerConfiguration

class BasicKafkaConsumer[A](
                              override val kafkaConsumerConfiguration: KafkaConsumerConfiguration,
                            ) extends KafkaConsumerBehavior[String, A]
