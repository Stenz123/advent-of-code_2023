package days.day19

import days.Day

class Day19 : Day() {

    override fun partOne(): Any {
        val process = parseInput()
        val machineParts = parseMachineParts()
        var result = 0

        for (part in machineParts) {
            var currentProcessIdentifier = "in"
            while (currentProcessIdentifier !in "AR") {
                currentProcessIdentifier = process[currentProcessIdentifier]!!.executeProcess(part)
            }
            if (currentProcessIdentifier == "A") {
                result += part.sum()
            }
        }

        return result
    }

    override fun partTwo(): Any {
        val process = parseInput()
        val machineParts = listOf(1..4000, 1..4000, 1..4000, 1..4000)
        return executeProcessP2("in", machineParts, process).sumOf {
            it[0].length * it[1].length * it[2].length * it[3].length
        }
    }

    private fun parseInput(): MutableMap<String, List<String>> {
        val process: MutableMap<String, List<String>> = hashMapOf()

        readInput().takeWhile { it.isNotBlank() }.forEach {
            val identifier = it.substringBefore("{")
            val parts = it.substringAfter("{").substringBefore("}").split(",")
            process[identifier] = parts
        }
        return process
    }

    private fun parseMachineParts(): List<MachinePart> {
        return readInput().dropWhile { it.isNotBlank() }.drop(1).map {
            val split = it.split(",")
            val x = split[0].substringAfter("=").toInt()
            val m = split[1].substringAfter("=").toInt()
            val a = split[2].substringAfter("=").toInt()
            val s = split[3].substringAfter("=").substringBefore("}").toInt()
            MachinePart(x, m, a, s)
        }
    }

    private fun List<String>.executeProcess(machinePart: MachinePart): String {
        for (i in 0 until this.size - 1) {
            val currentThing = this[i]
            val parameter1 = when (currentThing.substringBefore('<').substringBefore('>')) {
                "x" -> machinePart.x
                "m" -> machinePart.m
                "a" -> machinePart.a
                "s" -> machinePart.s
                else -> throw Exception("geh scheißn")
            }
            val parameter2 = currentThing.substringAfter('<').substringAfter('>').substringBefore(":").toInt()

            if (currentThing.contains('<')) {
                if (parameter1 < parameter2) {
                    return currentThing.substringAfter(":")
                }
            } else if (currentThing.contains('>')) {
                if (parameter1 > parameter2) {
                    return currentThing.substringAfter(":")
                }
            }
        }
        return this.last()
    }

    private fun executeProcessP2(identifier: String, ranges: List<IntRange>, map: MutableMap<String, List<String>>): List<List<IntRange>> {
        if (identifier == "R") return listOf()
        if (identifier == "A") return listOf(ranges)

        val process = map[identifier]!!
        var currentMachine = ranges
        val result = mutableListOf<List<IntRange>>()

        for (element in process) {
            if ("<" !in element && ">" !in element) return executeProcessP2(element, currentMachine, map) + result
            val indexOfParameter = XmasValue.valueOf(element[0].toString()).ordinal
            val parameter1 = currentMachine[indexOfParameter]
            val parameterToCompare = element.substringAfter('<').substringAfter('>').substringBefore(":").toInt()

            if (element.contains('<')) {
                if (parameter1.last < parameterToCompare) {
                    return executeProcessP2(element.substringAfter(":"), currentMachine, map) + result
                }
                val splits = parameter1.splitBy(parameterToCompare - 1)
                val newMachines = currentMachine.convertIntoSplits(indexOfParameter, splits)
                currentMachine = newMachines[1]
                result += executeProcessP2(element.substringAfter(":"), newMachines[0], map)

            } else if (element.contains('>')) {
                if (parameter1.first > parameterToCompare) {
                    return executeProcessP2(element.substringAfter(":"), currentMachine, map) + result
                }
                val splits = parameter1.splitBy(parameterToCompare)
                val newMachines = currentMachine.convertIntoSplits(indexOfParameter, splits)
                currentMachine = newMachines[0]
                result += executeProcessP2(element.substringAfter(":"), newMachines[1], map)
            }
        }
        return result
    }
}


class MachinePart(val x: Int, val m: Int, val a: Int, val s: Int) {
    fun sum(): Int {
        return x + m + a + s
    }

    override fun toString(): String {
        return "(x=$x, m=$m, a=$a, s=$s)"
    }
}

fun IntRange.splitBy(number: Int): List<IntRange> {
    if (number !in this) {
        return listOf(this)
    }
    val res = mutableListOf<IntRange>()
    res.add(this.first..number)
    res.add(number + 1..this.last)
    return res
}

fun List<IntRange>.convertIntoSplits(indexOfParameter:Int, splits: List<IntRange>):List<List<IntRange>> {
    val res = mutableListOf<List<IntRange>>()
    for (split in splits) {
        val copy = this.toMutableList()
        copy[indexOfParameter] = split
        res.add(copy)
    }
    return res
}

private val IntRange.length: Long
    get() = (this.last - this.first).toLong() + 1

enum class XmasValue{x, m, a, s}