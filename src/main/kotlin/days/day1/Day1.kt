package days.day1

import days.Day

class Day1 : Day(false) {
    override fun partOne(): Any {
        val input = readInput()

        //find first number in string
        val numbers = input.map { it.filter { c -> c.isDigit() } }
        val fisar = numbers.map {
            val string: String = it.first() + "" + it.last()
            string.toInt()
        }

        val sum = fisar.sum()


        return sum
    }

    override fun partTwo(): Any {
        val input = readInput().map {convertStringWithSpelledOutDigitsToStringWithDigits(it) }
val optherINput = readInput().map { convertStringWithSpelledOutDigitsToStringWithDigitsBackwards(it) }
        //find first number in string
        val numbers = input.map { it.filter { c -> c.isDigit() } }
        val otherNumbers = optherINput.map { it.filter { c -> c.isDigit() } }
        val fisar = numbers.map {
            it.first().toString()
        }
        val otherFisar = otherNumbers.map {
            it.last().toString()
        }
        val sum = fisar.zip(otherFisar).map { (it.first + it.second).toInt() }.sum()


        return sum
    }


    fun convertStringWithSpelledOutDigitsToStringWithDigits(string: String): String {
        //start reading the string from left while parsing
        var sequence = ""
        for (i in 0..<string.length) {
            val char = string[i]
            val sequenceBuilder = StringBuilder(sequence)
            sequenceBuilder.append(char)
            sequence= sequenceBuilder.toString()
            val oldSequence = sequence
            sequence = convertStringWithSpelledOutDigitsToStringWithDigitsWIthout(sequence)
            if (oldSequence != sequence) {
                return sequence
            }

        }
        return sequence
    }

    fun convertStringWithSpelledOutDigitsToStringWithDigitsBackwards(string: String): String {
        //start reading the string from right while parsing
        var sequence = ""
        for (i in string.length - 1 downTo 0) {
            val char = string[i]
            val sequenceBuilder = StringBuilder()
            sequenceBuilder.append(char)
            sequenceBuilder.append(sequence)
            sequence= sequenceBuilder.toString()
            val oldSequence = sequence
            sequence = convertStringWithSpelledOutDigitsToStringWithDigitsWIthout(sequence)
            if (oldSequence != sequence) {
                return sequence
            }

        }
        return sequence
    }

    fun convertStringWithSpelledOutDigitsToStringWithDigitsWIthout(string: String): String {
        return string.replace("one", "1")
            .replace("two", "2")
            .replace("three", "3")
            .replace("four", "4")
            .replace("five", "5")
            .replace("six", "6")
            .replace("seven", "7")
            .replace("eight", "8")
            .replace("nine", "9")
            .replace("zero", "0")
    }
}





