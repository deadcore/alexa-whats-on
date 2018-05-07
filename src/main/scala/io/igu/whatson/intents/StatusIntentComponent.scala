package io.igu.whatson.intents

import io.igu.meetup.v3.StatusClientComponent
import io.igu.whatson.Intent
import io.igu.whatson.Intent.Receiver

trait StatusIntentComponent {
  self: StatusClientComponent =>

  val statusIntent: StatusIntent

  trait StatusIntent extends Intent {

    def receive: Receiver = {
      case "Status" => getStatusResponse
    }

    private def getStatusResponse = {
      val isHealthy = statusClient.status.isHealthy
      val speechText = if (isHealthy) "Everything looks healthy" else "We are experiencing problems with Meetup at the moment"
      askResponse("Status", speechText)
    }

  }


}

