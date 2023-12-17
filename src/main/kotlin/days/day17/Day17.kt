package days.day17

import days.Day
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.abs

class Day17 : Day(true) {
    private val heatMap = parseInput(readInput())
    override fun partOne(): Any {
        val size = readInput().size - 1
        val result = findShortestPath(Coordinate(0, 0), Coordinate(size, size))!!
        printResult(result)

        return result.sumOf { it.getWeight() }
    }

    override fun partTwo(): Any {
        return "day 17 part 2 not Implemented"
    }

    fun parseInput(input: List<String>): Map<Coordinate, Int> {
        val mirrorMap = mutableMapOf<Coordinate, Int>()
        for (i in input.indices) {
            for (j in input[i].indices) {
                mirrorMap[Coordinate(j, i)] = input[i][j].digitToInt()
            }
        }
        return mirrorMap
    }

    fun findShortestPath(start: Coordinate, target: Coordinate): List<Coordinate>? {
        val priorityQueue = PriorityQueue<Pair<Coordinate, Int>>(compareBy { it.second })
        val distance = HashMap<Coordinate, Int>()
        val previous = HashMap<Coordinate, Coordinate>()

        priorityQueue.add(start to 0)
        distance[start] = 0

        while (priorityQueue.isNotEmpty()) {
            val (current, currentDistance) = priorityQueue.poll()

            if (current == target) {
                return getPath(target, previous).reversed()
            }

            val path = getPath(current, previous)
            val neighbours: List<Coordinate> = path.getNextFields()
                .filter { heatMap.containsKey(it) }
                .filter { !path.contains(it) }
            for (neighbor in neighbours) {
                val newDistance = currentDistance + neighbor.getWeight()

                val currentValues = distance.getOrDefault(neighbor, Int.MAX_VALUE)
                if (newDistance == currentValues) {
                    val lenghtSinceBiegung = getPath(current, previous).numberOfCoordinatesInOneRow()
                    val otherLenghtSinceBiegung = getPath(neighbor, previous).dropLast(1).numberOfCoordinatesInOneRow()
                    if (lenghtSinceBiegung > otherLenghtSinceBiegung) {
                        previous[neighbor] = current
                        priorityQueue.add(neighbor to newDistance)
                    }
                }

                if (newDistance < currentValues) {
                    distance[neighbor] = newDistance
                    previous[neighbor] = current
                    priorityQueue.add(neighbor to newDistance)
                }
            }
        }

        return null // No path found
    }

    fun getPath(curret: Coordinate, previous: Map<Coordinate, Coordinate>): List<Coordinate> {
        var node = curret
        val path = mutableListOf<Coordinate>()
        while (node != Coordinate(0, 0)) {
            path.add(node)
            node = previous[node]!!
        }
        path.add(Coordinate(0, 0))
        return path.reversed()
    }

    private fun Coordinate.getWeight() = heatMap[this]!!

    fun printResult(result: List<Coordinate>) {
        for (y in 0 until readInput().size) {
            for (x in 0 until readInput().size) {
                if (result.contains(Coordinate(x, y))) {
                    print("\u001B[35m${heatMap[Coordinate(x, y)]}\u001B[0m")
                } else {
                    print(heatMap[Coordinate(x, y)])
                }
            }
            println()
        }
    }
}

fun List<Coordinate>.areLastFourInARow(): Boolean {
    return numberOfCoordinatesInOneRow() == 4
}

fun List<Coordinate>.numberOfCoordinatesInOneRow(): Int {
    if (this.size < 2) return this.size

    if (this.last().y == this[size - 2].y) {
        val lastY = this.last().y
        return this.reversed().takeWhile { it.y == lastY }.count()
    } else {
        val lastX = this.last().x
        return this.reversed().takeWhile { it.x == lastX }.count()
    }
}

fun List<Coordinate>.getNextFields(): List<Coordinate> {
    if (this.areLastFourInARow()) {
        val coordinatesToAvoid = mutableListOf<Coordinate>()
        if (this.last().x == this[this.size - 2].x) {
            coordinatesToAvoid.add(Coordinate(this.last().x, this.last().y - 1))
            coordinatesToAvoid.add(Coordinate(this.last().x, this.last().y + 1))
        } else {
            coordinatesToAvoid.add(Coordinate(this.last().x + 1, this.last().y))
            coordinatesToAvoid.add(Coordinate(this.last().x - 1, this.last().y))
        }

        return this.last().getNeighbours().filter { coordinate ->
            coordinate != this.last() && !coordinatesToAvoid.contains(coordinate)
        }
    }

    return this.last().getNeighbours().filter { !this.contains(it) }
}

class Coordinate(val x: Int, val y: Int) {
    fun getNeighbours(): List<Coordinate> {
        return listOf(
            Coordinate(x - 1, y),
            Coordinate(x, y - 1),
            Coordinate(x, y + 1),
            Coordinate(x + 1, y),
        )
    }


    override fun toString(): String {
        return "($x, $y)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Coordinate

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}
