package days.day5

import days.Day
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.sql.Timestamp
import java.time.Instant
import java.util.*
import java.util.concurrent.atomic.AtomicLong

class Day5 : Day(false) {
    override fun partOne(): Any {
        val inputSeeds = readInput().first().substringAfter(": ").split(" ").map { it.toLong() }
        val input: LinkedList<List<Pair<LongRange, Long>>> = LinkedList()

        val rawInput = readInput().drop(2)


        var map = mutableListOf<Pair<LongRange, Long>>()
        for (line in rawInput) {
            if (line.contains("^[0-9\\s]*\$".toRegex()) && !line.isBlank()) {
                val numbers = line.split(" ").map { it.toLong() }

                val range = LongRange(numbers[1], numbers[1] + numbers[2] - 1)
                val pair = Pair(range, numbers[0])
                map.add(pair)
            }
            if (line.contains("map")) {
                input.add(map)
                map = mutableListOf()
            }
        }
        input.add(map)
        input.removeAt(0)

        val results = mutableListOf<Long>()

        for (seed in inputSeeds) {
            var currentValue: Long = seed
            for (map in input) {
                for (pair in map) {
                    if (pair.first.contains(currentValue)) {
                        currentValue = pair.second + currentValue - pair.first.first
                        break
                    }
                }
            }
            results.add(currentValue)
        }

        return results.min()
    }

    override fun partTwo(): Any {
        val time = Timestamp.from(Instant.now()).time

        val inputSeeds = readInput().first().substringAfter(": ").split(" ").map { it.toLong() }
        val realInputSeeds = mutableListOf<LongRange>()

        for (i in inputSeeds.indices step 2) {
            val range = LongRange(inputSeeds[i], inputSeeds[i] + inputSeeds[i + 1] - 1)
            realInputSeeds.add(range)
        }

        val input: LinkedList<List<Pair<LongRange, Long>>> = LinkedList()

        val rawInput = readInput().drop(2)

        var map = mutableListOf<Pair<LongRange, Long>>()
        for (line in rawInput) {
            if (line.contains("^[0-9\\s]*$".toRegex()) && !line.isBlank()) {
                val numbers = line.split(" ").map { it.toLong() }

                val range = LongRange(numbers[1], numbers[1] + numbers[2] - 1)
                val pair = Pair(range, numbers[0])
                map.add(pair)
            }
            if (line.contains("map")) {
                input.add(map)
                map = mutableListOf()
            }
        }
        input.add(map)
        input.removeAt(0)

        val atomicResult = AtomicLong(Long.MAX_VALUE)


        runBlocking {
            realInputSeeds.forEach { seedRange ->
                async(Dispatchers.IO) {
                    seedRange.forEach { seed ->
                        var currentValue = seed
                        for (map in input) {
                            for ((first, second) in map) {
                                if (first.contains(currentValue)) {
                                    currentValue = second + currentValue - first.first
                                    break
                                }
                            }
                        }
                        if (currentValue < atomicResult.get()) {
                            println("New min: $currentValue")
                            atomicResult.set(currentValue)
                        }
                    }
                }
            }
        }
        println("Time: ${Timestamp.from(Instant.now()).time-time}")

        return atomicResult.get()
    }
}