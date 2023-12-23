package days.day23

import days.Day
import java.util.*

class Day23: Day() {
    override fun partOne(): Any {
        val map = mutableMapOf<Coordinate, Char>()
        readInput().forEachIndexed{ y, line ->
            line.forEachIndexed{x, char ->
                if (char != '#') {
                    map[Coordinate(x,y)] = char
                }
            }
        }
        val start = map.keys.first { it.y == 0 }
        val end = map.keys.first{it.y == readInput().size-1}

        return dijkstra(start, end, map)
    }

    override fun partTwo(): Any {
        val map = mutableMapOf<Coordinate, Char>()
        readInput().forEachIndexed{ y, line ->
            line.forEachIndexed{x, char ->
                if (char != '#') {
                    map[Coordinate(x,y)] = '.'
                }
            }
        }
        val start = map.keys.first { it.y == 0 }
        val end = map.keys.first{it.y == readInput().size-1}

        return dijkstra(start, end, map)

    }

    fun dijkstra(start: Coordinate, end: Coordinate, map: Map<Coordinate, Char>):Int {
        val distances = mutableMapOf<Coordinate, Int>()
        val visited = mutableSetOf<Coordinate>()
        val priorityQueue:Queue<Coordinate> = LinkedList()
        val previous = mutableMapOf<Coordinate, Coordinate>()

        var result = 0

        // Initialization
        distances[start] = 0
        priorityQueue.add(start)
        priorityQueue.add(start)

        while (priorityQueue.isNotEmpty()) {
            val current = priorityQueue.poll()

            visited.add(current)

            val neighbors = current.getNeighbours(map[current]!!)
                .filter { map.containsKey(it) }
                .filter { !getPath(previous, start, current).contains(it) }
            for (neighbor in neighbors) {
                val newDistance = distances[current]!! + 1
                if (!priorityQueue.contains(neighbor)) {
                    priorityQueue.add(neighbor)
                }

                if (newDistance > distances.getOrDefault(neighbor, Int.MIN_VALUE)) {
                    distances[neighbor] = newDistance
                    previous[neighbor] = current
                    if (neighbor == end) {
                        // Reached the destination, reconstruct and print the path
                        if (distances[neighbor]!! > result) {
                            //printPath(previous, start, end, map)
                            println("\u001B[32m${distances[neighbor]!!}\u001B[0m")
                            result = distances[neighbor]!!
                        }
                    }
                }
            }
        }
        printPath(previous, start, end, map)
        return result
    }

    fun getPath(previous: Map<Coordinate, Coordinate>, start: Coordinate, end: Coordinate): List<Coordinate> {
        var current = end
        val path = mutableListOf<Coordinate>()

        while (current != start) {
            path.add(current)
            current = previous[current] ?: break
        }

        path.add(start)
        path.reverse()
        return path
    }

    fun printPath(previous: Map<Coordinate, Coordinate>, start: Coordinate, end: Coordinate, map: Map<Coordinate, Char>) {
        val path = getPath(previous, start, end)

        for (y in 0..path.maxOfOrNull { it.y }!!) {
            for (x in 0..path.maxOfOrNull { it.x }!!) {
                if (path.contains(Coordinate(x, y))) {
                    print("\u001B[35m*\u001B[0m")
                }else if (map.containsKey(Coordinate(x, y))) {
                    print(map[Coordinate(x,y)])
                }else{
                    print('#')
                }
            }
            println()
        }

        println("Shortest path from $start to $end: $path")
    }

}

class Coordinate(val x: Int, val y: Int) {
    fun getNeighbours(c:Char): List<Coordinate> {
        return when (c) {
            '>' -> listOf(Coordinate(x + 1, y))
            '<' -> listOf(Coordinate(x - 1, y))
            'v' -> listOf(Coordinate(x, y + 1))
            else -> listOf(
                Coordinate(x - 1, y),
                Coordinate(x, y - 1),
                Coordinate(x, y + 1),
                Coordinate(x + 1, y),
            )
        }
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

