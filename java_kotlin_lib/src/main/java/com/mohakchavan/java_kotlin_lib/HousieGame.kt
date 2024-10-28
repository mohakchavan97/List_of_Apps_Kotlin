package com.mohakchavan.java_kotlin_lib

import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Scanner
import java.util.UUID
import kotlin.system.exitProcess

private val cardsMap = mutableMapOf<String, Array<Array<String>>>()
private val trackedNumbers = mutableSetOf<String>()

fun main() {


    try {
        readCards()
        println(cardsMap)
        if (cardsMap.isNotEmpty()) {
            println()
            chooseTrackingMethod()
        } else {
            println("No data provided in Cards.csv file. Enter data into it and rerun the program.")

        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

private fun readCards() {
    val file = File("./Cards.csv")
    if (file.exists()) {
        val bfr = BufferedReader(FileReader(file))
        var str: String? = ""
        val list = arrayListOf<Array<String>>()
        var name: String = ""
        str = bfr.readLine()
        while (str != null) {
//            println(str)
            if (str.isEmpty()) {
                if (name.isNotEmpty() && list.isNotEmpty()) {
                    val arr = Array<Array<String>>(list.size) { arrayOf() }
                    list.forEachIndexed { index, ints ->
                        arr[index] = ints
                    }
                    cardsMap[name] = arr
                }
                list.clear()
            } else if (!str.contains(",")) {
                name = str
            } else {
                list.add(str.split(",").toTypedArray())
            }
            str = bfr.readLine()
        }
        bfr.close()
    } else {
        file.createNewFile()
        file.writeText("\n\nOnly enter data above this line\n=========================")
        throw FileNotFoundException("Cards.csv file not found. New file with same name has been created. Enter data into it and rerun the program.")
    }
}

private fun chooseTrackingMethod() {
    File("./TrackFiles").run {
        if (!exists()) {
            mkdirs()
        }
    }
    println(
        "\nEnter\n" +
                "1 for using existing tracking file in \"TrackFiles\" folder\n" +
                "2 for using a new tracking file:"
    )
    Scanner(System.`in`).let { scan ->
        scan.nextLine()?.let { choice ->
            when (choice) {
                "1" -> {
                    println("Enter tracking file name in double quotes(\"\"):")
                    scan.nextLine()?.let { fileName ->
                        startTracking(fileName)
                    } ?: run {
                        println("Enter valid tracking file name.")
                        chooseTrackingMethod()
                    }
                }

                "2" -> {
                    val fileName = SimpleDateFormat("dd_MM_yyyy_HH_mm_ss_SSS", Locale.ENGLISH).format(Date()).let {
                        "TrackedNumbers_${it.ifEmpty { UUID.randomUUID() } ?: UUID.randomUUID()}.csv"
                    }
                    startTracking(fileName)
                }

                else -> {
                    println("Enter valid choice.")
                    chooseTrackingMethod()
                }
            }
        } ?: run {
            println("Enter valid choice.")
            chooseTrackingMethod()
        }
    }
}

fun startTracking(fileName: String) {

    val trackFile = File("./TrackFiles", fileName)
    if (!trackFile.exists()) {
        trackFile.createNewFile()
        println("Track file created with name as \"$fileName\"")
    }
    println("Using track file: \"$fileName\"")
    println("Start Entering Tracking Numbers:")
    val scanner = Scanner(System.`in`)
    val writer = trackFile.bufferedWriter()
    while (true) {
        val number = scanner.nextLine()
        number.ifEmpty { null }?.let {
            if (number != "-1") {
                if (trackedNumbers.add(number)) {
                    writer.run {
                        appendLine(number)
                        flush()
                    }
                } else {
                    println("$number is already added in tracked numbers.".uppercase())
                }
            } else {
                writer.run {
                    flush()
                    close()
                }
                exitProcess(0)
            }
        }
    }
}


