package days.day11

import days.Day
import kotlin.math.abs

class Day11 : Day() {
    override fun partOne(): Any {


        val input = readInput()

        val galaxy = readInput().toMutableList()

        var countInsertions = 0
        for (i in input.indices) {
            if (input[i].none { it == '#' }) {
                galaxy.add(i + countInsertions, input[i])
                countInsertions++
            }
        }
        countInsertions = 0
        for (i in input[0].indices) {
            //get column
            val column = input.map { it[i] }
            if (column.none { it == '#' }) {
                for (index in galaxy.indices) {
                    val res = galaxy[index].substring(
                        0,
                        i + countInsertions
                    ) + "." + galaxy[index].substring(i + countInsertions)
                    galaxy[index] = res
                }
                countInsertions++
            }
        }

        val starCoordinates = mutableListOf<Pair<Int, Int>>()
        for (i in galaxy.indices) {
            for (j in galaxy[i].indices) {
                if (galaxy[i][j] == '#') {
                    starCoordinates.add(Pair(i, j))
                }
            }
        }

        //make pairs of coordinates
        val pairs = mutableListOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
        for (i in starCoordinates.indices) {
            for (j in starCoordinates.indices) {
                if (i != j && i < j) {

                    pairs.add(Pair(starCoordinates[i], starCoordinates[j]))
                }
            }
        }

        var totalDistance = 0

        for (pair in pairs) {
            val distance = abs(pair.first.first - pair.second.first) + abs(pair.first.second - pair.second.second)
            println(pair to distance)
            totalDistance += distance
        }




        return totalDistance
    }

    override fun partTwo(): Any {
        val increase = 999999

        val input = readInput()

        val galaxy = readInput().toMutableList()

        val starCoordinates = mutableListOf<Pair<Int, Int>>()
        for (i in galaxy.indices) {
            for (j in galaxy[i].indices) {
                if (galaxy[i][j] == '#') {
                    starCoordinates.add(Pair(i, j))
                }
            }
        }

        val strechedStarCoordinates = starCoordinates.toMutableList()

        var countInsertions = 0
        for (i in input.indices) {
            if (input[i].none { it == '#' }) {
                for (star in strechedStarCoordinates) {
                    if (star.first >= i + countInsertions * increase +1) {
                        strechedStarCoordinates[strechedStarCoordinates.indexOf(star)] = Pair(star.first + increase, star.second)
                    }
                }
                countInsertions++
            }
        }
        countInsertions = 0
        for (i in input[0].indices) {
            //get column
            val column = input.map { it[i] }
            if (column.none { it == '#' }) {
                for (star in strechedStarCoordinates) {
                    if (star.second >= i + countInsertions * increase +1) {
                        strechedStarCoordinates[strechedStarCoordinates.indexOf(star)] =
                            Pair(star.first, star.second + increase)
                    }
                }
                countInsertions++
            }
        }

        //make pairs of coordinates
        val pairs = mutableListOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
        for (i in starCoordinates.indices) {
            for (j in starCoordinates.indices) {
                if (i != j && i < j) {
                    pairs.add(Pair(strechedStarCoordinates[i], strechedStarCoordinates[j]))
                }
            }
        }

        var totalDistance = 0

        for (pair in pairs) {
            val distance = abs(pair.first.first - pair.second.first) + abs(pair.first.second - pair.second.second)
            println(pair to distance)
            totalDistance += distance
        }




        return totalDistance
    }
}

