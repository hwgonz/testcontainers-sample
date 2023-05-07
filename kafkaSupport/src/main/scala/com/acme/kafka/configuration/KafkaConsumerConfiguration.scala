package com.acme.kafka.configuration

case class KafkaConsumerConfiguration(
                                       bootstrapServers: String,
                                       groupId: String,
                                       autoOffsetResetConfig: String,
                                       batchSize: Option[Int],
                                       batchDurationMilliseconds: Option[Long],
                                     )
