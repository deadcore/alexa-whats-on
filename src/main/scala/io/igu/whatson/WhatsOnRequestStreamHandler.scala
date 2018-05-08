package io.igu.whatson

import java.util.Collections.emptySet

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler

class WhatsOnRequestStreamHandler extends SpeechletRequestStreamHandler(WhatsOnSpeechletComponent.whatsOnSpeechlet, emptySet[String]())