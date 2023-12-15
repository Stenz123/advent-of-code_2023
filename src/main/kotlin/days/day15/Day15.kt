package days.day15

import days.Day

class Day15 : Day(false) {
    override fun partOne(): Any {
        return readInput()[0].split(",").sumOf { inp ->
            var currentValue = 0
            inp.forEach { c ->
                currentValue += c.code
                currentValue *= 17
                currentValue %= 256
            }
            currentValue
        }

    }

    override fun partTwo(): Any {
        val boxes: HashMap<Int, MutableList<String>> = HashMap()

        readInput()[0].split(",").forEach { inp ->
            var hash = 0
            inp.substringBefore("=").substringBefore("-").forEach { c ->
                hash += c.code
                hash *= 17
                hash %= 256
            }
            if (inp.contains('-')) {
                boxes[hash]?.removeIf { it.contains(inp.substringBefore("-")) }
            } else if (inp.contains("=")) {
                if (boxes.containsKey(hash)) {
                    val indexOfElement = boxes[hash]!!.indexOfFirst { it.substringBefore("=") == inp.substringBefore("=") }
                    if (indexOfElement == -1) {
                        boxes[hash]?.add(inp)
                    } else {
                        boxes[hash]?.set(indexOfElement, inp)
                    }
                } else {
                    boxes[hash] = mutableListOf(inp)
                }
            }
        }

        var result = 0
        for (i in 0..255) {
            if (boxes.containsKey(i)) {
                boxes[i]?.forEachIndexed { j, label ->
                    result += (i + 1) * (j + 1) * (label.substringAfter("=").toInt())
                }
            }
        }
        return result
    }
}

