package io.igu.whatson


import com.amazon.speech.json.SpeechletRequestEnvelope
import com.amazon.speech.speechlet.SpeechletResponse.newAskResponse
import com.amazon.speech.speechlet.{IntentRequest, LaunchRequest, SessionEndedRequest, SessionStartedRequest, SpeechletResponse, _}
import com.amazon.speech.ui.{OutputSpeech, PlainTextOutputSpeech, Reprompt, SimpleCard}
import com.typesafe.scalalogging.LazyLogging
import io.igu.meetup.v2.ConciergeClientComponent
import io.igu.meetup.v3.StatusClientComponent
import io.igu.whatson.intents.{FindWhatsOnIntentComponent, HelloWorldIntentComponent, StatusIntentComponent}

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

      intent(requestEnvelope)
    }

    def onSessionEnded(requestEnvelope: SpeechletRequestEnvelope[SessionEndedRequest]): Unit = {
      logger.info("onSessionEnded requestId={}, sessionId={}", requestEnvelope.getRequest.getRequestId, requestEnvelope.getSession.getSessionId)
      // any cleanup logic goes here
    }

    private def welcomeResponse = {
      val speechText = "Welcome to the Alexa Skills Kit, you can say hello"
      askResponse("HelloWorld", speechText)
    }

    private def simpleCard(title: String, content: String): SimpleCard = {
      val card = new SimpleCard
      card.setTitle(title)
      card.setContent(content)
      card
    }

    private def getPlainTextOutputSpeech(speechText: String) = {
      val speech = new PlainTextOutputSpeech
      speech.setText(speechText)
      speech
    }

    private def reprompt(outputSpeech: OutputSpeech) = {
      val reprompt = new Reprompt
      reprompt.setOutputSpeech(outputSpeech)
      reprompt
    }

    private def askResponse(cardTitle: String, speechText: String) = {
      val card = simpleCard(cardTitle, speechText)
      val speech = getPlainTextOutputSpeech(speechText)
      newAskResponse(speech, reprompt(speech), card)
    }


  }

}

object WhatsOnSpeechletComponent extends WhatsOnSpeechletComponent with StatusClientComponent with StatusIntentComponent with HelloWorldIntentComponent with FindWhatsOnIntentComponent {

  override val statusIntent: StatusIntent = new StatusIntent {}
  override val helloWorldIntent: HelloWorldIntent = new HelloWorldIntent {}
  override val statusClient: StatusClient = new StatusClient {}
  override val findWhatsOnIntent: FindWhatsOnIntent = new FindWhatsOnIntent with ConciergeClientComponent {
    override val conciergeClient: ConciergeClient = new ConciergeClient {}
  }

  override val whatsOnSpeechlet: WhatsOnSpeechlet = new WhatsOnSpeechlet {
    override val intent: Intent = statusIntent :+ helloWorldIntent :+ findWhatsOnIntent
  }
}