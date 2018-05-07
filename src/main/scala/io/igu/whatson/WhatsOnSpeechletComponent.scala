package io.igu.whatson


import com.amazon.speech.json.SpeechletRequestEnvelope
import com.amazon.speech.speechlet.{IntentRequest, LaunchRequest, SessionEndedRequest, SessionStartedRequest, SpeechletResponse, _}
import com.amazon.speech.ui.{OutputSpeech, PlainTextOutputSpeech, Reprompt, SimpleCard}
import com.typesafe.scalalogging.LazyLogging
import io.igu.meetup.v3.StatusClientComponent
import io.igu.whatson.intents.{HelloWorldIntentComponent, StatusIntentComponent}

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
      getWelcomeResponse
    }

    def onIntent(requestEnvelope: SpeechletRequestEnvelope[IntentRequest]): SpeechletResponse = {
      val request = requestEnvelope.getRequest
      logger.info("onIntent requestId={}, sessionId={}", request.getRequestId, requestEnvelope.getSession.getSessionId)

      intent(request)
    }

    def onSessionEnded(requestEnvelope: SpeechletRequestEnvelope[SessionEndedRequest]): Unit = {
      logger.info("onSessionEnded requestId={}, sessionId={}", requestEnvelope.getRequest.getRequestId, requestEnvelope.getSession.getSessionId)
      // any cleanup logic goes here
    }

    private def getWelcomeResponse = {
      val speechText = "Welcome to the Alexa Skills Kit, you can say hello"
      askResponse("HelloWorld", speechText)
    }

    private def getSimpleCard(title: String, content: String) = {
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

    private def getReprompt(outputSpeech: OutputSpeech) = {
      val reprompt = new Reprompt
      reprompt.setOutputSpeech(outputSpeech)
      reprompt
    }

    private def askResponse(cardTitle: String, speechText: String) = {
      val card = getSimpleCard(cardTitle, speechText)
      val speech = getPlainTextOutputSpeech(speechText)
      val reprompt = getReprompt(speech)
      SpeechletResponse.newAskResponse(speech, reprompt, card)
    }


  }

}

object WhatsOnSpeechletComponent extends WhatsOnSpeechletComponent with StatusClientComponent with StatusIntentComponent with HelloWorldIntentComponent {

  override val statusIntent: StatusIntent = new StatusIntent {}
  override val helloWorldIntent: HelloWorldIntent = new HelloWorldIntent {}
  override val statusClient: StatusClient = new StatusClient {}

  val whatsOnSpeechlet: WhatsOnSpeechlet = new WhatsOnSpeechlet {
    override val intent: Intent = statusIntent :+ helloWorldIntent
  }

}