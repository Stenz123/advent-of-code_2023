package days.day5

import days.Day
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.sql.Timestamp
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*

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

// ... Your existing code ...

    override fun partTwo(): Any {
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

        val results = runBlocking {
            realInputSeeds.map { seedRange ->
                async(Dispatchers.IO) {
                    seedRange.map { seed ->
                        var currentValue: Long = seed
                        for (map in input) {
                            for ((first, second) in map) {
                                if (first.contains(currentValue)) {
                                    currentValue = second + currentValue - first.first
                                    break
                                }
                            }
                        }
                        currentValue
                    }.also {
                        print("Finished seed ${seedRange.first} - ${seedRange.last} at")
                        println(DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
                        println("Results: ${it.min()}")
                    }
                }
            }.awaitAll().flatten().minOrNull()
        }


        return results!!
    }

    fun parseRangeToRanges(range1: LongRange, ranges: List<Pair<LongRange, Long>>): MutableList<LongRange> {
        val resultRanges = mutableListOf<LongRange>()

        for (range in ranges) {
            val rangeOberlap = rangeOverlap(range1, range)
            if (rangeOberlap != null) {
                resultRanges.add(rangeOberlap)
            }
        }

        return resultRanges
    }

    fun rangeOverlap(range1: LongRange, range2: Pair<LongRange, Long>): LongRange? {
        val start = Math.max(range1.first, range2.first.first)
        val end = Math.min(range1.last, range2.first.last)
        if (start <= end) {
            val lenf = end - start
            return range2.second..range2.second + lenf
        } else {
            return null
        }
    }

}