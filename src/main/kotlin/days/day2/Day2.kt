package days.day2

import days.Day

class Day2: Day(false) {
    override fun partOne(): Any {
        val games = readInput().map{
            val id = it.substringBefore(":").substringAfter(" ").toInt()

            val values = it.substringAfter(": ").replace(";",",").split(", ").map{ it ->
                val color = it.substringAfter(" ").toLowerCase()
                val value = it.substringBefore(" ").toInt()
                ColorValue(color, value)
            }
            Game(id, values)
        }

        val validGames = games.filter { game ->
            game.value.all { color ->
                (color.color != "red" || color.value <= 12) &&
                (color.color != "green" || color.value <= 13) &&
                (color.color != "blue" || color.value <= 14)
            }
        }

        return validGames.sumOf { it.id }
    }

    override fun partTwo(): Any {
        val games = readInput().map{
            val id = it.substringBefore(":").substringAfter(" ").toInt()

            val values = it.substringAfter(": ").replace(";",",").split(", ").map{ it ->
                val color = it.substringAfter(" ").toLowerCase()
                val value = it.substringBefore(" ").toInt()
                ColorValue(color, value)
            }
            Game(id, values)
        }

        val map: MutableMap<Game, ArrayList<ColorValue>> = mutableMapOf()

        for (game in games) {
            map[game] = arrayListOf()

            map[game]?.add(ColorValue("red",game.value.filter { it.color == "red" }.map { it.value }.max()))
            map[game]?.add(ColorValue("green",game.value.filter { it.color == "green" }.map { it.value }.max()))
            map[game]?.add(ColorValue("blue",game.value.filter { it.color == "blue" }.map { it.value }.max()))
        }

        return map.map { it.value.map { it.value }.reduce{ acc, i ->  acc * i } }.sum()

    }
}


class Game(val id:Int, val value:List<ColorValue>){
    override fun toString(): String {
        return "Game(id=$id, value=$value)"
    }
}
class ColorValue(val color:String, val value:Int){
    override fun toString(): String {
        return "ColorValue(color='$color', value=$value)"
    }
}
