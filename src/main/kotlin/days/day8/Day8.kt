package days.day8

import days.Day

class Day8 : Day() {
    override fun partOne(): Any {
        val rawInput = readInput()

        val input = rawInput[0]
        val maps: MutableMap<String, Pair<String, String>> = mutableMapOf()

        val newRaw = rawInput.drop(2)
        newRaw.forEach {
            val key = it.substringBefore(" = ")
            val values = it.substringAfter("(").substringBefore(")").split(", ")
            maps[key] = Pair(values[0], values[1])
        }

        var currentNoce = "AAA"
        var inputIndex = 0
        var count = 0
        while (currentNoce != "ZZZ") {
            val direction = input[inputIndex]
            inputIndex++
            if (inputIndex == input.length) {
                inputIndex = 0
            }


            currentNoce = if (direction == 'L') {
                maps[currentNoce]!!.first
            } else {
                maps[currentNoce]!!.second
            }
            println(currentNoce)
            count++
        }

        return count
    }

    override fun partTwo(): Any {
        val rawInput = readInput()

        val input = rawInput[0]
        val maps: MutableMap<String, Pair<String, String>> = mutableMapOf()

        val newRaw = rawInput.drop(2)
        newRaw.forEach {
            val key = it.substringBefore(" = ")
            val values = it.substringAfter("(").substringBefore(")").split(", ")
            maps[key] = Pair(values[0], values[1])
        }

        var currentNodes:MutableSet<String> = maps.map { it.key }.filter { it.endsWith('A') }.toMutableSet()
        var inputIndex = 0
        var count = 0

        val nodeLength = mutableListOf<Int>()

        while (currentNodes.isNotEmpty()) {
            val direction = input[inputIndex]
            inputIndex++
            if (inputIndex == input.length) {
                inputIndex = 0
            }
            val newCurrentNodes = (currentNodes).toMutableSet()
            for (node in currentNodes) {
                newCurrentNodes.remove(node)
                val asdNode =(if (direction == 'L') {
                    maps[node]!!.first
                } else {
                    maps[node]!!.second
                })
                if (asdNode .endsWith('Z')) {
                    nodeLength.add(count+1)
                }else {
                    newCurrentNodes.add(asdNode)
                }
            }
            currentNodes = newCurrentNodes
            count++
        }

        return lcm(nodeLength.map{ it.toLong() }.toLongArray())

    }

    private fun lcm(a: Long, b: Long): Long {
        return a * (b / gcd(a, b))
    }

    private fun lcm(input: LongArray): Long {
        var result = input[0]
        for (i in 1 until input.size) result = lcm(result, input[i])
        return result
    }

    private fun gcd(a: Long, b: Long): Long {
        var a = a
        var b = b
        while (b > 0) {
            val temp = b
            b = a % b // % is remainder
            a = temp
        }
        return a
    }

}

