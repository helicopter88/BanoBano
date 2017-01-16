package com.test

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor


/**
 * Created by nospa on 15/01/2017.
 */
@Configuration
@EnableWebSocket
open class WebSocketConfig : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(backendHandler(), "/topic/services").setAllowedOrigins("*").addInterceptors(HttpSessionHandshakeInterceptor())
    }

    @Bean
    open fun backendHandler() = BackendHandler()
}