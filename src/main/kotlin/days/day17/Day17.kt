package days.day17

import days.Day
import java.util.*
import kotlin.collections.HashMap

class Day17 : Day(true) {
    private val heatMap = parseInput(readInput())
    override fun partOne(): Any {
        val size = readInput().size - 1
        val result = findShortestPath(Coordinate(0, 0), Coordinate(readInput().first().length - 1, size))!!
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

    fun findShortestPath(start: Coordinate, target: Coordinate, minBlocks: Int, maxBlocks: Int): List<Coordinate>? {
        val priorityQueue = PriorityQueue<Pair<Pair<Coordinate, Int>, Int>>(compareBy { it.second })
        val distance = HashMap<Pair<Coordinate, Int>, Int>()
        val previous = HashMap<Pair<Coordinate, Int>, Pair<Coordinate, Int>>()

        priorityQueue.add(start to 0 to 0)
        distance[start to 0] = 0

        while (priorityQueue.isNotEmpty()) {
            val (current, currentDistance) = priorityQueue.poll()
            val path = getPath(current, previous, start)

            if (current.first == Coordinate(3, 0)) {
                println()
            }
            val neighbours = path.getNextFields()
                .filter { heatMap.containsKey(it.first) }


            for (neighbor in neighbours) {
                val newDistance = currentDistance + neighbor.first.getWeight()

                val currentValues = distance.getOrDefault(neighbor, Int.MAX_VALUE)
                if (newDistance == currentValues) {
                    val lenghtSinceBiegung = current.second
                    val otherLenghtSinceBiegung = getPath(neighbor, previous, start).dropLast(1).last().second
                    if (lenghtSinceBiegung > otherLenghtSinceBiegung) {
                        distance[neighbor] = newDistance
                        previous[neighbor] = current

                        priorityQueue.add(neighbor to newDistance)
                    }
                }

                if (newDistance < currentValues ) {
                    distance[neighbor] = newDistance
                    previous[neighbor] = current
                    priorityQueue.add(neighbor to newDistance)
                }
            }

            if ( current.first == target) {
                println(distance[previous.keys.first { it.first == target }])
                val results = priorityQueue.filter { it.first.first == target }
                return getPath(previous.keys.first { it.first == target }, previous, start).map { it.first }
            }
        }

        return null // No path found
    }

    fun getPath(
        curret: Pair<Coordinate, Int>,
        previous: HashMap<Pair<Coordinate, Int>, Pair<Coordinate, Int>>,
        start: Coordinate
    ): List<Pair<Coordinate, Int>> {
        var node = curret
        val path = mutableListOf<Pair<Coordinate, Int>>()
        while (node.first != start) {
            path.add(node)
            if (previous[node]!!.second == 0 && previous[node]!!.second > node.second - 1) {
                println()
            }
            node = previous[node]!!
        }
        path.add(start to 1)
        return path.reversed()
    }

    private fun Coordinate.getWeight() = heatMap[this]!!

    fun printResult(result: List<Coordinate>) {
        for (y in 0 until readInput().size) {
            for (x in 0 until readInput().first().length) {
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


fun List<Pair<Coordinate, Int>>.getNextFields(): List<Pair<Coordinate, Int>> {
    if (this.size == 1) {
        return this.last().first.getNeighbours().map { it to 2 }
    }
    if (this.last().second == 3) {
        val result = mutableListOf<Pair<Coordinate, Int>>()
        if (this.last().first.x != this[this.size - 2].first.x) {
            result.add(Coordinate(this.last().first.x, this.last().first.y - 1) to 0)
            result.add(Coordinate(this.last().first.x, this.last().first.y + 1) to 0)
        } else {
            result.add(Coordinate(this.last().first.x + 1, this.last().first.y) to 0)
            result.add(Coordinate(this.last().first.x - 1, this.last().first.y) to 0)
        }
        return result.filter { !this.map { it.first }.contains(it.first) }
    }
    val result = mutableListOf<Pair<Coordinate, Int>>()

    var newXRowLength = if (this.last().first.x == this[this.size - 2].first.x) this.last().second + 1 else 0
    var newYRowLength = if (this.last().first.y == this[this.size - 2].first.y) this.last().second + 1 else 0

    if (this.last().second == 0) {
        newXRowLength = 1
        newYRowLength = 1
    }

    result.add(Coordinate(this.last().first.x, this.last().first.y - 1) to newXRowLength)
    result.add(Coordinate(this.last().first.x, this.last().first.y + 1) to newXRowLength)
    result.add(Coordinate(this.last().first.x + 1, this.last().first.y) to newYRowLength)
    result.add(Coordinate(this.last().first.x - 1, this.last().first.y) to newYRowLength)

    return result.filter { !this.map { it.first }.contains(it.first) }
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
