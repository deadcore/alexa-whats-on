package io.igu.whatson.intents

import com.amazon.speech.json.SpeechletRequestEnvelope
import com.amazon.speech.speechlet.{IntentRequest, User}
import io.igu.meetup.v2.ConciergeClientComponent
import io.igu.meetup.v2.model.Event
import io.igu.whatson.{Intent, ResponseSupport}

trait FindWhatsOnIntentComponent {

  val findWhatsOnIntent: FindWhatsOnIntent

  trait FindWhatsOnIntent {
    self: ConciergeClientComponent =>

    def findWhatsOn: Intent = Intent.withRequest { request: SpeechletRequestEnvelope[IntentRequest] => {
      case "FindWhatsOn" =>
        val events = user(request).map(_.getAccessToken).map(findEvents).toList.flatten.headOption

        events match {
          case Some(event) => ResponseSupport.askResponse("FindWhatsOn", s"I found an event for you: ${event.name}")
          case None        => ResponseSupport.askResponse("FindWhatsOn", "I was unable to find any events")
        }
    }
    }


    private def user(request: SpeechletRequestEnvelope[IntentRequest]): Option[User] = Option(request).
      map(_.getSession).
      map(_.getUser)

    private def findEvents(token: String): List[Event] = conciergeClient.concierge(token).results

  }

}
