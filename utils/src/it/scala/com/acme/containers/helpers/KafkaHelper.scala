package com.acme.containers.helpers

import cats.effect.IO
import com.acme.containers.BaseContainers
import fs2.Pipe
import fs2.kafka._

import java.util.UUID

trait KafkaHelper {
  _: BaseContainers =>

  protected def kafkaProducer[T](
                                  implicit
                                  valueSerializer: Serializer[IO, T],
                                ): Pipe[IO, ProducerRecords[_, String, T], ProducerResult[_, String, T]] =
    KafkaProducer.pipe(
      ProducerSettings[IO, String, T]
        .withBootstrapServers(kafkaContainer.bootstrapServers)
        .withRetries(5)
    )

  protected def kafkaConsumer[T](topic: String, autoOffsetReset: AutoOffsetReset = AutoOffsetReset.Earliest)(
    implicit
    valueDeserializer: Deserializer[IO, T],
  ): fs2.Stream[IO, T] =
    KafkaConsumer
      .stream[IO, String, T](
        ConsumerSettings[IO, String, T]
          .withBootstrapServers(kafkaContainer.bootstrapServers)
          .withGroupId(s"kafka-it-test-consumer-${UUID.randomUUID()}")
          .withAutoOffsetReset(autoOffsetReset)
          .withIsolationLevel(IsolationLevel.ReadCommitted)
      )
      .evalTap(_.subscribeTo(topic))
      .flatMap(_.partitionedStream)
      .flatten
      .through(_.evalTap(_.offset.commit))
      .map(_.record.value)

}
