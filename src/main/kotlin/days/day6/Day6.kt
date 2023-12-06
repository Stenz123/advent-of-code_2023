package days.day6

import days.Day

class Day6 : Day(false) {
    override fun partOne(): Any {
        val times = readInput()[0].substringAfter(":").trim().split(" ").filter { it.isNotBlank() }.map { it.toInt() }
        val distances = readInput()[1].substringAfter(":").trim().split(" ").filter { it.isNotBlank() }.map { it.toInt() }

        var result = 1

        for (i in times.indices) {

            var numberOfSolutions = 0
            for (holddownTime in 1..<distances[i]) {
                val timeLeft = times[i] - holddownTime

                val distance = holddownTime * timeLeft
                if (distance > distances[i]) {
                    numberOfSolutions++
                }
            }
            result *= numberOfSolutions
        }

        return result
    }

    override fun partTwo(): Any {
        val times = readInput()[0].substringAfter(":").replace(" ", "").toLong()
        val distances = readInput()[1].substringAfter(":").replace(" ", "").toLong()

        var result: Long = 0


        for (holddownTime in 1..<times) {
            val timeLeft = times - holddownTime
            val distance = holddownTime * timeLeft
            if (distance > distances) {
                result++
            }
        }

        return result

    }
}

