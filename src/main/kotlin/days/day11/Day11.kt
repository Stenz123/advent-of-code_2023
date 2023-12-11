package days.day11

import days.Day
import kotlin.math.abs

class Day11 : Day(false) {
    override fun partOne(): Any {
        return solve(1)
    }

    override fun partTwo(): Any {
        return solve(999_999)
    }

    private fun solve(increas: Int):Long {
        val input = readInput()
        val galaxy = readInput().toMutableList()

        val starCoordinates = mutableListOf<Pair<Long, Long>>()
        for (i in galaxy.indices) {
            for (j in galaxy[i].indices) {
                if (galaxy[i][j] == '#') {
                    starCoordinates.add(Pair(i.toLong(), j.toLong()))
                }
            }
        }

        val strechedStarCoordinates = starCoordinates.toMutableList()
        var countInsertions = 0
        for (i in input.indices) {
            if (input[i].none { it == '#' }) {
                for (star in strechedStarCoordinates) {
                    if (star.first >= i + countInsertions * increas) {
                        strechedStarCoordinates[strechedStarCoordinates.indexOf(star)] =
                            Pair(star.first + increas, star.second)
                    }
                }
                countInsertions++
            }
        }
        countInsertions = 0
        for (i in input[0].indices) {
            val column = input.map { it[i] }
            if (column.none { it == '#' }) {
                for (star in strechedStarCoordinates) {
                    if (star.second >= i + countInsertions * increas) {
                        strechedStarCoordinates[strechedStarCoordinates.indexOf(star)] =
                            Pair(star.first, star.second + increas)
                    }
                }
                countInsertions++
            }
        }

        //make pairs of coordinates
        val pairs = mutableListOf<Pair<Pair<Long, Long>, Pair<Long, Long>>>()
        for (i in starCoordinates.indices) {
            for (j in starCoordinates.indices) {
                if (i != j && i < j) {
                    pairs.add(Pair(strechedStarCoordinates[i], strechedStarCoordinates[j]))
                }
            }
        }

        var totalDistance:Long = 0
        for (pair in pairs) {
            val distance = abs(pair.first.first - pair.second.first) + abs(pair.first.second - pair.second.second)
            //println(pair to distance)
            totalDistance += distance
        }

        return totalDistance
    }
}

