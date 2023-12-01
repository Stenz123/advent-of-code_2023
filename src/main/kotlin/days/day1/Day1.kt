package days.day1

import days.Day

class Day1 : Day(false) {
    override fun partOne(): Any {
        val inputNumbers = readInput().map { it.filter (Char::isDigit) }
        return inputNumbers.sumOf {
            (it.first() +""+ it.last()).toInt()
        }
    }

    override fun partTwo(): Any {
        val inputDigits = readInput().map { convertStringWithSpelledOutDigitsToDigits(it) }
        val reversedInputDigits = readInput().map { convertStringWithSpelledOutDigitsToDigitsBackwards(it) }

        val numbers = inputDigits.map { it.filter (Char::isDigit) }
        val reversedNumbers = reversedInputDigits.map { it.filter (Char::isDigit) }

        val firstDigitString = numbers.map { it.first().toString() }
        val firstDigitFromBackString = reversedNumbers.map { it.last().toString() }

        return firstDigitString.zip(firstDigitFromBackString).sumOf { (it.first + it.second).toInt() }
    }

    private fun convertStringWithSpelledOutDigitsToDigits(input: String): String {
        var sequence = ""
        for (char in input) {
            val sequenceBuilder = StringBuilder(sequence)
            sequenceBuilder.append(char)
            sequence = sequenceBuilder.toString()
            val oldSequence = sequence
            sequence = convertStringWithSpelledOutDigitsNoOrder(sequence)
            if (oldSequence != sequence) {
                return sequence
            }
        }
        return sequence
    }

    private fun convertStringWithSpelledOutDigitsToDigitsBackwards(input: String): String {
        var sequence = ""
        for (i in input.length - 1 downTo 0) {
            val char = input[i]
            val sequenceBuilder = StringBuilder()
            sequenceBuilder.append(char)
            sequenceBuilder.append(sequence)
            sequence = sequenceBuilder.toString()
            val oldSequence = sequence
            sequence = convertStringWithSpelledOutDigitsNoOrder(sequence)
            if (oldSequence != sequence) {
                return sequence
            }
        }
        return sequence
    }

    private fun convertStringWithSpelledOutDigitsNoOrder(input: String): String {
        return input.replace("one", "1")
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
