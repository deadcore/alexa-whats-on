package io.igu.meetup.v3.member

import com.typesafe.scalalogging.LazyLogging
import io.circe.generic.extras.Configuration
import io.igu.meetup.v3.member.model.Profile
import io.igu.scalaj.http.HttpResponsePimp._
import io.igu.whatson.{ApiException, WsCallFailedException}
import scalaj.http.{Http, HttpResponse}

trait ProfileClientComponent {

  val profileClient: ProfileClient

  trait ProfileClient extends LazyLogging {
    private implicit val config: Configuration = Configuration.default.withSnakeCaseMemberNames

    def self(accessToken: String): Profile = {
      val response = Http("https://api.meetup.com/members/self").
        header("Authorization", s"Bearer $accessToken").
        asString

      logger.info("Got response from [https://api.meetup.com/members/self]: [{}]", response.body)

      response.code match {
        case 200 => response.as[Profile].body
        case _   => throw responseAsException(response)
      }
    }

    private def responseAsException(response: HttpResponse[String]): ApiException = {
      logger.info("Unexpected response from [https://api.meetup.com/members/self] with status code [{}] and body [{}]", response.code, response.body)
      new WsCallFailedException
    }
  }

}
