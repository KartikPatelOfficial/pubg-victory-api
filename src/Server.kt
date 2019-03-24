package com.deucate

import io.ktor.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

private const val portArgsName = "--server.port"
private const val defaultPort = 8080

fun main(args: Array<String>) {
    val port = if (args.isNotEmpty() && args[0].startsWith(portArgsName)) {
        args[0].split("=").last().trim().toInt()
    } else defaultPort

    embeddedServer(Netty, port, module = Application::main).start(wait = true)
}
