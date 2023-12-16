package days.day16

import days.Day
import java.util.*

class Day16 : Day(false) {
    override fun partOne(): Any {
        val mirrorMap = parseInput(readInput())

        val startPositions = LinkedList<Beam>()
        startPositions.add(Beam(Coordinate(0, 0), Direction.RIGHT))
        return simulate(startPositions, mirrorMap, true)
    }

    override fun partTwo(): Any {
        val input = readInput()
        val mirrorMap = parseInput(input)


        val maxX = mirrorMap.map { it.key.x }.max()
        val maxY = mirrorMap.map { it.key.y }.max()

        val possibleStartPossitions: Queue<Beam> = LinkedList()
        for (x in input.first().indices) {
            possibleStartPossitions.add(Beam(Coordinate(x, 0), Direction.DOWN))
            possibleStartPossitions.add(Beam(Coordinate(x, maxY), Direction.UP))
        }
        for (y in input.indices) {
            possibleStartPossitions.add(Beam(Coordinate(0, y), Direction.RIGHT))
            possibleStartPossitions.add(Beam(Coordinate(maxX, y), Direction.LEFT))
        }

        return simulate(possibleStartPossitions, mirrorMap)
    }

    fun simulate(
        possibleStartPossitions: Queue<Beam>,
        mirrorMap: Map<Coordinate, Char>,
        visualize: Boolean = false
    ): Int {
        val maxX = mirrorMap.map { it.key.x }.max()
        val maxY = mirrorMap.map { it.key.y }.max()
        var result = -1
        while (possibleStartPossitions.isNotEmpty()) {
            val startBeam = possibleStartPossitions.remove()
            val illuminatedFields = mutableSetOf(startBeam)
            val currentBeams: Queue<Beam> = LinkedList(listOf(startBeam))

            while (currentBeams.isNotEmpty()) {
                val currentBeam = currentBeams.remove()
                if (mirrorMap.containsKey(currentBeam.coordinate)) {
                    val newBeams = mirrorMap[currentBeam.coordinate]!!.deflect(currentBeam)
                        .filter { it.coordinate.x >= 0 && it.coordinate.y >= 0 && it.coordinate.x <= maxX && it.coordinate.y <= maxY }
                        .filter { illuminatedFields.add(it) }
                    currentBeams.addAll(newBeams)
                }
            }
            val res = illuminatedFields.distinctBy { it.coordinate }.count()
            if (res > result) {
                result = res
            }
            if (visualize) {
                visualize(illuminatedFields)
            }
        }
        return result
    }

    fun parseInput(input: List<String>): Map<Coordinate, Char> {
        val mirrorMap = mutableMapOf<Coordinate, Char>()
        for (i in input.indices) {
            for (j in input[i].indices) {
                mirrorMap[Coordinate(j, i)] = input[i][j]
            }
        }
        return mirrorMap
    }

    fun visualize(input: Set<Beam>) {
        val maxX = input.map { it.coordinate.x }.max()
        val maxY = input.map { it.coordinate.y }.max()

        for (y in 0..maxY) {
            for (x in 0..maxX) {
                if (input.map { it.coordinate }.contains(Coordinate(x, y))) {
                    print("#")
                } else {
                    print(".")
                }
            }
            println()
        }

    }

    fun Char.deflect(beam: Beam): List<Beam> {
        var result = mutableListOf<Beam>()
        when (this) {
            '/' -> {
                when (beam.direction) {
                    Direction.LEFT -> result.add(Beam(beam.coordinate.down(), Direction.DOWN))
                    Direction.UP -> result.add(Beam(beam.coordinate.right(), Direction.RIGHT))
                    Direction.DOWN -> result.add(Beam(beam.coordinate.left(), Direction.LEFT))
                    Direction.RIGHT -> result.add(Beam(beam.coordinate.up(), Direction.UP))
                }
            }

            '\\' -> {
                when (beam.direction) {
                    Direction.LEFT -> result.add(Beam(beam.coordinate.up(), Direction.UP))
                    Direction.UP -> result.add(Beam(beam.coordinate.left(), Direction.LEFT))
                    Direction.DOWN -> result.add(Beam(beam.coordinate.right(), Direction.RIGHT))
                    Direction.RIGHT -> result.add(Beam(beam.coordinate.down(), Direction.DOWN))
                }
            }

            '|' -> {
                if (beam.direction == Direction.LEFT || beam.direction == Direction.RIGHT) {
                    result.add(Beam(beam.coordinate.up(), Direction.UP))
                    result.add(Beam(beam.coordinate.down(), Direction.DOWN))
                }
                if (beam.direction == Direction.DOWN) result.add(Beam(beam.coordinate.down(), Direction.DOWN))
                if (beam.direction == Direction.UP) result.add(Beam(beam.coordinate.up(), Direction.UP))
            }

            '-' -> {
                if (beam.direction == Direction.UP || beam.direction == Direction.DOWN) {
                    result.add(Beam(beam.coordinate.left(), Direction.LEFT))
                    result.add(Beam(beam.coordinate.right(), Direction.RIGHT))
                }
                if (beam.direction == Direction.LEFT) result.add(Beam(beam.coordinate.left(), Direction.LEFT))
                if (beam.direction == Direction.RIGHT) result.add(Beam(beam.coordinate.right(), Direction.RIGHT))
            }

            '.' -> {
                when (beam.direction) {
                    Direction.LEFT -> result.add(Beam(beam.coordinate.left(), Direction.LEFT))
                    Direction.UP -> result.add(Beam(beam.coordinate.up(), Direction.UP))
                    Direction.DOWN -> result.add(Beam(beam.coordinate.down(), Direction.DOWN))
                    Direction.RIGHT -> result.add(Beam(beam.coordinate.right(), Direction.RIGHT))
                }
            }
        }
        return result
    }
}

class Beam(var coordinate: Coordinate, var direction: Direction) {
    override fun toString(): String {
        var directionChar = when (direction) {
            Direction.UP -> '^'
            Direction.DOWN -> 'v'
            Direction.LEFT -> '<'
            Direction.RIGHT -> '>'
        }
        return "$directionChar $coordinate $directionChar"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Beam

        if (coordinate != other.coordinate) return false
        if (direction != other.direction) return false

        return true
    }

    override fun hashCode(): Int {
        var result = coordinate.hashCode()
        result = 31 * result + direction.hashCode()
        return result
    }

}

class Coordinate(val x: Int, val y: Int) {
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

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}