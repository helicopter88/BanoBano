package com.test

import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

/**
 * Created by nospa on 15/01/2017.
 */


@RestController
class BanoController {
    private final val counter = AtomicLong()

    @RequestMapping("/start_job")
    fun startJob(@Param("type") type: String, @Param("model_git") modelGit: String, @Param("student_gits") studentGits: Array<String>): Long {
        val t = Task(counter.incrementAndGet(), type, modelGit, studentGits)
        BackendHandler.submitTask(t)
        return counter.get()
    }
}

