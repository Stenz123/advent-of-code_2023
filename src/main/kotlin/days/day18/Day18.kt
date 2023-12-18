package days.day18

import days.Day
import kotlin.math.abs

class Day18 : Day() {
    override fun partOne(): Any {
        val digMap = mutableSetOf(Coordinate(0, 0))
        readInput().forEach {
            val direction = it[0]
            val numberOfSteps = it.split(" ")[1].toInt()
            val currentCoordinate = digMap.last()
            digMap.addAll(currentCoordinate.goXInDirextion((numberOfSteps - 1).toLong(), direction))
        }
        var result = 0L
        val digMapList = digMap.toList()
        for (i in 0 .. digMap.size-2) {
            result += determinante(digMapList[i], digMapList[i+1])
        }
        result+= determinante(digMapList.last(), digMapList.first())
        return (result+digMapList.size)/2 + 1
    }

    override fun partTwo(): Any {
        val digMap = mutableSetOf(Coordinate(0, 0))
        readInput().forEach {
            val directionNumber = it[it.length - 2]
            val numberOfSteps = it.substringAfter("#").dropLast(2).toLong(radix = 16)

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
        val digMapList = digMap.toList()
        var borderLength = 0L
        var area = 0L
        for (i in 0 .. digMap.size-2) {
            borderLength += abs(digMapList[i].x-digMapList[i+1].x)
            borderLength += abs(digMapList[i].y-digMapList[i+1].y)

            area += determinante(digMapList[i], digMapList[i+1])
        }
        area+= determinante(digMapList.last(), digMapList.first())
        borderLength += abs(digMapList.last().x-digMapList.first().x)
        borderLength += abs(digMapList.last().y-digMapList.first().y)

        return (abs(area)+borderLength) /2L + 1L
    }
}

fun determinante(c1: Coordinate, c2: Coordinate):Long {
    return (c1.y + c2.y) * (c1.x - c2.x)
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