package io.igu.whatson.intents

import io.igu.meetup.v2.ConciergeClientComponent
import io.igu.whatson.Intent
import io.igu.whatson.Intent.Receiver

trait FindWhatsOnIntentComponent {

  val findWhatsOnIntent: FindWhatsOnIntent

  trait FindWhatsOnIntent extends Intent {
    self: ConciergeClientComponent =>

    override def receive: Receiver = {
      case "FindWhatsOn" =>
        val events = user.map(_.getAccessToken).map(findEvents).toList.flatten.headOption

        events match {
          case Some(event) => askResponse("FindWhatsOn", s"I found an event for you: ${event.name}")
          case None        => askResponse("FindWhatsOn", "I was unable to find any events")
        }
    }

    private def user = request.
      map(_.getSession).
      map(_.getUser)

    private def findEvents(token: String) = conciergeClient.concierge(token).results

  }

}
