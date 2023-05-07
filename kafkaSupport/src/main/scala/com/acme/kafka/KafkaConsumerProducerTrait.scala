package com.acme.kafka

import com.acme.kafka.configuration.{KafkaConsumerConfiguration, KafkaProducerConfiguration}

trait KafkaConsumerProducerTrait[A, B, C, D] extends KafkaConsumerBehavior[A, B] with KafkaProducerBehavior[C, D]

class KafkaConsumerProducer[A, B, C, D](
                                              override val kafkaConsumerConfiguration: KafkaConsumerConfiguration,
                                              override val kafkaProducerConfiguration: KafkaProducerConfiguration,
                                            ) extends KafkaConsumerProducerTrait[A, B, C, D]
