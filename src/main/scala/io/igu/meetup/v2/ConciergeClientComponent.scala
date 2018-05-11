package io.igu.meetup.v2

import com.typesafe.scalalogging.LazyLogging
import io.circe.generic.extras.Configuration
import io.igu.meetup.v2.model.{ConciergeRequest, Event, MeetupResponse}
import io.igu.scalaj.http.HttpResponsePimp._
import io.igu.whatson.{ApiException, WsCallFailedException}
import scalaj.http.{Http, HttpResponse}

trait ConciergeClientComponent {

  val conciergeClient: ConciergeClient

  trait ConciergeClient extends LazyLogging {
    private implicit val config: Configuration = Configuration.default.withSnakeCaseMemberNames

    def concierge(request: ConciergeRequest, accessToken: String): MeetupResponse[Seq[Event]] = {
      logger.info("Sending request to [https://api.meetup.com/2/concierge] with access token [{}]", accessToken)
      val response = Http("https://api.meetup.com/2/concierge").
        params(request.flatternToMap).
        header("Authorization", s"Bearer $accessToken").
        asString

      logger.info("Got response from [https://api.meetup.com/2/concierge]: [{}]", response.body)

      response.code match {
        case 200 => response.as[MeetupResponse[Seq[Event]]].body
        case _   => throw responseAsException(response)
      }
    }

    private def responseAsException(response: HttpResponse[String]): ApiException = {
      logger.info("Unexpected response from [https://api.meetup.com/2/events] with status code [{}] and body [{}]", response.code, response.body)
      new WsCallFailedException
    }
  }

}