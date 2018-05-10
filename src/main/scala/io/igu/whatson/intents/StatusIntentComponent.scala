package io.igu.whatson.intents

import io.igu.meetup.v3.StatusClientComponent
import io.igu.whatson.{Intent, ResponseSupport}

trait StatusIntentComponent {

  val statusIntent: StatusIntent

  trait StatusIntent {
    self: StatusClientComponent =>

    def status = Intent {
      case "Status" => getStatusResponse
    }

    private def getStatusResponse = {
      val isHealthy = statusClient.status.isHealthy
      val speechText = if (isHealthy) "Everything looks healthy" else "We are experiencing problems with Meetup at the moment"
      ResponseSupport.askResponse("Status", speechText)
    }

  }


}

