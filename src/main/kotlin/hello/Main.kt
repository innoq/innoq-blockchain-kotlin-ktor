package hello

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.tomcat.Tomcat

fun main(args: Array<String>) {
    val server = embeddedServer(Tomcat, port = 8080) {
        routing {
            get("/") {
                call.respondText(
					"""{
                			"nodeId": "bcfeb8c5-c9a6-4731-9a17-e0fedd7aa073",
                			"currentBlockHeight": 69
                	   }""",
					ContentType.Application.Json)
            }
            get("/blocks") {
                call.respondText(
					"""{
                			"blocks": [
	                			 {
	                				"index": 1,
	                				"timestamp": 955977,
	                				"proof": 0,
	                				"transactions": [{
	                					"id": "b3c973e2-db05-4eb5-9668-3e81c7389a6d",
	                					"timestamp": 0,
	                					"payload": "I am Heribert Innoq"
	                				}],
	                				"previousBlockHash": "0"
	                			},
	                			{
	                				"index": 2,
	                				"timestamp": 1524086823469,
	                				"proof": 3288718,
	                				"transactions": [],
	                				"previousBlockHash": "0000008793d0a9aa..."
	                			}
	                		],
                			"blockHeight": 2
            			}
                	""", ContentType.Application.Json)
            }
			get("/mine") {
                call.respondText("""
                	{
                		"message": "Mined a new block in 11.214s. Hashing power: 58854 hashes/s.",
                		"block": {
                			"index": 5,
                			"timestamp": 1524087328713,
                			"proof": 659987,
                			"transactions": [],
                			"previousBlockHash": "000000555398faa74ff..."
                		}
                	}
                	""", ContentType.Application.Json)
            }
        }
    }
    server.start(wait = true)
}
