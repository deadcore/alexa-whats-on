package io.igu.meetup.v3

import io.igu.meetup.v3.model.Status
import io.igu.scalaj.http.HttpResponsePimp._
import scalaj.http.Http

trait StatusClientComponent {

  val statusClient: StatusClient

  trait StatusClient {

    def status: Status = {
      val response = Http("https://api.meetup.com/status").
        asString

      response.as[Status].body
    }

  }

}
