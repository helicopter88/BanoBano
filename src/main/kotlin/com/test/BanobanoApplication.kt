package com.test

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.json.JsonParser
import org.springframework.boot.json.JsonParserFactory
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@SpringBootApplication
@EnableWebMvc
open class BanoBanoApplication {
    companion object {
        val Mapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule())
        val JsonParser: JsonParser = JsonParserFactory.getJsonParser()
    }

}

fun main(args: Array<String>) {
    SpringApplication.run(BanoBanoApplication::class.java, *args)
}
