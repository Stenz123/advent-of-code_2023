package days.day22

import days.Day
import java.util.*

class Day22: Day() {
    override fun partOne(): Any {
        val bricks = readInput().map {
            val split = it.split(("~"))
            val coordinates = split.map {
                val coSplit = it.split(",").map { it.toInt() }
                Coordinate(coSplit[0], coSplit[1], coSplit[2])
            }
            Brick(coordinates[0], coordinates[1])
        }
        val brickCoordinateMap = mutableListOf<Coordinate>()

        val queue: PriorityQueue<Brick> = PriorityQueue()
        queue.addAll(bricks)
        while (queue.isNotEmpty()) {
            val current = queue.poll()
            println("$current ${current.lowestZ}")
            while (current.checkIfCanFall(brickCoordinateMap)) {
                current.moveDown()
            }
            brickCoordinateMap.addAll(current.getAllBrickCoordinates())
        }

        var result = 0
        for (brick in bricks) {
            val brickCoordinateMapWithBrickRemoved = brickCoordinateMap.toMutableList()
            brickCoordinateMapWithBrickRemoved.removeAll (brick.getAllBrickCoordinates())

            val bricksAbove = bricks.filter { it.lowestZ > brick.lowestZ && it != brick }.sortedBy { it.lowestZ }
            var isValid = true
            for (otherBrick in bricksAbove) {
                if (otherBrick.checkIfCanFall(brickCoordinateMapWithBrickRemoved)) {
                    isValid = false
                    break
                }
            }
            if (isValid) result++
        }

        return result
    }

    override fun partTwo(): Any {
        return "day 22 part 2 not Implemented"
    }
}

class Brick(val start: Coordinate, val end: Coordinate):Comparable<Brick>{
    val lowestZ get() = if (start.z > end.z) end.z else start.z

    fun checkIfCanFall(map: List<Coordinate>):Boolean {
        if (this.lowestZ == 1) return false
        val res = !getAllBrickCoordinates().any {
            val down = it.down()
            if (!getAllBrickCoordinates().contains(down)) {
                map.contains(it.down())
            } else {
                false
            }
        }
        return res
    }
    fun getAllBrickCoordinates(): List<Coordinate> {
        if (this.start == this.end) return listOf(this.start)

        val res = mutableListOf<Coordinate>()
        if (start.x - end.x != 0) {
            val xRange = if (start.x < end.x) start.x .. end.x else end.x .. start.x
            for (newX in xRange) { res.add(Coordinate(newX,start.y,start.z)) }
        }else if (start.y - end.y != 0) {
            val yRange = if (start.y < end.y) start.y .. end.y else end.y .. start.y
            for (newY in yRange) { res.add(Coordinate(start.x,newY,start.z)) }
        }else if (start.z - end.z != 0) {
            val zRange = if (start.z < end.z) start.z .. end.z else end.z .. start.z
            for (newZ in zRange) { res.add(Coordinate(start.x, start.y, newZ)) }
        }
        return res
    }
    fun moveDown() {
        this.start.z--
        this.end.z--
    }

    override fun compareTo(other: Brick): Int {
        var res = this.lowestZ.compareTo(other.lowestZ)

        return res
    }


    override fun toString(): String {
        return "($start ~ $end)"
    }
}

class Coordinate(val x: Int, val y: Int, var z: Int) {

    fun down(): Coordinate {
        return Coordinate(x,y,z-1)
    }

    override fun toString(): String {
        return "($x, $y, $z)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Coordinate

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + z
        return result
    }
}