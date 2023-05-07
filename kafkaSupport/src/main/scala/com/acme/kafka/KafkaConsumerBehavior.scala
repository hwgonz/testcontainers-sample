package com.acme.kafka

import cats.effect._
import com.acme.kafka.configuration.KafkaConfigurationDefaults.{BatchDurationMilliseconds, BatchSize}
import com.acme.kafka.configuration.KafkaConsumerConfiguration
import fs2.kafka.{ConsumerSettings, IsolationLevel, KafkaConsumer, RecordDeserializer}
import org.apache.kafka.clients.consumer.ConsumerConfig
import scala.concurrent.duration._

trait KafkaConsumerBehavior[A,B] {

  def kafkaConsumerConfiguration: KafkaConsumerConfiguration

  val batchSize: Int =
    kafkaConsumerConfiguration
      .batchSize
      .getOrElse(BatchSize)

  val batchDuration: FiniteDuration =
    kafkaConsumerConfiguration
      .batchDurationMilliseconds
      .getOrElse(BatchDurationMilliseconds)
      .milliseconds

  /**
   * Extend this field to add custom properties in implementing classes.
   * Set this field to Map.empty to not set any properties.
   */
  def withConsumerProperties: Map[String, String] =
      Map(
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> kafkaConsumerConfiguration.autoOffsetResetConfig,
      )

  def consumerSettings[F[_] : Sync](implicit
                                    keyDeserializer: RecordDeserializer[F, A],
                                    valueDeserializer: RecordDeserializer[F, B],
                                   ): ConsumerSettings[F, A, B] =
    ConsumerSettings[F, A, B]
      .withBootstrapServers(kafkaConsumerConfiguration.bootstrapServers)
      .withGroupId(kafkaConsumerConfiguration.groupId)
      .withProperties(withConsumerProperties)
      .withIsolationLevel(IsolationLevel.ReadCommitted)

  def consumerStream[F[_] : Async](implicit
                                   keyDeserializer: RecordDeserializer[F, A],
                                   valueDeserializer: RecordDeserializer[F, B],
                                  ): fs2.Stream[F, KafkaConsumer[F, A, B]] = KafkaConsumer.stream(consumerSettings)

  def consumerResource[F[_] : Async](implicit
                                     keyDeserializer: RecordDeserializer[F, A],
                                     valueDeserializer: RecordDeserializer[F, B],
                                    ): Resource[F, KafkaConsumer[F, A, B]] = KafkaConsumer.resource(consumerSettings)

}
