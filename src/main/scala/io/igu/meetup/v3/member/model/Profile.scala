package io.igu.meetup.v3.member.model

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class Profile(id: Int,
                   name: String,
                   country: String,
                   city: String,
                   lat: BigDecimal,
                   lon: BigDecimal)

object Profile {
  implicit val encoder: Encoder[Profile] = deriveEncoder[Profile]
  implicit val decoder: Decoder[Profile] = deriveDecoder[Profile]
}