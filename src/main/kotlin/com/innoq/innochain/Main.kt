package com.innoq.innochain

import com.innoq.innochain.dto.TransactionRequest
import com.innoq.innochain.model.BlockChain
import com.innoq.innochain.model.Transaction
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.tomcat.Tomcat
import java.time.Instant
import java.util.UUID
import com.innoq.innochain.dto.TransactionResponse

fun main(args: Array<String>) {
    embeddedServer(Tomcat, port = 8080, module = Application::main).start(wait = true)
}


// Modules
fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging)
    install(StatusPages){
        exception<Throwable> { cause ->
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    routing {
        root()
    }
}

// Routing
fun Routing.root() {
    get("/") {
        call.respondText(
                """|{
                		|  "nodeId": "${BlockChain.id}",
                		|  "currentBlockHeight": ${BlockChain.blocks.size}
                   |}""".trimMargin(),
                ContentType.Application.Json)
    }
    get("/blocks") {
        call.respondText(
                """|{
                	    |  "blocks": ${BlockChain.blocks},
                		|  "blockHeight": ${BlockChain.blocks.size}
                   |}""".trimMargin(), ContentType.Application.Json)
    }
    get("/mine") {
        val block = BlockChain.mine(emptyList())
        call.respondText(block.toString(), ContentType.Application.Json)
    }
	post("/transactions") {
		var tr = call.receive<TransactionRequest>()
		val transaction = Transaction(UUID.randomUUID(), Instant.now().epochSecond, tr.payload)
		BlockChain.mine(listOf(transaction))
		call.respondText(TransactionResponse(transaction.id, transaction.timestamp, transaction.payload, true).toString(), ContentType.Application.Json)
	}
    get("/health") {
        call.respondText("OK")
    }
}
