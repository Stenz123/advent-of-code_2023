package days.day12

import days.Day

class Day12 : Day(true) {
    override fun partOne(): Any {
        val springs: MutableList<Pair<List<Char>, List<Int>>> = mutableListOf()
        val rawInput = readInput()
        rawInput.forEach {
            val input = it.split(" ")
            val spring = input[0].toCharArray().toList()
            val output = input[1].split(",").map { it.toInt() }
            springs.add(Pair(spring, output))
        }

        var result = 0
        for (spring in springs) {
            val combinations = allCombinations(spring.first.count { it == '?' })
            for (combination in combinations) {
                //replace ? with combination
                var counter = -1
                val newSpring = spring.first.mapIndexed { index, c ->
                    if (c == '?') {
                        counter++
                        combination[counter]
                    } else {
                        c
                    }
                }.joinToString("")

                //check if newSpring is valid
                var valid = true
                val split = newSpring.split(".").filter { it.isNotEmpty() }
                if (split.size == spring.second.size) {
                    for (i in spring.second.indices) {
                        if (spring.second[i] != split[i].length) {
                            valid = false
                            break
                        }
                    }
                } else {
                    valid = false
                }
                if (valid) {
                    result++
                }
            }
        }

        return result
    }

    override fun partTwo(): Any {
        val springs: MutableList<Pair<List<Char>, List<Int>>> = mutableListOf()
        val rawInput = readInput()
        rawInput.forEach {
            val input = it.split(" ")
            val spring = input[0] + "?"
            //val spring = input[0]
            val output = input[1].split(",").map { it.toInt() }

            springs.add(
                Pair(
                    spring.toCharArray().toList(),
                    output + output
                //output
                )
            )
        }

        var result = 0
        for (spring in springs) {
            val res = makeGroup(spring.first, spring.second)
            println(res)
            result += res
        }

        return result

    }

    fun makeGroup(group: List<Char>, values: List<Int>): Int {
        var indexOfFirst = group.indexOfFirst { it == '?' }
        if (indexOfFirst == -1) indexOfFirst = group.size
        val newGroup = group.subList(0, indexOfFirst).toMutableList()
        val groupCountSplit = newGroup.joinToString("").split('.').filter { it.isNotBlank() }
        var currentGroupSize = if (newGroup.isNotEmpty() && newGroup.last() == '#') {
            groupCountSplit.last().length
        } else {
            0
        }
        var groupCount = groupCountSplit.count()
        if (groupCount > 0) {

            if (groupCount > values.size) {
                return 0
            }
            if (currentGroupSize > values[groupCount - 1]) {
                return 0
            }
        }
        for (i in indexOfFirst until group.size) {
            if (group[i] == '.') {
                newGroup.add('.')
                currentGroupSize = 0
            } else if (group[i] == '#') {
                if (currentGroupSize == 0) {
                    groupCount++
                }
                currentGroupSize++
                newGroup.add('#')
            } else if (group[i] == '?') {
                if (groupCount > 0) {
                    if (currentGroupSize == values[groupCount - 1]) {
                        val group1 = group.toMutableList()
                        group1[i] = '.'
                        return makeGroup(group1, values)
                    }
                }

                val group1 = group.toMutableList()
                group1[i] = '.'
                val group2 = group.toMutableList()
                group2[i] = '#'
                val option1 = makeGroup(group1, values)
                val option2 = makeGroup(group2, values)
                return option1 + option2

            }
        }
        return if (isValid(values, newGroup.joinToString(""))) {
            1
        } else {
            0
        }
    }

    fun isValid(values: List<Int>, newSpring: String): Boolean {
        val split = newSpring.split(".").filter { it.isNotEmpty() }
        if (split.size == values.size) {
            for (i in values.indices) {
                if (values[i] != split[i].length) {
                    return false
                }
            }
        } else {
            return false
        }
        return true
    }

    fun allCombinations(length: Int): List<List<Char>> {
        val values = listOf('.', '#')

        // Base case: If the length is 0, return a list containing an empty list
        if (length == 0) {
            return listOf(emptyList())
        }

        // Recursive case: Generate combinations for the previous length
        val prevCombinations = allCombinations(length - 1)

        // Build new combinations by appending each value to the existing combinations
        return values.flatMap { value ->
            prevCombinations.map { combination -> listOf(value) + combination }
        }
    }
}

