package days.day3

import days.Day

class Day3: Day(false) {
    override fun partOne(): Any {
        val input: HashMap<Coordinate, Char> = HashMap()

        val rawInput = readInput()
        for (i in rawInput.indices) {
            for (j in rawInput[i].indices) {
                input[Coordinate(i, j)] = rawInput[i][j]
            }
        }

        //filter numbers


        val digits = input.filter { it.value.isDigit() }

        var numberCoordinates :HashSet<HashSet<Coordinate>> = hashSetOf()

        for (digit in digits) {
            numberCoordinates.add(findConnectedNumbers(input, digit.key))
        }

        val validNumberCOordinates = numberCoordinates.filter {coordinateList ->
            coordinateList.any { coordinate ->
                coordinate.getNeighbours().any { neighbour ->
                    input.containsKey(neighbour) && input[neighbour]!! != '.'  && !input[neighbour]!!.isDigit()
                }
            }
        }

        var result = 0

        for (i in rawInput.indices) {
            var stringBuilder = StringBuilder()
            for (j in rawInput[i].indices) {
                if (validNumberCOordinates.flatten().contains(Coordinate(i, j))) {
                    stringBuilder.append(input[Coordinate(i, j)])
                    print(input[Coordinate(i, j)])
                } else {
                    val string = stringBuilder.toString()
                    if (string.isNotEmpty()) {
                        result+= string.toInt()
                        println()
                    }
                    stringBuilder = StringBuilder()
                }
            }
            println()
        }

        return result


        return "day 3 part 2 not Implemented"
    }

    fun findConnectedNumbers(input: HashMap<Coordinate, Char>, currentCoordinate: Coordinate): HashSet<Coordinate> {

        var neighbours = currentCoordinate.getNeighboursLeftRight().filter{neighbour -> input.containsKey(neighbour) && input[neighbour]!!.isDigit()}.toHashSet()

       var neighboursCache = HashSet<Coordinate>()
        neighbours.forEach{ neighbour ->
            val newNeighbours = neighbour.getNeighbours().filter { newNeighbour -> input.containsKey(newNeighbour) && input[newNeighbour]!!.isDigit() }
            neighboursCache.addAll(newNeighbours)
        }
        for(i in 0..100) {
            neighbours.addAll(neighboursCache)
            neighbours.forEach{ neighbour ->
                val newNeighbours = neighbour.getNeighbours().filter { newNeighbour -> input.containsKey(newNeighbour) && input[newNeighbour]!!.isDigit() }
                neighboursCache.addAll(newNeighbours)
            }
        }


        return neighbours
    }

    override fun partTwo(): Any {
        return "hajkl"
    }

}




class Coordinate(val x: Int, val y: Int) {
    fun getNeighbours(): MutableList<Coordinate> {
        return mutableListOf(
            Coordinate(x - 1, y - 1),
            Coordinate(x - 1, y),
            Coordinate(x - 1, y + 1),
            Coordinate(x, y - 1),
            Coordinate(x, y + 1),
            Coordinate(x + 1, y - 1),
            Coordinate(x + 1, y),
            Coordinate(x + 1, y + 1)
        )
    }
    fun getNeighboursLeftRight(): MutableList<Coordinate> {
        return mutableListOf(
            Coordinate(x, y - 1)
            ,this
            , Coordinate(x, y + 1)
        )
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
