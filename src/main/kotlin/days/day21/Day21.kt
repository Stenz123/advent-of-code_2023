package days.day21

import days.Day
import java.util.*

class Day21 : Day() {
    override fun partOne(): Any {
        return solve(64)
    }

    override fun partTwo(): Any {//131 input size
        val x = (26501365L - 65) / 131
        val values = arrayOf(solve(65), solve(65 + 131), solve(65 + 131 * 2))
        val quadrat = quadrat(values)
        return x * x * quadrat.first + x * quadrat.second + quadrat.third
    }

    fun quadrat(values: Array<Int>):Triple<Int,Int,Int> {
        //Lagrange
        // * a = y0/2 - y1 + y2/2
        // * b = -3*y0/2 + 2*y1 - y2/2
        // * c = y0
        return Triple(
            values[0] / 2 - values[1] + values[2] / 2,
            -3 * (values[0] / 2) + 2 * values[1] - values[2] / 2,
            values[0]
        )


    }

    fun solve(steps: Int): Int {
        val inputMap = mutableListOf<Coordinate>()
        var startingPosition:Coordinate? = null
        readInput().forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if (char != '#') {
                    inputMap.add(Coordinate(x, y))
                }
                if (char == 'S') {
                    startingPosition = Coordinate(x,y)
                }
            }
        }
        if (startingPosition == null) throw Exception("No start found")

        val smallestX = inputMap.minOfOrNull { it.x }!!
        val largestX = inputMap.maxOfOrNull { it.x }!! + 1
        val smallestY = inputMap.minOfOrNull { it.y }!!
        val largestY = inputMap.maxOfOrNull { it.y }!! + 1


        val queue:Queue<Coordinate> = LinkedList()
        queue.add(startingPosition)
        val data = mutableListOf<Int>()

        for (i in 0 until steps) {
            val nextQueue: MutableSet<Coordinate> = mutableSetOf()
            while (queue.isNotEmpty()) {
                val current = queue.remove()
                val neighbours = current.getNeighbours()
                val neighboursFiltered = neighbours.toMutableList()
                neighbours.forEach {
                    val mappedX = if (it.x >= largestX) {
                        it.x % (largestX - smallestX) + smallestX
                    } else if (it.x < smallestX) {
                        largestX - ((smallestX - it.x - 1) % (largestX - smallestX)) - 1
                    } else {
                        it.x
                    }
                    val mappedY = if (it.y >= largestY) {
                        it.y % (largestY - smallestY) + smallestY
                    } else if (it.y < smallestY) {
                        largestY - ((smallestY - it.y - 1) % (largestY - smallestY)) - 1
                    } else {
                        it.y
                    }
                    if (!inputMap.contains(Coordinate(mappedX, mappedY))) {
                        neighboursFiltered.remove(it)
                    }
                }

                nextQueue.addAll(neighboursFiltered)
            }
            queue.addAll(nextQueue)
            data.add(nextQueue.size)
            println("${i}: ${nextQueue.size}")
        }
        return queue.size
    }

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


