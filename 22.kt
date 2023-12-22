val bricks = ArrayList<Brick>()

data class Brick (val s: Triple<Int, Int, Int>, val e: Triple<Int, Int, Int>) {
    fun contains(p: Triple<Int, Int, Int>): Boolean {
        return p.first >= s.first && p.first <= e.first &&
                p.second >= s.second && p.second <= e.second &&
                p.third >= s.third && p.third <= e.third
    }

    fun intersect(other: Brick): Boolean {
        if (other.s.third > e.third || other.e.third < s.third) return false
        for (x in other.s.first..other.e.first) {
            for (y in other.s.second..other.e.second) {
                for (z in other.s.third..other.e.third) {
                    if (contains(Triple(x, y, z))) return true
                }
            }
        }
        return false
    }

    fun canFall(bricks: ArrayList<Brick>): Boolean {
        val f = fallen
        for (b in bricks) {
            if (b != this && b.intersect(f)) return false
        }
        return s.third > 1
    }

    val canFall: Boolean get() = canFall(bricks)
    val fallen: Brick get() = Brick(Triple(s.first, s.second, s.third - 1), Triple(e.first, e.second, e.third - 1))
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
        while (bricks[i].canFall) bricks[i] = bricks[i].fallen
    }
    var result1 = 0; var result2 = 0
    for (i in 0..<bricks.size) {
        val save = bricks[i]
        bricks[i] = Brick(Triple(-1, -1, -1), Triple(-1, -1, -1))
        if (bricks.all { !it.canFall }) {
            result1++
        } else {
            val fallen = ArrayList(bricks)
            for (j in (i+1)..<fallen.size) {
                if (fallen[j].canFall(fallen)) {
                    result2++
                    while (fallen[j].canFall(fallen)) fallen[j] = fallen[j].fallen
                }
            }
        }
        bricks[i] = save
    }
    println(listOf(result1, result2))
}
