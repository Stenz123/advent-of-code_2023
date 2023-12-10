package days.day10

import days.Day

class Day10 : Day(true) {
    override fun partOne(): Any {
        val tubeMap: MutableMap<Coordinate, Pipe> = mutableMapOf()

        val input = readInput()

        for (i in input.indices) {
            for (j in input[i].indices) {
                if (input[i][j] != '.') {
                    tubeMap[Coordinate(j, i)] = Pipe(input[i][j])
                }
            }
        }

        return getLoop(tubeMap).size / 2
    }

    override fun partTwo(): Any {
        val tubeMap: MutableMap<Coordinate, Pipe> = mutableMapOf()
        val input = readInput()
        for (i in input.indices) {
            for (j in input[i].indices) {
                if (input[i][j] != '.') {
                    tubeMap[Coordinate(j, i)] = Pipe(input[i][j])
                }
            }
        }

        val loop = getLoop(tubeMap)


        val smallestX = loop.minOfOrNull { it.x } ?: error("No smallest coordinate found")
        val smallestY = loop.minOfOrNull { it.y } ?: error("No smallest coordinate found")
        val highestX = loop.maxOfOrNull { it.x } ?: error("No highest coordinate found")
        val highestY = loop.maxOfOrNull { it.y } ?: error("No highest coordinate found")

        val filledMap = mutableMapOf<Coordinate, Pipe?>()

        val margin = 2
        for (i in smallestY - margin..highestY + margin) {
            for (j in smallestX - margin..highestX + margin) {
                filledMap[Coordinate(j, i)] = null
            }
        }
        loop.forEach { filledMap[it] = tubeMap[it] }

        //flood fill
        val queue = mutableListOf<Coordinate>()
        val topLeft = Coordinate(smallestX - margin, smallestY - margin)
        queue.add(topLeft)
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            val connected = current.getNeighbours().filter { filledMap.containsKey(it) && filledMap[it] == null }

            connected.forEach {
                if (filledMap.containsKey(it) && filledMap[it] == null) {
                    filledMap[it] = Pipe('X')
                    queue.add(it)
                }
            }
        }

        var tilesInPipe = mutableSetOf<Coordinate>()

        var lookingDirection = Direction.LEFT
        for (coordinate in loop) {
            val pipe = tubeMap[coordinate] ?: throw Exception("ah sjkl")
            if (pipe.direction == 'L') {
                lookingDirection = when (lookingDirection) {
                    Direction.RIGHT -> {
                        if (filledMap[coordinate.right()] == null) {
                            tilesInPipe.add(coordinate.right())
                        }
                        Direction.UP
                    }

                    Direction.UP -> {
                        if (filledMap[coordinate.up()] == null) {
                            tilesInPipe.add(coordinate.up())
                        }
                        Direction.RIGHT
                    }

                    Direction.LEFT -> {
                        if (filledMap[coordinate.left()] == null) {
                            tilesInPipe.add(coordinate.left())
                        }
                        Direction.DOWN
                    }

                    Direction.DOWN -> {
                        if (filledMap[coordinate.down()] == null) {
                            tilesInPipe.add(coordinate.down())
                        }
                        Direction.LEFT
                    }
                }
            } else if (pipe.direction == 'J') {
                lookingDirection = when (lookingDirection) {
                    Direction.LEFT -> {
                        if (filledMap[coordinate.left()] == null) {
                            tilesInPipe.add(coordinate.left())
                        }
                        Direction.UP
                    }

                    Direction.UP -> {
                        if (filledMap[coordinate.up()] == null) {
                            tilesInPipe.add(coordinate.up())
                        }
                        Direction.LEFT
                    }

                    Direction.DOWN -> {
                        if (filledMap[coordinate.down()] == null) {
                            tilesInPipe.add(coordinate.down())
                        }
                        Direction.RIGHT
                    }

                    Direction.RIGHT -> {
                        if (filledMap[coordinate.right()] == null) {
                            tilesInPipe.add(coordinate.right())
                        }
                        Direction.DOWN
                    }
                }
            } else if (pipe.direction == 'F') {
                lookingDirection = when (lookingDirection) {
                    Direction.RIGHT -> {
                        if (filledMap[coordinate.right()] == null) {
                            tilesInPipe.add(coordinate.right())
                        }
                        Direction.DOWN
                    }

                    Direction.DOWN -> {
                        if (filledMap[coordinate.down()] == null) {
                            tilesInPipe.add(coordinate.down())
                        }
                        Direction.RIGHT
                    }

                    Direction.LEFT -> {
                        if (filledMap[coordinate.left()] == null) {
                            tilesInPipe.add(coordinate.left())
                        }
                        Direction.UP
                    }

                    Direction.UP -> {
                        if (filledMap[coordinate.up()] == null) {
                            tilesInPipe.add(coordinate.up())
                        }
                        Direction.LEFT
                    }
                }
            } else if (pipe.direction == '7') {
                lookingDirection = when (lookingDirection) {
                    Direction.LEFT -> {
                        if (filledMap[coordinate.left()] == null) {
                            tilesInPipe.add(coordinate.left())
                        }
                        Direction.DOWN
                    }

                    Direction.DOWN -> {
                        if (filledMap[coordinate.down()] == null) {
                            tilesInPipe.add(coordinate.down())
                        }
                        Direction.LEFT
                    }

                    Direction.RIGHT -> {
                        if (filledMap[coordinate.right()] == null) {
                            tilesInPipe.add(coordinate.right())
                        }
                        Direction.UP
                    }

                    Direction.UP -> {
                        if (filledMap[coordinate.up()] == null) {
                            tilesInPipe.add(coordinate.up())
                        }
                        Direction.RIGHT
                    }
                }
            }
            if (lookingDirection == Direction.RIGHT) {
                if (filledMap[coordinate.right()] == null) {
                    tilesInPipe.add(coordinate.right())
                }
            } else if (lookingDirection == Direction.LEFT) {
                if (filledMap[coordinate.left()] == null) {
                    tilesInPipe.add(coordinate.left())
                }
            }
            else if (lookingDirection == Direction.UP) {
                if (filledMap[coordinate.up()] == null) {
                    tilesInPipe.add(coordinate.up())
                }
            } else if (lookingDirection == Direction.DOWN) {
                if (filledMap[coordinate.down()] == null) {
                    tilesInPipe.add(coordinate.down())
                }
            }

        }

        val queueNew = tilesInPipe.toMutableList()

        val result = queueNew.toMutableList()

        while (queueNew.isNotEmpty()) {
            val current = queueNew.removeFirst()
            val connected = current.getNeighbours().filter { filledMap.containsKey(it) && filledMap[it] == null }

            connected.forEach {
                if (!result.contains(it)) {
                    result.add(it)
                    queueNew.add(it)
                }
            }
        }

        //println(filledMap)
        for (i in smallestY - margin..highestY + margin) {
            for (j in smallestX - margin..highestX + margin) {
                if (tilesInPipe.contains(Coordinate(j, i))) {
                    print("\u001B[35mI\u001B[0m")
                }else if (result.contains(Coordinate(j,i))) {
                    print("\u001B[32mI\u001B[0m")
                } else if (loop.contains(Coordinate(j, i))) {
                    print(tubeMap[Coordinate(j, i)])
                } else {
                    print("\u001B[37m.\u001B[0m")
                }

            }
            println()
        }

        return result.size

    }

    fun getLoop(tubeMap: MutableMap<Coordinate, Pipe>): List<Coordinate> {
        val start = tubeMap.filter { it.value.direction == 'S' }.keys.first()

        val visited = mutableSetOf<Coordinate>(start)
        while (true) {
            val current = visited.lastOrNull() ?: start

            val pipe = tubeMap[current] ?: error("Pipe not found at $current")
            val connected = current.getConnectedCoordinates(pipe, tubeMap)

            if (connected.contains(start)) {
                if (visited.size > 2) {
                    return visited.toList()
                }
            }

            val next = connected.firstOrNull { !visited.contains(it) }
            if (next != null) {
                visited.add(next)
            }
        }
    }

}

class Pipe(val direction: Char) {
    fun isConnectedToDown(): Boolean {
        return direction == '|' || direction == 'F' || direction == '7' || direction == 'S'
    }

    fun isConnectedToUp(): Boolean {
        return direction == '|' || direction == 'L' || direction == 'J' || direction == 'S'
    }

    fun isConnectedToLeft(): Boolean {
        return direction == '-' || direction == 'J' || direction == '7' || direction == 'S'
    }

    fun isConnectedToRight(): Boolean {
        return direction == '-' || direction == 'F' || direction == 'L' || direction == 'S'
    }

    override fun toString(): String {
        return direction.toString()
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

    fun addIfConnected(
        result: MutableList<Coordinate>,
        map: Map<Coordinate, Pipe?>,
        coord: Coordinate?,
        checkConnection: (Pipe) -> Boolean
    ) {
        if (coord != null && map.containsKey(coord) && checkConnection(
                map[coord] ?: error("Pipe not found at $coord")
            )
        ) {
            result.add(coord)
        }
    }

    fun getConnectedCoordinates(pipe: Pipe, map: Map<Coordinate, Pipe?>): List<Coordinate> {
        val result = mutableListOf<Coordinate>()

        when (pipe.direction) {
            '|' -> {
                addIfConnected(result, map, up(), Pipe::isConnectedToDown)
                addIfConnected(result, map, down(), Pipe::isConnectedToUp)
            }

            '-' -> {
                addIfConnected(result, map, left(), Pipe::isConnectedToRight)
                addIfConnected(result, map, right(), Pipe::isConnectedToLeft)
            }

            'F' -> {
                addIfConnected(result, map, right(), Pipe::isConnectedToLeft)
                addIfConnected(result, map, down(), Pipe::isConnectedToUp)
            }

            '7' -> {
                addIfConnected(result, map, left(), Pipe::isConnectedToRight)
                addIfConnected(result, map, down(), Pipe::isConnectedToUp)
            }

            'L' -> {
                addIfConnected(result, map, right(), Pipe::isConnectedToLeft)
                addIfConnected(result, map, up(), Pipe::isConnectedToDown)
            }

            'J' -> {
                addIfConnected(result, map, left(), Pipe::isConnectedToRight)
                addIfConnected(result, map, up(), Pipe::isConnectedToDown)
            }

            'S' -> {
                addIfConnected(result, map, left(), Pipe::isConnectedToRight)
                addIfConnected(result, map, down(), Pipe::isConnectedToUp)
                addIfConnected(result, map, up(), Pipe::isConnectedToDown)
                addIfConnected(result, map, right(), Pipe::isConnectedToLeft)
            }
        }

        return result
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