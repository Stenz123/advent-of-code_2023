package days.day25

import days.Day

class Day25 : Day() {
    override fun partOne(): Any {
        val vertices = mutableSetOf<String>()
        val edges = mutableListOf<Pair<String, String>>()

        readInput().forEach {
            val firstComponent = it.substringBefore(": ")
            vertices.add(firstComponent)
            it.substringAfter(": ").split(" ").forEach { secondComponent ->
                vertices.add(secondComponent)
                val edge = firstComponent to secondComponent
                val reversedEdge = secondComponent to firstComponent
                if (edge !in edges && reversedEdge !in edges) edges.add(edge)
            }
        }


        var groups:MutableList<MutableList<String>>
        do {
            groups = vertices.map { mutableListOf(it) }.toMutableList()

            while (groups.size > 2) {

                val random = edges.random()
                val subset1 = groups.first { it.contains(random.first) }
                val subset2 = groups.first { it.contains(random.second) }

                if (subset1 == subset2) continue

                groups.remove(subset2)
                subset1.addAll(subset2)
            }
        } while (countCuts(groups.toList(), edges) != 3)

        return groups.fold(1) { acc, s -> acc * s.size }
    }

    private fun countCuts(subsets: List<List<String>>, edges:List<Pair<String,String>>): Int {
        var cuts = 0
        edges.forEach { edge ->
            val subset1 = subsets.first { it.contains(edge.first) }
            val subset2 = subsets.first { it.contains(edge.second) }
            if (subset1 != subset2) cuts++
        }
        return cuts
    }

    override fun partTwo(): Any {
        return "day 25 part 2 not Implemented"
    }

}
