package io.igu.meetup.v2.model

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class Group(join_mode: String,
                 created: Long,
                 name: String,
                 group_lon: Double,
                 id: Int,
                 urlname: String,
                 group_lat: Double,
                 who: String)

object Group {
  implicit val encoder: Encoder[Group] = deriveEncoder[Group]
  implicit val decoder: Decoder[Group] = deriveDecoder[Group]
}