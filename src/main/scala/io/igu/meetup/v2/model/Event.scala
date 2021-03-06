package io.igu.meetup.v2.model

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class Event(utc_offset: Int,
                 venue: Option[Venue],
                 headcount: Option[Int],
                 visibility: String,
                 waitlist_count: Int,
                 created: Option[Long],
                 maybe_rsvp_count: Option[Int],
                 description: Option[String],
                 event_url: Option[String],
                 yes_rsvp_count: Int,
                 duration: Option[Int],
                 name: String,
                 id: String,
                 photo_url: Option[String],
                 time: Long,
                 updated: Option[Long],
                 group: Group,
                 status: Option[String],
                 rsvp_limit: Option[Int],
                 how_to_find_us: Option[String],
                 fee: Option[Fee])

object Event {
  implicit val encoder: Encoder[Event] = deriveEncoder[Event]
  implicit val decoder: Decoder[Event] = deriveDecoder[Event]
}