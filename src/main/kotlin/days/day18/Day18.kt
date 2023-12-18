package days.day18

import days.Day
import java.util.LinkedList
import java.util.Queue

class Day18 : Day(true) {
    override fun partOne(): Any {
        val digMap = mutableSetOf(Coordinate(0, 0))
        readInput().forEach {
            val direction = it[0]
            val numberOfSteps = it.split(" ")[1].toInt()
            val currentCoordinate = digMap.last()
            digMap.addAll(currentCoordinate.goXInDirextion((numberOfSteps - 1).toLong(), direction))
        }
        val queue: Queue<Coordinate> = LinkedList()
        val visited = mutableSetOf<Coordinate>()
        var count = 0

        queue.add(Coordinate(1, 1))
        visited.add(Coordinate(1, 1))

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            count++

            current.getNeighbours().forEach { neighbor ->
                if (!digMap.contains(neighbor) && !visited.contains(neighbor)) {
                    queue.add(neighbor)
                    visited.add(neighbor)
                }
            }
        }

        return digMap.size + visited.size
    }

    override fun partTwo(): Any {
        val digMap = mutableSetOf(Coordinate(0, 0))
        readInput().forEach {
            val directionNumber = it[it.length - 2]
            val numberOfSteps = it.substringAfter("#").dropLast(2).toUpperCase().toLong(radix = 16)

            when (directionNumber) {
                '0' -> digMap.add(Coordinate(digMap.last().x + numberOfSteps, digMap.last().y))//R
                '1' -> digMap.add(Coordinate(digMap.last().x, digMap.last().y - numberOfSteps))//D
                '2' -> digMap.add(Coordinate(digMap.last().x - numberOfSteps, digMap.last().y))//L
                '3' -> digMap.add(Coordinate(digMap.last().x, digMap.last().y + numberOfSteps))//U
                else -> {
                    throw Exception()
                }
            }
        }
        var result = 0L
        val digMapList = digMap.toList()
        for (i in 0 .. digMap.size-2) {
            result += determinante(digMapList[i], digMapList[i+1])
        }
        result+= determinante(digMapList.last(), digMapList.first())
        return result
    }
}

fun determinante(c1: Coordinate, c2: Coordinate):Long {
    return c1.x * c2.y - c2.x * c1.y
}

class Coordinate(val x: Long, val y: Long) {
    fun goXInDirextion(number: Long, direction: Char): List<Coordinate> {
        val result = mutableListOf(this)
        for (i in 0..number) {
            when (direction) {
                'U' -> result.add(result.last().up())
                'D' -> result.add(result.last().down())
                'L' -> result.add(result.last().left())
                'R' -> result.add(result.last().right())
            }
        }
        return result
    }

    fun up(): Coordinate {
        return Coordinate(x, y - 1)
    }

    fun down(): Coordinate {
        return Coordinate(x, y + 1)
    }

    fun left(): Coordinate {
        return Coordinate(x - 1, y)
    }

    fun right(): Coordinate {
        return Coordinate(x + 1, y)
    }

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
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }
}