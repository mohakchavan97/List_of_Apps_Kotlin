package com.mohakchavan.java_kotlin_lib

import java.io.File
import java.util.*

fun main() {

    val scanner=Scanner(File("E:\\Downloads\\Automata Transactions.csv"))
    scanner.useDelimiter("\n")
    if (scanner.hasNextLine()) {
        scanner.nextLine()
        while (scanner.hasNext()){
            println("https://grubbrr.atlassian.net/browse/"+scanner.next().split(",")[0])
        }
    }
    scanner.close()
}