package com.test

import org.eclipse.jgit.api.Git
import org.springframework.stereotype.Controller
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.io.File
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

@Controller
class BackendHandler : TextWebSocketHandler() {

    private val MAX_SERVICES = AtomicInteger()
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val payload = message.payload
        val m = jsonParser.parseMap(payload)
        when (m["msg"]) {
            "register" -> registerService(session)
            "job_complete" -> jobComplete(session, payload)
            "close" -> closeService(session)
            else -> session.sendMessage(TextMessage("${m["msg"]} not recognised"))
        }
    }

    private fun registerService(session: WebSocketSession) {
        println("HELLO")
        val ws = services.put(session.id, session)
        if (ws == null)
            MAX_SERVICES.andIncrement
        session.sendMessage(TextMessage("Registered"))
    }

    fun closeService(session: WebSocketSession) {
        session.close()
        services.remove(session.id)
        MAX_SERVICES.decrementAndGet()
    }

    private fun jobComplete(session: WebSocketSession, message: String) {
        val m = jsonParser.parseMap(message)
        val id = m["id"]!!.toString().toLong()
        //tasks[id]!!.incrementAndGet() == MAX_SERVICES.get()
    }

    companion object {
        private val services = HashMap<String, WebSocketSession>()
        private val tasks = HashMap<Long, AtomicInteger>()
        private val jsonParser = BanoBanoApplication.JsonParser

        fun submitTask(task: Task) {
            tasks[task.id] = AtomicInteger(0)
            services.forEach { s, webSocketSession -> webSocketSession.sendMessage(TextMessage(cloneTask(task, s))) }
        }

        private fun gitClone(remote: String, pattern: String): String {
            val git = Git.cloneRepository()
            val dir = File(pattern).absoluteFile
            dir.mkdir()
            try {
                git.setDirectory(dir)
                        .setURI(remote)
                        .setNoCheckout(false)
                        .call()
                        .close()
            } catch(e: Throwable) {
                e.printStackTrace()
            }
            return dir.absolutePath
        }

        private fun cloneTask(task: Task, session: String): String {
            val name = "$session-${task.id}"
            val model = gitClone(task.modelGit, "model-$name")
            val students = task.studentGits.mapIndexed { i, s -> gitClone(s, "s$i-$name") }

            return Task(task.id, task.type, model, students.toTypedArray()).toString()
        }
    }
}

