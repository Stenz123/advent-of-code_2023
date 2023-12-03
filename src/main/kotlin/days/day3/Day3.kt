package days.day3

import days.Day

class Day3: Day(false) {
    override fun partOne(): Any {
        //467..114..
        //...*.....
        //..35..633.
        //......#...
        //617*......
        //.....+.58.
        //..592.....
        //......755.
        //...$.*....
        //.664.598..

        val input: HashMap<Coordinate, Char> = HashMap()

        val rawInput = readInput()
        for (i in rawInput.indices) {
            for (j in rawInput[i].indices) {
                input[Coordinate(i, j)] = rawInput[i][j]
            }
        }



        val validDigits: HashMap<Coordinate, Char> = HashMap()

        input.filter { !it.value.isDigit() && it.value != '.' }
            .forEach{ coordinateChar ->
                println(coordinateChar)
                findConnectedNumbers(input, coordinateChar.key, validDigits).forEach{
                     validDigits[it.key] = it.value
                 }
            }


        input.filter { it.value.isDigit() && it.key !in validDigits.keys }
            .forEach { coordinateChar ->
                println(coordinateChar)
            }



        var result = 0

        for (i in rawInput.indices) {
            var stringBuilder = StringBuilder()
            for (j in rawInput[i].indices) {
                if (validDigits.containsKey(Coordinate(i, j))) {
                    stringBuilder.append(validDigits[Coordinate(i, j)])
                    //print(validDigits[Coordinate(i, j)])
                } else {
                    val string = stringBuilder.toString()
                    if (string.isNotEmpty()) {
                        result+= string.toInt()
                        //println()
                    }
                    stringBuilder = StringBuilder()
                }
            }
           //println()
        }

        return result
    }

    fun findConnectedNumbers(input: HashMap<Coordinate, Char>, currentCoordinate: Coordinate, validDigits: HashMap<Coordinate, Char>): HashMap<Coordinate, Char> {

        val neighbours :List<Coordinate> = if (!input[currentCoordinate]!!.isDigit()) {
            currentCoordinate.getNeighbours()
        } else{
            currentCoordinate.getNeighbours()
        }

        val neighboursWithValues = neighbours.filter { neighbour -> input.containsKey(neighbour) }
        val neighboursWithValuesAndDigits = neighboursWithValues.filter { neighbour -> input[neighbour]!!.isDigit() }
        val neighboursWithValuesAndDigitsAndNotVisited = neighboursWithValuesAndDigits.filter { neighbour -> !validDigits.containsKey(neighbour) }
        val neighboursWithValuesAndDigitsAndNotVisitedAndNotCurrent = neighboursWithValuesAndDigitsAndNotVisited.filter { neighbour -> neighbour != currentCoordinate }

        if (neighboursWithValuesAndDigitsAndNotVisitedAndNotCurrent.isEmpty()) {
            return validDigits
        }

        neighboursWithValuesAndDigitsAndNotVisitedAndNotCurrent.forEach { neighbour ->
            validDigits[neighbour] = input[neighbour]!!
            findConnectedNumbers(input, neighbour, validDigits)
        }

        return validDigits
    }

    override fun partTwo(): Any {
        val input: HashMap<Coordinate, Char> = HashMap()

        val rawInput = readInput()
        for (i in rawInput.indices) {
            for (j in rawInput[i].indices) {
                input[Coordinate(i, j)] = rawInput[i][j]
            }
        }

        //filter numbers

        val validDigits: HashMap<Coordinate, Char> = HashMap()

        val digits = input.filter { it.value.isDigit() && it.key.getNeighbours().any { neighbour -> input.containsKey(neighbour) && input[neighbour]!! != '.'  && input[neighbour]!!.isDigit()}}

        for (digit in digits) {
            var neighbours = digit.key.getNeighboursLeftRight().filter { neighbour -> input.containsKey(neighbour) && input[neighbour]!! != '.'  && input[neighbour]!!.isDigit() }
                .toMutableList()



        }

        return "day 3 part 2 not Implemented"
    }

    fun findConnectedNumbersLeftRight(input: HashMap<Coordinate, Char>, currentCoordinate: Coordinate, validDigits: HashMap<Coordinate, Char>): HashMap<Coordinate, Char> {

        val neighbours :List<Coordinate> = if (!input[currentCoordinate]!!.isDigit()) {
            currentCoordinate.getNeighbours()
        } else{
            currentCoordinate.getNeighbours()
        }

        val neighboursWithValues = neighbours.filter { neighbour -> input.containsKey(neighbour) }
        val neighboursWithValuesAndDigits = neighboursWithValues.filter { neighbour -> input[neighbour]!!.isDigit() }
        val neighboursWithValuesAndDigitsAndNotVisited = neighboursWithValuesAndDigits.filter { neighbour -> !validDigits.containsKey(neighbour) }
        val neighboursWithValuesAndDigitsAndNotVisitedAndNotCurrent = neighboursWithValuesAndDigitsAndNotVisited.filter { neighbour -> neighbour != currentCoordinate }

        if (neighboursWithValuesAndDigitsAndNotVisitedAndNotCurrent.isEmpty()) {
            return validDigits
        }

        neighboursWithValuesAndDigitsAndNotVisitedAndNotCurrent.forEach { neighbour ->
            validDigits[neighbour] = input[neighbour]!!
            findConnectedNumbers(input, neighbour, validDigits)
        }

        return validDigits
    }
}




class Coordinate(val x: Int, val y: Int) {
    fun getNeighbours(): List<Coordinate> {
        return listOf(
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
    fun getNeighboursLeftRight(): List<Coordinate> {
        return listOf(
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
