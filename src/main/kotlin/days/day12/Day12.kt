package days.day12

import days.Day

class Day12 : Day() {
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
            val spring = "${input[0]}?${input[0]}?${input[0]}?${input[0]}?${input[0]}"
            //val spring = input[0]
            val output = input[1].split(",").map { it.toInt() }

            springs.add(
                Pair(
                    spring.toCharArray().toList(),
                    output + output + output + output + output
                    //output
                )
            )
        }

        var result = 0L
        for (spring in springs) {
            val res = makeGroup(spring.first.joinToString (""), spring.second)
            println(res)
            result += res
        }

        return result
    }

    private val cache = hashMapOf<Pair<String, List<Int>>, Long>()
    private fun makeGroup(springs: String, values: List<Int>): Long {
        if (values.isEmpty()) return if (springs.contains('#')) 0 else 1

        if (springs.isEmpty()) return 0

        return cache.getOrPut(springs to values) {
            var result = 0L
            if (springs[0] == '.' || springs[0] == '?') {
                result += makeGroup(springs.drop(1), values)
            }
            if (springs[0] in "#?" && values[0] <= springs.length){
                if ("." !in springs.take(values[0])) {
                    if (values.first() == springs.length || springs[values[0]] != '#'){
                        result += makeGroup(springs.drop(values[0] + 1), values.drop(1))
                    }
                }
            }
            result
        }
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

