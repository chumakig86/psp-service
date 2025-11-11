package org.example.pspservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PspServiceApplication

fun main(args: Array<String>) {
    runApplication<PspServiceApplication>(*args)
}
