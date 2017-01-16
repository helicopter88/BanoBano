package com.test

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.stereotype.Controller
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.atomic.AtomicLong

@Controller
@EnableWebSocket
class FrontendHandler : TextWebSocketHandler() {
    private val counter = AtomicLong()
    private val jsonParser = BanoBanoApplication.JsonParser

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val json = jsonParser.parseMap(message.payload)
        val value = when (json["msg"]) {
            "start_job" -> startJob(get(json, "type"), get(json, "model_git"), parseList(json, "student_gits"))
            else -> -1
        }
        println(value)
        val w = BanoBanoApplication.Mapper.writer()
        val s = w.writeValueAsString(mapOf(Pair("job_id", value)))
        session.sendMessage(TextMessage(s))

    }

    private fun parseList(json: Map<String, Any>, key: String): Array<String> {
        val list = get(json, key)
        return list.removePrefix("[").removeSuffix("]").split(",").toTypedArray()
    }

    private fun get(json: Map<String, Any>, key: String): String {
        return json[key].toString()
    }

    @JsonFormat
    private fun startJob(type: String, modelGit: String, studentGits: Array<String>): Long {
        val t = Task(counter.incrementAndGet(), type, modelGit, studentGits)
        BackendHandler.submitTask(t)
        return counter.get()
    }
}

