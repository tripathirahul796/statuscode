package com.onlinesystem.statuscode.web.handler

import com.onlinesystem.statuscode.exceptions.InvalidRequestException
import com.onlinesystem.statuscode.util.Constants.CREATED_CODE
import com.onlinesystem.statuscode.util.Constants.OK
import com.onlinesystem.statuscode.util.Constants.OK_CODE
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import kotlin.jvm.optionals.getOrDefault

@Component
class StatusCodeHandler {


    fun getAllStatusCode(serverRequest: ServerRequest): Mono<ServerResponse> {
        return when (val statusCode = serverRequest.pathVariable("statusCode")) {
            OK, OK_CODE -> ServerResponse.ok()
                .bodyValue("Status: Success, code: $OK_CODE for URI: ${serverRequest.uri()}")

            CREATED_CODE -> ServerResponse.status(HttpStatus.CREATED)
                .bodyValue("Status: Created, code: $CREATED_CODE for URI: ${serverRequest.uri()}")

            else -> ServerResponse.badRequest().bodyValue("Invalid code: $statusCode for URI : ${serverRequest.uri()}")
        }
    }

    fun getOkStatus(serverRequest: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().build()
    }

    fun getResourceNotFound(serverRequest: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.notFound().build()
    }

    fun getInternalServerError(serverRequest: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.status(500).build()
    }

    fun getCustomResponse(serverRequest: ServerRequest): Mono<ServerResponse> {

        try {
            val statusCode = serverRequest
                .queryParam("code")
                .orElseThrow { InvalidRequestException("Query parameter 'code' is missing") }
                .toIntOrNull() ?: throw IllegalArgumentException("Invalid 'code' parameter, must be and integer")
            val status = serverRequest.queryParam("status").getOrDefault("Custom")
            val message = serverRequest
                .queryParam("message")
                .orElseThrow { InvalidRequestException("Query parameter 'message' is missing") }

            val statusMessage = HttpStatus.resolve(statusCode)?.reasonPhrase ?: status
            val responseBody = mapOf("statusCode" to statusCode,
                "statusMessage" to statusMessage,
                "message" to message)
            return ServerResponse.status(statusCode).bodyValue(responseBody)

        } catch (ex: InvalidRequestException) {
            return ServerResponse.badRequest().bodyValue(mapOf("error" to ex.message))
        } catch (ex: Exception) {
            return ServerResponse.status(500).bodyValue(mapOf("error" to ex.message))
        }

    }
}