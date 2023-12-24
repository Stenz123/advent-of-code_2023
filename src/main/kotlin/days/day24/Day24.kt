package days.day24

import days.Day

class Day24 : Day() {
    override fun partOne(): Any {
        val testArea = 200000000000000.0..400000000000000.0
        val vectors = readInput().map {
            val split = it.split(" @ ").map { it.split(", ").map(String::toDouble) }
            val point = Point(split.first()[0], split.first()[1], 0.0)
            val velocity = Velocity(split.last()[0], split.last()[1], 0.0)
            Vector3(point, velocity)
        }
        return vectors.cartesianProduct().count { combination ->
            val collision = Vector3.collision2D(combination.first, combination.second)
            collision != null && collision.x in testArea && collision.y in testArea
        }
    }

    override fun partTwo(): Any {
        return "day 24 part 2 not Implemented"
    }
}

// Extensions
fun <T> List<T>.cartesianProduct(): List<Pair<T, T>> {
    val result = mutableListOf<Pair<T, T>>()

    for (i in indices) {
        for (j in i + 1 until size) {
            val pair = Pair(get(i), get(j))
            val reversePair = Pair(get(j), get(i))

            if (!result.contains(pair) && !result.contains(reversePair)) {
                result.add(pair)
            }
        }
    }

    return result
}


class Vector3(val point: Point, val velocity: Velocity) {
    override fun toString() = "$point $velocity"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vector3

        if (point != other.point) return false
        if (velocity != other.velocity) return false

        return true
    }

    override fun hashCode(): Int {
        var result = point.hashCode()
        result = 31 * result + velocity.hashCode()
        return result
    }
    fun addVelocity() = Point(point.x + velocity.x, point.y + velocity.y, point.z + velocity.z)

    companion object {
        fun collision2D(vector1: Vector3, vector2: Vector3): Point? {
            val secondPoint1 = vector1.addVelocity()
            val k1 = calculateSlope(vector1.point, secondPoint1)
            val d1 = calculateYIntercept(vector1.point, k1)

            val secondPoint2 = vector2.addVelocity()
            val k2 = calculateSlope(vector2.point, secondPoint2)
            val d2 = calculateYIntercept(vector2.point, k2)

            val intersect =  findIntersectionPoint(k1, d1, k2, d2)

            if (vector1.velocity.x > 0) {
                if (intersect.x < vector1.point.x) return null
            } else {
                if (intersect.x >  vector1.point.x) return null
            }

            if (vector2.velocity.x > 0) {
                if (intersect.x < vector2.point.x) return null
            } else {
                if (intersect.x >  vector2.point.x) return null
            }

            return intersect
        }

        fun findIntersectionPoint(k1: Double, d1: Double, k2: Double, d2: Double): Point {
            // Solve for x: k1x + d1 = k2x + d2
            val x = (d2 - d1) / (k1 - k2)

            // Substitute x into either function to find y
            val y = k1 * x + d1

            return Point(x, y, 0.0)
        }

        fun calculateSlope(point1: Point, point2: Point): Double {
            val deltaY = point2.y - point1.y
            val deltaX = point2.x - point1.x

            // Avoid division by zero
            if (deltaX == 0.0) {
                throw IllegalArgumentException("The slope is undefined for vertical lines.")
            }

            return deltaY / deltaX
        }

        fun calculateYIntercept(point: Point, slope: Double): Double {
            return point.y - slope * point.x
        }
    }
}


class Point(val x: Double, val y: Double, val z: Double) {
    override fun toString() = "P($x, $y, $z)"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Point
        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false
        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }

}

class Velocity(val x: Double, val y: Double, val z: Double) {
    override fun toString() = "V($x, $y, $z)"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Velocity

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }

}
