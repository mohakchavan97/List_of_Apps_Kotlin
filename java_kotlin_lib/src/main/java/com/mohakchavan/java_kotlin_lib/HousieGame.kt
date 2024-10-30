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
private val trackingCardsFile = File("./TrackedCards.csv")
private val rulesFile = File("./Rules.csv")
private val rulesList = mutableListOf<Triple<Int, String, String>>()

fun main() {


    try {
        readCards()
        println(cardsMap)
        readRules()
        if (cardsMap.isNotEmpty() && rulesList.isNotEmpty()) {
            println()
            writeTrackedCards(true)
            println()
            chooseTrackingMethod()
        } else {
            println("No data provided in Cards.csv or Rules.csv file. Enter data into it and rerun the program.")

        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

fun readRules() {
    if (rulesFile.exists()) {
        rulesList.clear()
        rulesFile.bufferedReader().let { bfr ->
            var str: String = ""
            while (bfr.readLine()?.also { line -> str = line } != null) {
                val list = mutableListOf<String?>()
                if (str.isNotEmpty() && str.contains(",") && str.split(",").also { l -> list.addAll(l) }.size == 3) {
                    if (!list[2].isNullOrEmpty() && !list[0].isNullOrEmpty()) {
                        rulesList.add(Triple((list[0] ?: "0").toInt(), list[1] ?: "", list[2] ?: ""))
                    }
                }
            }
        }
    } else {
        rulesFile.createNewFile()
        val rulesText = "" +
                "1,First Line,true" +
                "\n" +
                "2,Second Line,true" +
                "\n" +
                "3,Third Line,true" +
                "\n" +
                "4,Full House,true"
        rulesFile.writeText(rulesText)
        throw FileNotFoundException("Rules.csv file not found. New file with same name has been created. Enter data into it and rerun the program.")
    }
}

fun writeRules() {
    if (rulesList.isNotEmpty()) {
        if (rulesFile.exists()) {
            rulesFile.delete()
        }
        rulesFile.createNewFile()
        rulesFile.bufferedWriter().let { bfw ->
            rulesList.forEach { triple ->
                bfw.appendLine("${triple.first},${triple.second},${triple.third}")
            }
            bfw.flush()
            bfw.close()
        }
    }
}

private fun readCards() {
    val file = File("./Cards.csv")
    if (file.exists()) {
        val bfr = BufferedReader(FileReader(file))
        var str: String = ""
        val list = arrayListOf<Array<String>>()
        var name: String = ""
//        str = bfr.readLine()
        while (bfr.readLine()?.also { line -> str = line } != null) {
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
//            str = bfr.readLine()
        }
        bfr.close()
    } else {
        file.createNewFile()
        file.writeText("\n\nOnly enter data above this line\n=========================")
        throw FileNotFoundException("Cards.csv file not found. New file with same name has been created. Enter data into it and rerun the program.")
    }
}

private fun writeTrackedCards(printLog: Boolean = false) {
    if (trackingCardsFile.exists()) {
        if (printLog) {
            println("Clearing Tracking Cards File...")
        }
        trackingCardsFile.delete()
    }
    if (printLog) {
        println("Creating new Tracking Cards File...")
    }
    trackingCardsFile.createNewFile()
    val bfr = trackingCardsFile.bufferedWriter()
    cardsMap.forEach { entry ->
        bfr.appendLine(entry.key)

        entry.value.forEach { row ->
            row.forEachIndexed { index, col ->
                bfr.append(
                    col, if (index < row.size - 1) {
                        ","
                    } else {
                        ""
                    }
                )
            }
            bfr.appendLine()
        }
        bfr.appendLine()
        bfr.flush()
    }
    bfr.appendLine("=========================")
    bfr.flush()
    bfr.close()
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
    println("Start Entering Tracking Numbers (Enter \"-1\" to exit program) :")
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
                    trackNumberInCards(number)
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

fun trackNumberInCards(number: String) {
    var isMapChanged = false
    cardsMap.forEach { entry ->
        val value = entry.value
        var isChangedValue = false
        value.forEachIndexed { indexRow, orgRow ->
            val row = orgRow.copyOf()
            var isChanged = false
            orgRow.forEachIndexed { indexCol, col ->
                if (col == number) {
                    row[indexCol] = "-1"
                    isChanged = true
                }
            }
            if (isChanged) {
                value[indexRow] = row
                isChangedValue = true
            }
        }
        if (isChangedValue) {
            cardsMap[entry.key] = value
            isMapChanged = true
        }
    }
    if (isMapChanged) {
        writeTrackedCards()
        verifyRules()
    }
}

fun verifyRules() {
    readRules()
    val successList = mutableListOf<String>()
    val listToRemove = mutableSetOf<Int>()
    val intList = rulesList.filter { triple -> triple.third.takeIf { thr -> thr.trim().equals("true", true) } != null }.map { trp -> trp.first }
    cardsMap.forEach { entry ->
        val value = entry.value

        if (1 in intList) {
            if (value.isNotEmpty() && value[0].all { num ->
                    num.isEmpty() || num == "-1"
                }) {
                successList.add("${entry.key} satisfies Rule 1")
                listToRemove.add(1)
            }
        }

        if (2 in intList) {
            if (value.size >= 2 && value[1].all { num ->
                    num.isEmpty() || num == "-1"
                }) {
                successList.add("${entry.key} satisfies Rule 2")
                listToRemove.add(2)
            }
        }

        if (3 in intList) {
            if (value.size >= 3 && value[2].all { num ->
                    num.isEmpty() || num == "-1"
                }) {
                successList.add("${entry.key} satisfies Rule 3")
                listToRemove.add(3)
            }
        }

        if (4 in intList) {
            if (value.all { row ->
                    row.all { col ->
                        col.isEmpty() || col == "-1"
                    }
                }) {
                successList.add("${entry.key} satisfies Rule 4")
                listToRemove.add(4)
            }
        }
    }
    successList.takeIf { it.isNotEmpty() }?.forEach { suc ->
        println(suc)
    }
    var isRulesChanged = false
    rulesList.forEachIndexed { index, triple ->
        if (triple.first in listToRemove) {
            rulesList[index] = triple.copy(third = "false")
            isRulesChanged = true
        }
    }
    if (isRulesChanged) {
        writeRules()
    }
}




