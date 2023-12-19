package days.day19

import days.Day
import java.awt.font.NumericShaper.Range

class Day19 : Day() {
    override fun partOne(): Any {
        val process: MutableMap<String, List<String>> = hashMapOf()

        readInput().takeWhile { it.isNotBlank() }.forEach {
            val identifier = it.substringBefore("{")
            val parts = it.substringAfter("{").substringBefore("}").split(",")
            process[identifier] = parts
        }

        val machineParts = readInput().dropWhile { it.isNotBlank() }.drop(1).map {
            val split = it.split(",")
            val x = split[0].substringAfter("=").toInt()
            val m = split[1].substringAfter("=").toInt()
            val a = split[2].substringAfter("=").toInt()
            val s = split[3].substringAfter("=").substringBefore("}").toInt()
            MachinePart(x, m, a, s)
        }
        var result = 0

        for (part in machineParts) {
            var currentProcessIdentifier = "in"
            while (currentProcessIdentifier !in "AR") {
                currentProcessIdentifier = process[currentProcessIdentifier]!!.executeProcess(part)
            }
            if (currentProcessIdentifier == "A") {
                result+=part.sum()
            }
        }

        return result
    }

    override fun partTwo(): Any {
        val process: MutableMap<String, List<String>> = hashMapOf()

        readInput().takeWhile { it.isNotBlank() }.forEach {
            val identifier = it.substringBefore("{")
            val parts = it.substringAfter("{").substringBefore("}").split(",")
            process[identifier] = parts
        }

        var result = 0

        val max = 4000

        for (x in 0..max) {
            for (m in 0..max) {
                for (a in 0..max) {
                    for (s in 0..max) {
                        val part = MachinePart(x, m, a, s)
                        var currentProcessIdentifier = "in"
                        while (currentProcessIdentifier !in "AR") {
                            currentProcessIdentifier = process[currentProcessIdentifier]!!.executeProcess(part)
                        }
                        if (currentProcessIdentifier == "A") {
                            result += part.sum()
                        }
                    }
                }
                println(m)
            }
            println(x)
        }

        return result
    }

    fun List<String>.executeProcess(machinePart: MachinePart): String {
        for (i in 0 until this.size - 1) {
            val currentThing = this[i]
            val parameter1 = when (currentThing.substringBefore('<').substringBefore('>')) {
                "x" -> machinePart.x
                "m" -> machinePart.m
                "a" -> machinePart.a
                "s" -> machinePart.s
                else -> {
                    throw Exception("geh scheißn")
                }
            }
            val parameter2 = currentThing.substringAfter('<').substringAfter('>').substringBefore(":").toInt()


            if (currentThing.contains('<')) {
                if (parameter1 < parameter2) {
                    return currentThing.substringAfter(":")
                }
            }else if (currentThing.contains('>')) {
                if (parameter1 > parameter2) {
                    return currentThing.substringAfter(":")
                }
            }
        }
        return this.last()
    }
}

class MachinePart(val x: Int, val m: Int, val a: Int, val s: Int) {
    fun sum():Int {
        return x+m+a+s
    }
    override fun toString(): String {
        return "(x=$x, m=$m, a=$a, s=$s)"
    }
}

class MachinePartP2(val x: IntRange, val m: IntRange, val a: IntRange, val s: IntRange){

}



