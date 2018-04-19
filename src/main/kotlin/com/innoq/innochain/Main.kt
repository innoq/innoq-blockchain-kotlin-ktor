package com.innoq.innochain

import com.innoq.innochain.model.Block
import com.innoq.innochain.model.BlockChain
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.tomcat.Tomcat

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
        val block = BlockChain.mine()
        call.respondText(block.toString(), ContentType.Application.Json)
    }
    get("/health") {
        call.respondText("OK")
    }
}
