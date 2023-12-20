package days.day20

import days.Day
import java.util.*
import kotlin.math.floor

class Day20 : Day() {
    override fun partOne(): Any {
        val modules = parseInput()
        val broadCaster = modules.first { it.identifier == "broadcaster" }

        var numberOfHighSignals = 0
        var numberOfLowSignals = 0

        var isInitialState = false

        val loopDict = hashMapOf<Int, Pair<Int, Int>>()
        var loopCount = 0

        val rxMachineDict = HashMap<String, Int>()
        modules.filterIsInstance<Conjunction>()
            .first {
                it.identifier == "kl"
            }.dict.keys.forEach {
                rxMachineDict[it] = -1
            }


        while (!isInitialState) {
            val queue: Queue<Triple<Module, Boolean, String>> = LinkedList()
            queue.add(Triple(broadCaster, false, "button"))
            numberOfLowSignals++
            while (queue.isNotEmpty()) {
                val current = queue.remove()
                if (current.first.identifier == "rx" && !current.second) {
                    return loopCount
                }

                val newSignals = current.first.sendSignal(current.second, current.third)
                queue.addAll(newSignals)

                newSignals.forEach {
                    if (rxMachineDict.containsKey(it.first.identifier) && !it.second && loopCount != 0) {
                        rxMachineDict[it.first.identifier] = loopCount+1
                        if (rxMachineDict.values.none { it == -1 }) {
                            return lcm(rxMachineDict.values.map { it.toLong() }.toLongArray())
                        }
                    }

                }

                if (newSignals.isNotEmpty() && newSignals.first().second) {
                    numberOfHighSignals += newSignals.size
                } else {
                    numberOfLowSignals += newSignals.size
                }
            }
            isInitialState = modules.all { it.isInInitialState() }
            loopCount++
            loopDict[loopCount] = numberOfLowSignals to numberOfHighSignals
            println(loopCount)
        }
        var lowSignals = numberOfLowSignals * floor(1000.0 / loopCount).toInt()
        var highSignals = numberOfHighSignals * floor(1000.0 / loopCount).toInt()
        val rest = 1000 % loopCount
        if (rest > 0) {
            lowSignals += loopDict[rest]!!.first
            highSignals += loopDict[rest]!!.second
        }
        return lowSignals * highSignals
    }

    override fun partTwo(): Any {
        return "day 20 part 2 not Implemented"
    }

    fun parseInput(): List<Module> {
        val modules = mutableListOf<Module>()
        val rawInput = readInput()
        rawInput.forEach {
            val identifier = it.substringBefore(" ->").removePrefixes()
            if (it.contains("broadcaster")) {
                modules.add(BroadCaster(identifier))
            } else if (it.contains("%")) {
                modules.add(FlipFlop(identifier))
            } else if (it.contains("&")) {
                modules.add(Conjunction(identifier))
            }
        }
        modules.forEach { module ->
            val moduleString =
                rawInput.first { line -> line.substringBefore(" ->").removePrefixes() == module.identifier }
            val nextModulesSplit = moduleString.substringAfter("-> ").split(", ")
            val currentModule = modules.first { it.identifier == moduleString.substringBefore(" ->").removePrefixes() }
            nextModulesSplit.forEach { nextModuleId ->
                val nextModule = modules.firstOrNull { it.identifier == nextModuleId }
                if (nextModule != null) {
                    currentModule.connections.add(nextModule)
                } else {
                    currentModule.connections.add(OutputModule(nextModuleId))
                }
            }
        }
        modules.filterIsInstance<Conjunction>().forEach { module ->
            val inputModules = modules.filter { it.connections.contains(module) }
            inputModules.forEach { inputModel ->
                module.addInputModule(inputModel.identifier)
            }
        }
        return modules
    }
}

abstract class Module(val identifier: String) {
    val connections: MutableList<Module> = mutableListOf()
    abstract fun sendSignal(signal: Boolean, sender: String): List<Triple<Module, Boolean, String>>
    internal fun prepareSending(signal: Boolean, sender: String): List<Triple<Module, Boolean, String>> {
        val res = mutableListOf<Triple<Module, Boolean, String>>()
        for (connection in connections) {
            res.add(Triple(connection, signal, sender))
        }
        return res
    }

    abstract fun isInInitialState(): Boolean
    override fun toString(): String {
        return "$identifier -> ${connections.map(Module::identifier)}"
    }

}


class FlipFlop(identifier: String) : Module(identifier) {

    private var state = false

    override fun sendSignal(signal: Boolean, sender: String): List<Triple<Module, Boolean, String>> {
        if (!signal) { //if signal low
            state = !state
            return prepareSending(state, identifier)
        }
        return listOf()
    }

    override fun isInInitialState() = !state
    override fun toString(): String {
        return "%" + super.toString()
    }

}

class Conjunction(identifier: String) : Module(identifier) {
    val dict: HashMap<String, Boolean> = hashMapOf()
    override fun sendSignal(signal: Boolean, sender: String): List<Triple<Module, Boolean, String>> {
        if (!dict.containsKey(sender)) throw Exception("des woa illegal")
        dict[sender] = signal
        return prepareSending(!dict.values.all { it }, identifier) //if all in dict are true return true
    }

    override fun isInInitialState() = dict.values.all { !it }
    fun addInputModule(sender: String) {
        dict[sender] = false
    }

    override fun toString(): String {
        return "&" + super.toString()
    }
}

class BroadCaster(identifier: String) : Module(identifier) {
    override fun sendSignal(signal: Boolean, sender: String): List<Triple<Module, Boolean, String>> {
        return prepareSending(signal, identifier)
    }

    override fun isInInitialState() = true
}

class OutputModule(identifier: String) : Module(identifier) {
    override fun sendSignal(signal: Boolean, sender: String): List<Triple<Module, Boolean, String>> = listOf()
    override fun isInInitialState() = true

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

fun String.removePrefixes() = this.replace("%", "").replace("&", "")
