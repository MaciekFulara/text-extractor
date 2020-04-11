package pl.sparkbit

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TextExtractorApplication

fun main(args: Array<String>) {
	runApplication<TextExtractorApplication>(*args)
}
