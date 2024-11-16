package com.onlinesystem.statuscode.web.routes

import com.onlinesystem.statuscode.web.handler.StatusCodeHandler
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.server.router

@RestController
class StatusRouter (
    private  val statusCodeHandler: StatusCodeHandler
){
    fun router() = router {
        "/status".nest{
            accept(MediaType.APPLICATION_JSON).nest {
                GET("/code/{statusCode}",statusCodeHandler::getAllStatusCode)
                GET("/200",statusCodeHandler::getOkStatus)
                GET("/404",statusCodeHandler::getResourceNotFound)
                GET("/500",statusCodeHandler::getInternalServerError)
                GET("/",statusCodeHandler::getCustomResponse)
            }

        }
    }
}