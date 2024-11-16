package com.onlinesystem.statuscode.web.routes

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class HttpRouter(
    private val statusRouter: StatusRouter
) {
    @Bean
    fun route(): RouterFunction<ServerResponse> =
        router {
                accept(MediaType.APPLICATION_JSON).nest {
                    add(statusRouter.router())
                }
        }

}