package com.acme.kafka

import com.acme.kafka.configuration.KafkaProducerConfiguration
import fs2.kafka._
import fs2.Pipe
import cats.effect._

trait KafkaProducerBehavior[A, B] {

  def kafkaProducerConfiguration: KafkaProducerConfiguration

  def producerSettings[F[_]](implicit
                             keySerializer: RecordSerializer[F, A],
                             valueSerializer: RecordSerializer[F, B],
                            ): ProducerSettings[F, A, B] =
    ProducerSettings[F, A, B]
      .withBootstrapServers(kafkaProducerConfiguration.bootstrapServers)
      .withRetries(5)

  def producerStream[F[_] : Async](implicit
                                   keySerializer: RecordSerializer[F, A],
                                   valueSerializer: RecordSerializer[F, B],
                                  ): fs2.Stream[F, KafkaProducer.Metrics[F, A, B]] =
    KafkaProducer.stream(producerSettings)

  def producerResource[F[_] : Async](implicit
                                     keySerializer: RecordSerializer[F, A],
                                     valueSerializer: RecordSerializer[F, B],
                                    ): Resource[F, KafkaProducer.Metrics[F, A, B]] =
    KafkaProducer.resource(producerSettings)

  def producerPipe[F[_] : Async](implicit
                                 keySerializer: RecordSerializer[F, A],
                                 valueSerializer: RecordSerializer[F, B],
                                ): Pipe[F, ProducerRecords[Unit, A, B], ProducerResult[Unit, A, B]] =
    KafkaProducer.pipe(producerSettings)

}
