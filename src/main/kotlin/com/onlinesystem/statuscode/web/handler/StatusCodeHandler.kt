package com.onlinesystem.statuscode.web.handler

import com.onlinesystem.statuscode.util.Constants.CREATED_CODE
import com.onlinesystem.statuscode.util.Constants.OK
import com.onlinesystem.statuscode.util.Constants.OK_CODE
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class StatusCodeHandler {


    fun getAllStatusCode(serverRequest: ServerRequest): Mono<ServerResponse>
    {
        return when (val statusCode = serverRequest.pathVariable("statusCode")){
            OK, OK_CODE -> ServerResponse.ok().bodyValue("Status: Success, code: $OK_CODE for URI: ${serverRequest.uri()}")
            CREATED_CODE ->ServerResponse.status(HttpStatus.CREATED).bodyValue("Status: Created, code: $CREATED_CODE for URI: ${serverRequest.uri()}")
            else -> ServerResponse.badRequest().bodyValue("Invalid code: $statusCode for URI : ${serverRequest.uri()}")
        }
    }

    fun getOkStatus(serverRequest: ServerRequest): Mono<ServerResponse>
    {
        return ServerResponse.ok().build()
    }

    fun getResourceNotFound(serverRequest: ServerRequest) : Mono<ServerResponse>
    {
        return  ServerResponse.notFound().build()
    }

    fun getInternalServerError(serverRequest: ServerRequest): Mono<ServerResponse>
    {
        return ServerResponse.status(500).build()
    }

    fun getCustomResponse(serverRequest: ServerRequest): Mono<ServerResponse>
    {
        val statusCode = serverRequest.queryParam("code").get().toInt()
        val message = serverRequest.queryParam("message").get()
        return ServerResponse.status(statusCode).bodyValue(message)
    }
}