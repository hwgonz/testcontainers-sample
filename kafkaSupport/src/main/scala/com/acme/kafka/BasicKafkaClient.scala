package com.acme.kafka

import com.acme.kafka.configuration.{KafkaConsumerConfiguration, KafkaProducerConfiguration}

class BasicKafkaClient[A, B](
                               override val kafkaConsumerConfiguration: KafkaConsumerConfiguration,
                               override val kafkaProducerConfiguration: KafkaProducerConfiguration,
                             ) extends KafkaConsumerProducerTrait[String, A, String, B]
