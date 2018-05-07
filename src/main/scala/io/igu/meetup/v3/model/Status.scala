package io.igu.meetup.v3.model

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class Status(status: String) {

  def isHealthy: Boolean = status == "ok"

}

object Status {
  implicit val encoder: Encoder[Status] = deriveEncoder[Status]
  implicit val decoder: Decoder[Status] = deriveDecoder[Status]
}