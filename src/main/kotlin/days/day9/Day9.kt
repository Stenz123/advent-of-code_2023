package days.day9

import days.Day

class Day9: Day(false) {
    override fun partOne(): Any {
        val inputs = readInput().map { it.split(" ").map { it.toInt() } }

        val result = mutableListOf<Int>()

        for (input in inputs) {
            val listsUnitZero: MutableList<MutableList<Int>> = mutableListOf(input.toMutableList())

            while (!listsUnitZero.last().all { it == 0 }) {
                val differenceList = makeDifferenceList(listsUnitZero.last())
                listsUnitZero.add(differenceList)
            }
            for (i in listsUnitZero.size-1 downTo  1){
                listsUnitZero[i-1].add(listsUnitZero[i].last()+listsUnitZero[i-1].last())
            }
            result.add(listsUnitZero[0].last())
        }

        return result.sum()
    }

    override fun partTwo(): Any {
        val inputs = readInput().map { it.split(" ").map { it.toInt() } }

        val result = mutableListOf<Int>()

        for (input in inputs) {
            val listsUnitZero: MutableList<MutableList<Int>> = mutableListOf(input.toMutableList())

            while (!listsUnitZero.last().all { it == 0 }) {
                val differenceList = makeDifferenceList(listsUnitZero.last())
                listsUnitZero.add(differenceList)
            }
            for (i in listsUnitZero.size-1 downTo  1){
                listsUnitZero[i-1].add(0,listsUnitZero[i-1].first() - listsUnitZero[i].first())
            }
            result.add(listsUnitZero[0].first())
        }

        return result.sum()
    }

    private fun makeDifferenceList(input:List<Int>):MutableList<Int> {
        val result = mutableListOf<Int>()
        for (i in 0 until input.size-1) {
            result.add(input[i+1] - input[i])
        }
        return result
    }
}

