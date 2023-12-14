package days.day14

import days.Day
import java.util.*

class Day14 : Day() {
    override fun partOne(): Any {
        val input = readInput()
        val maxSize = input.size
        var inputMap = mutableMapOf<Coordinate, Char>()
        input.forEachIndexed { i, l ->
            l.forEachIndexed { j, c ->
                if (c != '.') {
                    inputMap[Coordinate(j, maxSize - i)] = c
                }
            }
        }

        var direction = 0

        val cache = mutableListOf<MutableMap<Coordinate, Char>>()

        var complete = false
        val cycles = 1000000000L
        var count = 0L
        while (count < 4 * cycles) {

            val queue: Queue<Coordinate> = when (direction) {
                0 -> LinkedList(inputMap.filter { it.value == 'O' }.map { it.key }.sortedByDescending { it.y })
                1 -> LinkedList(inputMap.filter { it.value == 'O' }.map { it.key }.sortedBy { it.x })
                2 -> LinkedList(inputMap.filter { it.value == 'O' }.map { it.key }.sortedBy { it.y })
                3 -> LinkedList(inputMap.filter { it.value == 'O' }.map { it.key }.sortedByDescending { it.x })
                else -> {
                    throw Exception()
                }
            }

            for (i in 0 until queue.size) {
                val rock = queue.remove()
                val freeSpace = when (direction) {
                    0 -> findNextNorthFreeSpace(rock, inputMap)
                    1 -> findNextWestFreeSpace(rock, inputMap)
                    2 -> findextSouthFreeSpace(rock, inputMap)
                    3 -> findNextEastFreeSpace(rock, inputMap)
                    else -> {
                        throw Exception()
                    }
                }
                inputMap.remove(rock)
                inputMap[freeSpace] = 'O'
            }
            direction++
            direction %= 4
            if (direction == 0 && !complete) {
                if (cache.contains(inputMap)) {
                    val loopStart = cache.indexOf(inputMap)
                    val sizeOfLoop = count / 4 - loopStart
                    val iterationsLeft = ((1000000000L - loopStart) % sizeOfLoop)
                    count = (1000000000L - iterationsLeft)*4+4
                    println(iterationsLeft)
                    complete=true
                } else {
                    println(count)
                    cache.add(inputMap.toMutableMap())
                }
            }
            count++
        }
        printMap(inputMap)
        return inputMap.filter { it.value == 'O' }.map { it.key }.sumOf { it.y }
    }

    override fun partTwo(): Any {
        return "day 14 part 2 not Implemented"
    }

    fun printMap(map: Map<Coordinate, Char>) {

        for (y in map.keys.map { it.y }.max() downTo 1) {
            for (x in 0..map.keys.map { it.x }.max()) {
                if (map.containsKey(Coordinate(x, y))) {
                    print(map[Coordinate(x, y)])
                } else {
                    print('.')
                }
            }
            println()
        }
    }
}

fun findNextNorthFreeSpace(coordinate: Coordinate, map: Map<Coordinate, Char>): Coordinate {
    return map.map { it.key }
        .filter { it.x == coordinate.x }
        .filter { it.y > coordinate.y }
        .map { Coordinate(it.x, it.y - 1) }
        .minByOrNull { it.y } ?: Coordinate(coordinate.x, map.map { it.key.y }.max())
}

fun findNextWestFreeSpace(coordinate: Coordinate, map: Map<Coordinate, Char>): Coordinate {
    return map.map { it.key }
        .filter { it.y == coordinate.y }
        .filter { it.x < coordinate.x }
        .map { Coordinate(it.x + 1, it.y) }
        .maxByOrNull { it.x } ?: Coordinate(0, coordinate.y)
}

fun findextSouthFreeSpace(coordinate: Coordinate, map: Map<Coordinate, Char>): Coordinate {
    return map.map { it.key }
        .filter { it.x == coordinate.x }
        .filter { it.y < coordinate.y }
        .map { Coordinate(it.x, it.y + 1) }
        .maxByOrNull { it.y } ?: Coordinate(coordinate.x, 1)
}

fun findNextEastFreeSpace(coordinate: Coordinate, map: Map<Coordinate, Char>): Coordinate {
    //println("---")
    return map.map { it.key }
        .filter {
            it.y == coordinate.y
        }.filter {
            it.x > coordinate.x
        }.map {
            //println("(${it.x}|${it.y})")
            Coordinate(it.x - 1, it.y)
        }.minByOrNull { it.x } ?: Coordinate(map.map { it.key.x }.max(), coordinate.y)
}

class Coordinate(val x: Int, val y: Int) : Comparable<Coordinate> {
    override fun compareTo(other: Coordinate): Int {

        if (this.y == other.y) {
            return this.x.compareTo(other.x)
        }
        return this.y.compareTo(other.y)

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

    override fun toString(): String {
        return "Coordinate(x=$x, y=$y)"
    }
}


