package common

import java.io.File

fun File.readParagraphs() = this.readText().trimEnd().split("\n\n", "\r\n\r\n")
