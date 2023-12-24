import kotlin.math.abs

data class Pos3D(val x: Long, val y: Long, val z: Long)
data class Hailstone(val pos: Pos3D, val velocity: Pos3D)

class Day24 {
    var hailstones = ArrayList<Hailstone>()
    val number = Regex("-?\\d+")

    fun run() {
        while (true) {
            val line = readlnOrNull() ?: break
            val items = number.findAll(line).map { it.value.toLong() }.toList()
            hailstones.add(Hailstone(Pos3D(items[0], items[1], items[2]),
                Pos3D(items[3], items[4], items[5])))
        }
        var result1 = 0
        for (i in 0..<(hailstones.size - 1)) {
            for (j in (i + 1)..<hailstones.size) {
                val h1 = hailstones[i]; val h2 = hailstones[j]
                if (abs(h1.velocity.x) == abs(h2.velocity.x) && abs(h1.velocity.y) == abs(h2.velocity.y))
                    continue  // parallel
                if (h1.velocity.x * h2.velocity.y == h1.velocity.y * h2.velocity.x) continue
                val t: Long = ((h2.pos.x - h1.pos.x) * h2.velocity.y + (h1.pos.y - h2.pos.y) * h2.velocity.x) /
                        (h1.velocity.x * h2.velocity.y - h1.velocity.y * h2.velocity.x)
                if (t < 0) continue // in the past for 1
                val s: Long = ((h1.pos.y - h2.pos.y) + t * h1.velocity.y) / h2.velocity.y
                if (s < 0) continue // in the past for 2
                if ((h1.pos.x + h1.velocity.x * t) in 200000000000000L..400000000000000L &&
                    (h1.pos.y + h1.velocity.y * t) in 200000000000000L..400000000000000L) {
                    result1++
                }
            }
        }
        println(result1)
        // print equations to solve using online solver
        for (i in 0..10) {
            val h = hailstones[i]
            println("(x - ${h.pos.x}) * (${h.velocity.y} - vy) - (y - ${h.pos.y}) * (${h.velocity.x} - vx)")
            println("(y - ${h.pos.y}) * (${h.velocity.z} - vz) - (z - ${h.pos.z}) * (${h.velocity.y} - vy)")
        }
    }
}

fun main() { Day24().run() }
