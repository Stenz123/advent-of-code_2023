package days.day7
import days.Day

class Day7 : Day(false) {


    override fun partOne(): Any {
        val hands: MutableMap<Hand, Int> = readInput().map { line ->
            val cards = line.split(" ")[0].map { Card(it) }
            val hand = Hand(cards)
            hand to line.split(" ")[1].toInt()
        }.toMap().toMutableMap()

        val linkedHands = hands.keys.sorted()

        for (i in linkedHands.indices) {
            hands[linkedHands[i]] = hands[linkedHands[i]]!! * (i + 1)
        }

        return hands.keys.sumBy { hands[it]!! }
    }

    override fun partTwo(): Any {
        return "Part one is part two"
    }

    companion object {
        var jokerCache: MutableMap<Hand, Hand> = mutableMapOf()
    }

}

class Hand(val cards: List<Card>) : Comparable<Hand> {
    override fun compareTo(other: Hand): Int {
        val thisJokerTransformedCard = transformCardToHighestPossibleWithJoker(this)
        val otherJokerTransformedCard = transformCardToHighestPossibleWithJoker(other)

        if (cards.size != other.cards.size) {
            throw Exception("Hands are not the same size")
        }

        // Five of a kind
        if (thisJokerTransformedCard.cards.distinct().size == 1 && otherJokerTransformedCard.cards.distinct().size == 1) {
            return compareEqualValuedHands(this, other)
        } else if (thisJokerTransformedCard.cards.distinct().size == 1) {
            return 1
        } else if (otherJokerTransformedCard.cards.distinct().size == 1) {
            return -1
        }

        // Four of a kind
        if (thisJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 4 }
                .isNotEmpty() && otherJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 4 }
                .isNotEmpty()) {
            return compareEqualValuedHands(this, other)
        } else if (thisJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 4 }.isNotEmpty()) {
            return 1
        } else if (otherJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 4 }.isNotEmpty()) {
            return -1
        }

        // Full house
        if (thisJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 3 }
                .isNotEmpty() && thisJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 2 }
                .isNotEmpty() && otherJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 3 }
                .isNotEmpty() && otherJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 2 }
                .isNotEmpty()) {
            return compareEqualValuedHands(this, other)
        } else if (thisJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 3 }.isNotEmpty() && thisJokerTransformedCard.cards.groupBy { it.value }
                .filter { it.value.size == 2 }.isNotEmpty()) {
            return 1
        } else if (otherJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 3 }
                .isNotEmpty() && otherJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 2 }
                .isNotEmpty()) {
            return -1
        }

        // Three of a kind
        if (thisJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 3 }
                .isNotEmpty() && otherJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 3 }
                .isNotEmpty()) {
            return compareEqualValuedHands(this, other)
        } else if (thisJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 3 }.isNotEmpty()) {
            return 1
        } else if (otherJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 3 }.isNotEmpty()) {
            return -1
        }

        // Two pair
        if (thisJokerTransformedCard.cards.groupBy { it.value }
                .filter { it.value.size == 2 }.size == 2 && otherJokerTransformedCard.cards.groupBy { it.value }
                .filter { it.value.size == 2 }.size == 2) {
            return compareEqualValuedHands(this, other)
        } else if (thisJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 2 }.size == 2) {
            return 1
        } else if (otherJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 2 }.size == 2) {
            return -1
        }

        // Pair
        if (thisJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 2 }
                .isNotEmpty() && otherJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 2 }
                .isNotEmpty()) {
            return compareEqualValuedHands(this, other)
        } else if (thisJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 2 }.isNotEmpty()) {
            return 1
        } else if (otherJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 2 }.isNotEmpty()) {
            return -1
        }

        // High card
        return compareEqualValuedHands(this, other)
    }

    fun compareWithoutJoker(first: Hand, other: Hand): Int {

        val thisJokerTransformedCard = (first)
        val otherJokerTransformedCard = (other)

        if (cards.size != other.cards.size) {
            throw Exception("Hands are not the same size")
        }

        // Five of a kind
        if (thisJokerTransformedCard.cards.distinct().size == 1 && otherJokerTransformedCard.cards.distinct().size == 1) {
            return compareEqualValuedHands(this, other)
        } else if (cards.distinct().size == 1) {
            return 1
        } else if (otherJokerTransformedCard.cards.distinct().size == 1) {
            return -1
        }

        // Four of a kind
        if (thisJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 4 }
                .isNotEmpty() && otherJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 4 }
                .isNotEmpty()) {
            return compareEqualValuedHands(this, other)
        } else if (thisJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 4 }.isNotEmpty()) {
            return 1
        } else if (otherJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 4 }.isNotEmpty()) {
            return -1
        }

        // Full house
        if (thisJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 3 }
                .isNotEmpty() && thisJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 2 }
                .isNotEmpty() && otherJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 3 }
                .isNotEmpty() && otherJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 2 }
                .isNotEmpty()) {
            return compareEqualValuedHands(this, other)
        } else if (thisJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 3 }.isNotEmpty() && thisJokerTransformedCard.cards.groupBy { it.value }
                .filter { it.value.size == 2 }.isNotEmpty()) {
            return 1
        } else if (otherJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 3 }
                .isNotEmpty() && otherJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 2 }
                .isNotEmpty()) {
            return -1
        }

        // Three of a kind
        if (thisJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 3 }
                .isNotEmpty() && otherJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 3 }
                .isNotEmpty()) {
            return compareEqualValuedHands(this, other)
        } else if (thisJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 3 }.isNotEmpty()) {
            return 1
        } else if (otherJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 3 }.isNotEmpty()) {
            return -1
        }

        // Two pair
        if (thisJokerTransformedCard.cards.groupBy { it.value }
                .filter { it.value.size == 2 }.size == 2 && otherJokerTransformedCard.cards.groupBy { it.value }
                .filter { it.value.size == 2 }.size == 2) {
            return compareEqualValuedHands(this, other)
        } else if (thisJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 2 }.size == 2) {
            return 1
        } else if (otherJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 2 }.size == 2) {
            return -1
        }

        // Pair
        if (thisJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 2 }
                .isNotEmpty() && otherJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 2 }
                .isNotEmpty()) {
            return compareEqualValuedHands(this, other)
        } else if (thisJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 2 }.isNotEmpty()) {
            return 1
        } else if (otherJokerTransformedCard.cards.groupBy { it.value }.filter { it.value.size == 2 }.isNotEmpty()) {
            return -1
        }

        // High card
        return compareEqualValuedHands(this, other)
    }

    override fun toString(): String {
        return "Hand($cards)"
    }

    private fun transformCardToHighestPossibleWithJoker(hand: Hand): Hand {
        if (hand.cards == listOf(Card('J'), Card('J'), Card('J'), Card('J'), Card('J'))) {
            return Hand(listOf(Card('A'), Card('A'), Card('A'), Card('A'), Card('A')))
        }
        if (hand in Day7.jokerCache) {
            return Day7.jokerCache[hand]!!
        }

        if (hand.cards.none { it.value == 'J' }) {
            return hand
        }

        val jIndexes = mutableListOf<Int>()
        for (i in hand.cards.indices) {
            if (hand.cards[i].value == 'J') {
                jIndexes.add(i)
            }
        }

        val allCombinations = linkedSetOf<Hand>()
        val combinationArray = generateCombinations(jIndexes.size)

        for (combination in combinationArray) {
            val newHand = hand.cards.toMutableList()
            for (index in jIndexes) {
                newHand[index]=Card(combination[jIndexes.indexOf(index)])
            }
            allCombinations.add(Hand(newHand))
        }

        val result = allCombinations.sortedWith(withoutJokerComperator()).last()
        Day7.jokerCache[hand] = result
        return result
    }
}



fun generateCombinations(length: Int): List<List<Char>> {
    val values = listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A')

    // Base case: If the length is 0, return a list containing an empty list
    if (length == 0) {
        return listOf(emptyList())
    }

    // Recursive case: Generate combinations for the previous length
    val prevCombinations = generateCombinations(length - 1)

    // Build new combinations by appending each value to the existing combinations
    return values.flatMap { value ->
        prevCombinations.map { combination -> listOf(value) + combination }
    }
}

private fun compareEqualValuedHands(hand: Hand, other: Hand): Int {
    for (i in 0 until hand.cards.size) {
        if (hand.cards[i].compareTo(other.cards[i]) != 0) {
            val result = hand.cards[i].compareTo(other.cards[i])
            return result
        }
    }
    return 0
}



class withoutJokerComperator : Comparator<Hand> {
    override fun compare(o1: Hand, o2: Hand): Int {
        return o1.compareWithoutJoker(o1, o2)
    }
}

class Card(val value: Char) : Comparable<Card> {
    override fun compareTo(other: Card): Int {
        val values = listOf( 'J','2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A')
        return (values.indexOf(value) - values.indexOf(other.value))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Card

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun toString(): String {
        return "Card($value)"
    }


}


