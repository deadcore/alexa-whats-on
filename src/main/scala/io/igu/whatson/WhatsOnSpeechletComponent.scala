package io.igu.whatson


import com.amazon.speech.json.SpeechletRequestEnvelope
import com.amazon.speech.speechlet.{IntentRequest, LaunchRequest, SessionEndedRequest, SessionStartedRequest, SpeechletResponse, _}
import com.typesafe.scalalogging.LazyLogging
import io.igu.meetup.v2.ConciergeClientComponent
import io.igu.meetup.v3.StatusClientComponent
import io.igu.meetup.v3.member.ProfileClientComponent
import io.igu.whatson.intents.{FindWhatsOnIntentComponent, HelloWorldIntentComponent, StatusIntentComponent}
import io.igu.whatson.services.WhatsOnServiceComponent

trait WhatsOnSpeechletComponent {

  val whatsOnSpeechlet: WhatsOnSpeechlet

  trait WhatsOnSpeechlet extends LazyLogging with SpeechletV2 {
    val intent: Intent

    def onSessionStarted(requestEnvelope: SpeechletRequestEnvelope[SessionStartedRequest]): Unit = {
      logger.info("onSessionStarted requestId={}, sessionId={}", requestEnvelope.getRequest.getRequestId, requestEnvelope.getSession.getSessionId)
      // any initialization logic goes here
    }

    def onLaunch(requestEnvelope: SpeechletRequestEnvelope[LaunchRequest]): SpeechletResponse = {
      logger.info("onLaunch requestId={}, sessionId={}", requestEnvelope.getRequest.getRequestId, requestEnvelope.getSession.getSessionId)
      welcomeResponse
    }

    def onIntent(requestEnvelope: SpeechletRequestEnvelope[IntentRequest]): SpeechletResponse = {
      val request = requestEnvelope.getRequest
      logger.info("onIntent requestId={}, sessionId={}", request.getRequestId, requestEnvelope.getSession.getSessionId)

      intent(requestEnvelope).getOrElse {
        val speechText = "I'm sorry I don't know what to say to that"
        ResponseSupport.askResponse("Error", speechText)
      }
    }

    def onSessionEnded(requestEnvelope: SpeechletRequestEnvelope[SessionEndedRequest]): Unit = {
      logger.info("onSessionEnded requestId={}, sessionId={}", requestEnvelope.getRequest.getRequestId, requestEnvelope.getSession.getSessionId)
      // any cleanup logic goes here
    }

    private def welcomeResponse = {
      val speechText = "Welcome to the Alexa Skills Kit, you can say hello"
      ResponseSupport.askResponse("HelloWorld", speechText)
    }

  }

}

object WhatsOnSpeechletComponent extends WhatsOnSpeechletComponent with StatusIntentComponent
  with HelloWorldIntentComponent with FindWhatsOnIntentComponent {
  self =>

  override val statusIntent: StatusIntent = new StatusIntent with StatusClientComponent {
    override val statusClient: StatusClient = new StatusClient {}
  }

  override val helloWorldIntent: HelloWorldIntent = new HelloWorldIntent {}

  override val findWhatsOnIntent: FindWhatsOnIntent = new FindWhatsOnIntent with WhatsOnServiceComponent {
    override val whatsOnService: WhatsOnService = new WhatsOnService with ProfileClientComponent with ConciergeClientComponent {
      override val profileClient: ProfileClient = new ProfileClient {}
      override val conciergeClient: ConciergeClient = new ConciergeClient {}
    }
  }

  override val whatsOnSpeechlet: WhatsOnSpeechlet = new WhatsOnSpeechlet {
    override val intent: Intent = statusIntent.status ++ helloWorldIntent.helloWorld ++ findWhatsOnIntent.findWhatsOn
  }
}