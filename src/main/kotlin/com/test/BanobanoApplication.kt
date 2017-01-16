package com.test

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@SpringBootApplication
@EnableWebMvc
open class BanobanoApplication {
    companion object {
        public val mapper = ObjectMapper().registerModule(KotlinModule())
    }

}

fun main(args: Array<String>) {
    SpringApplication.run(BanobanoApplication::class.java, *args)
}
