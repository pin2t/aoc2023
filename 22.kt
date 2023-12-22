import kotlin.math.max
import kotlin.math.min

val bricks = ArrayList<Brick>()

data class Brick (val s: Triple<Int, Int, Int>, val e: Triple<Int, Int, Int>) {
    fun intersect(other: Brick): Boolean {
        return max(s.first, other.s.first) <= min(e.first, other.e.first) &&
                max(s.second, other.s.second) <= min(e.second, other.e.second) &&
                max(s.third, other.s.third) <= min(e.third, other.e.third)
    }

    fun canFall(bricks: ArrayList<Brick>): Boolean = bricks.all { it == this || !it.intersect(fall) } && s.third > 1
    val canFall: Boolean get() = canFall(bricks)
    val fall: Brick get() = Brick(Triple(s.first, s.second, s.third - 1), Triple(e.first, e.second, e.third - 1))
}

val number = Regex("-?\\d+")

fun main() {
    while (true) {
        val line = readlnOrNull() ?: break
        val numbers = number.findAll(line).map { it.value.toInt() }.toList()
        bricks.add(Brick(Triple(numbers[0], numbers[1], numbers[2]),
            Triple(numbers[3], numbers[4], numbers[5])))
    }
    bricks.sortBy { it.s.third }
    for (i in 0..<bricks.size) {
        while (bricks[i].canFall) bricks[i] = bricks[i].fall
    }
    var result1 = 0; var result2 = 0
    for (i in 0..<bricks.size) {
        val save = bricks[i]
        bricks[i] = Brick(Triple(-1, -1, -1), Triple(-1, -1, -1))
        if (i == bricks.size - 1) {
            result1++
        } else if (!bricks.subList(i + 1, bricks.size).any { it.canFall }) {
            result1++
        } else {
            val fall = ArrayList(bricks)
            for (j in (i + 1)..<fall.size) {
                if (fall[j].canFall(fall)) {
                    result2++
                    while (fall[j].canFall(fall)) fall[j] = fall[j].fall
                }
            }
        }
        bricks[i] = save
    }
    println(listOf(result1, result2))
}
