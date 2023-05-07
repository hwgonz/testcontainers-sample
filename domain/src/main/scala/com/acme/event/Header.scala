package com.acme.event

import java.time.Instant
import java.util.UUID
case class Header(
                   eventTimestamp: Long,
                   eventId: Option[UUID],
                 )

object Header {

  def apply(businessEvent: BusinessEvent[_]): Header =
    apply(businessEvent.header)

  def apply(header: Header): Header =
    header.copy(
      eventTimestamp = Instant.now.toEpochMilli,
      eventId = Some(UUID.randomUUID),
    )

}
