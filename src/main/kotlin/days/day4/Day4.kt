package days.day4

import days.Day
import kotlin.math.pow

class Day4: Day(false) {
    override fun partOne(): Any {
        //Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        val winningNumbers: List<List<Int>> = readInput().map{
            it.substringAfter(":").substringBefore("|").trim().split(" ").filter { it.isNotBlank() }
                .map{it.toInt()}
        }

        val myValues = readInput().map{ it ->
            it.substringAfter("| ")
                .trim()
                .split(" ")
                .filter { it.isNotBlank() }
                .map{it.toInt()}
        }

        var result = 0

        for (i in winningNumbers.indices) {
            val winningNumber = winningNumbers[i]
            val myValue = myValues[i]
            var count = 0
            for (number in myValue) {
                if (number in winningNumber) {
                    count++
                }
            }
            result += 2.0.pow((count - 1).toDouble()).toInt()

        }

        return result
    }

    override fun partTwo(): Any {
        val winningNumbers: List<List<Int>> = readInput().map{
            it.substringAfter(":").substringBefore("|").trim().split(" ").filter { it.isNotBlank() }
                .map{it.toInt()}
        }

        val myValues = readInput().map{ it ->
            it.substringAfter("| ").trim().split(" ").filter { it.isNotBlank() }.map{it.toInt()}
        }


        val instances = IntArray(winningNumbers.size){1}

        for (i in winningNumbers.indices) {
            val winningNumber = winningNumbers[i]
            val myValue = myValues[i]
            var count = 0
            for (number in myValue) {
                if (number in winningNumber) {
                    count++
                }
            }
            for (j in 0 ..< count) {
                try {
                    instances[j+i+1] += instances[i]
                }catch (e: Exception) {
                    println("i: $i, j: $j")
                }
            }
        }

        return instances.sum()
    }
}

