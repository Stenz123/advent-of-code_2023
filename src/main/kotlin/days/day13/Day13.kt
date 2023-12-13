package days.day13

import days.Day

class Day13 : Day() {
    override fun partOne(): Any {
        val input: MutableList<List<String>> = mutableListOf()

        var temp = mutableListOf<String>()
        readInput().forEach {
            if (it.isNotBlank()) {
                temp.add(it)
            } else {
                input.add(temp)
                temp = mutableListOf()
            }
        }
        input.add(temp)


        var result = 0
        input.forEach { it ->
            val mirrorIndex = findMirror(it)
            if (mirrorIndex.first() != -1) {
                result += mirrorIndex.first()
                println(mirrorIndex)
            } else {
                val spinnedInput = mutableListOf<String>()
                for (i in it.first().indices) {
                    spinnedInput.add(it.map { line -> line[i] }.joinToString(""))
                }
                val spinnedMirrorIndex = findMirror(spinnedInput)
                result += 100 * spinnedMirrorIndex.first()
                println(spinnedMirrorIndex)
            }
        }

        return result
    }

    override fun partTwo(): Any {
        val input: MutableList<List<String>> = mutableListOf()

        var temp = mutableListOf<String>()
        readInput().forEach {
            if (it.isNotBlank()) {
                temp.add(it)
            } else {
                input.add(temp)
                temp = mutableListOf()
            }
        }
        input.add(temp)


        var result = 0
        input.forEach { it ->
            val oldMirrorIndex = findMirror(it)
            var oldSpinnedMirrorIndex = -1
            if (oldMirrorIndex.first() == -1) {
                val spinnedInput = mutableListOf<String>()
                for (i in it.first().indices) {
                    spinnedInput.add(it.map { line -> line[i] }.joinToString(""))
                }
                oldSpinnedMirrorIndex = findMirror(spinnedInput).first()
            }

            loop@ for (i in it.indices) {
                for (j in it.first().indices) {
                    val newInput = it.toMutableList()
                    if (newInput[i] == "........##..#") {
                        //println(it.first().length)
                    }
                    if (newInput[i][j] == '.') {
                        newInput[i] = newInput[i].take(j) + '#' + newInput[i].substringAfter(newInput[i].take(j) + '.')

                    } else {
                        newInput[i] = newInput[i].take(j) + '.' + newInput[i].substringAfter(newInput[i].take(j) + '#')
                    }

                    val mirrorIndexs = findMirror(newInput)
                    for (mirrorIndex in mirrorIndexs) {
                        if (mirrorIndex != -1 && mirrorIndex != oldMirrorIndex.first()) {
                            result += mirrorIndex
                            println(mirrorIndex)
                            break@loop
                        }
                    }
                    val spinnedInput = mutableListOf<String>()
                    for (i in it.first().indices) {
                        spinnedInput.add(newInput.map { line -> line[i] }.joinToString(""))
                    }
                    val spinnedMirrorIndexs = findMirror(spinnedInput)
                    for (spinnedMirrorIndex in spinnedMirrorIndexs) {
                        if (spinnedMirrorIndex != -1 && spinnedMirrorIndex != oldSpinnedMirrorIndex) {
                            result += 100 * spinnedMirrorIndex
                            println(spinnedMirrorIndex)
                            break@loop
                        }
                    }

                }
            }
        }

        return result

    }

    fun findMirror(input: List<String>): List<Int> {
        if (input.contains("#......#.#..#")) {
           println()
        }

        val result = mutableListOf<Int>()

        for (i in 1 until input.first().length) {
            var isMirror = true
            loop@ for (line in input) {
                val leftPart = line.take(i)
                val rightPart = line.substring(i)

                if (leftPart.length > rightPart.length) {
                    if (!leftPart.reversed().startsWith(rightPart)) {
                        isMirror = false
                        //result.add(i)
                        break@loop
                    }
                } else {
                    if (!rightPart.startsWith(leftPart.reversed())) {
                        isMirror = false
                        //result.add(i)
                        break@loop
                    }
                }
            }
            if (isMirror) {
                result.add(i)
            }
        }
        return result.ifEmpty { listOf(-1) }
    }
}

