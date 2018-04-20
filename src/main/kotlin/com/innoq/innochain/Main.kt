package com.innoq.innochain

import com.innoq.innochain.dto.TransactionRequest
import com.innoq.innochain.dto.TransactionResponse
import com.innoq.innochain.model.BlockChain
import com.innoq.innochain.model.Transaction
import com.innoq.innochain.util.ByteArrayToHexAsciiTypeAdapter
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DataConversion
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.*
import io.ktor.request.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.tomcat.Tomcat
import io.ktor.util.DataConversionException
import java.text.DateFormat
import java.time.Instant
import java.util.UUID

fun main(args: Array<String>) {
	embeddedServer(Tomcat, port = 8080, module = Application::main).start(wait = true)
}


// Modules
fun Application.main() {
	install(DefaultHeaders)
	install(CallLogging)
	install(StatusPages) {
		exception<Throwable> { cause ->
			call.respond(HttpStatusCode.InternalServerError)
		}
	}
	install(ContentNegotiation) {
		gson {
			setDateFormat(DateFormat.LONG)
			setPrettyPrinting()
			registerTypeHierarchyAdapter(ByteArray::class.java, ByteArrayToHexAsciiTypeAdapter())
		}
	}

	routing {
		root()
        transactions()
	}
}

// Routing
fun Routing.root() {
	accept(ContentType.Application.Json) {
	
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
		get("/health") {
			call.respondText("OK")
		}
	}
}

fun Routing.transactions() {
    accept(ContentType.Application.Json) {
        route("/transactions") {
            post() {
                var tr = call.receive<TransactionRequest>()
                val transaction = Transaction(UUID.randomUUID(), Instant.now().epochSecond, tr.payload)
                BlockChain.mine(listOf(transaction))
                call.respond(TransactionResponse(transaction.id, transaction.timestamp, transaction.payload, true))
            }
            get("/{id}") {
                val transactionId = call.parameters["id"]!!.toUUID()
                val transaction = BlockChain.blocks.flatMap { it.transaction }.find { it.id == transactionId }
                if (transaction == null) {
                    call.respondText("""{"error": "No such transaction"}""", ContentType.Application.Json, HttpStatusCode.NotFound)
                } else {
                    call.respond(TransactionResponse(transaction.id, transaction.timestamp, transaction.payload, true))
                }

            }
        }
    }
}


fun String.toUUID(): UUID {
	return UUID.fromString(this)
}
