package io.igu.meetup.v2.model

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class Metadata(next: String,
                    method: String,
                    total_count: Int,
                    link: String,
                    count: Int,
                    description: String,
                    lon: String,
                    title: String,
                    url: String,
                    signed_url: Option[String],
                    id: String,
                    updated: Long,
                    lat: String)

object Metadata {
  implicit val encoder: Encoder[Metadata] = deriveEncoder[Metadata]
  implicit val decoder: Decoder[Metadata] = deriveDecoder[Metadata]
}