package io.igu.meetup.v2

import io.igu.meetup.v2.model.{Event, MeetupResponse}
import io.igu.scalaj.http.HttpResponsePimp._
import scalaj.http.Http

trait ConciergeClientComponent {

  val conciergeClient: ConciergeClient

  trait ConciergeClient {
    def concierge(accessToken: String): MeetupResponse[List[Event]] = {
      val response = Http("https://api.meetup.com/2/events").
        header("Authorization", s"Bearer $accessToken").
        asString

      response.as[MeetupResponse[List[Event]]].body
    }
  }

}