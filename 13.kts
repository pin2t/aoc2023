import java.util.*
import kotlin.collections.HashSet
import kotlin.math.min

data class Grid(val rocks: HashSet<Pair<Int, Int>>) {
    fun cols(): Int = rocks.maxOf { it.first } + 1
    fun rows(): Int = rocks.maxOf { it.second } + 1

    fun colRef(prev: Int): Int {
        for (i in 0..<(cols() - 1)) {
            val width = min(i + 1, cols() - i - 1)
            var reflected = true
            for (r in 0..<rows()) {
                for (j in 1..width) {
                    if (rocks.contains(Pair(i + 1 - j, r)) != rocks.contains(Pair(i + j, r))) {
                        reflected = false
                        break
                    }
                }
            }
            if (reflected && (i + 1) != prev % 100) return i + 1
        }
        return 0
    }

    fun rowRef(prev: Int): Int {
        for (i in 0..<(rows() - 1)) {
            val height = min(i + 1, rows() - i - 1)
            var reflected = true
            for (c in 0..<cols()) {
                for (j in 1..height) {
                    if (rocks.contains(Pair(c, i + 1 - j)) != rocks.contains(Pair(c, i + j))) {
                        reflected = false
                        break
                    }
                }
            }
            if (reflected && (i + 1) != prev / 100) return i + 1
        }
        return 0
    }

    fun reflection(prev: Int): Int {
        return colRef(prev) + rowRef(prev) * 100
    }
}
val scanner = Scanner(System.`in`)
var result = 0
var result2 = 0
while (scanner.hasNext()) {
    val rocks = HashSet<Pair<Int, Int>>()
    var rows = 0
    while (scanner.hasNext()) {
        val line = scanner.nextLine()
        if (line.isBlank()) break
        line.forEachIndexed { index, c -> if (c == '#') rocks.add(Pair(index, rows)) }
        rows++
    }
    val grid = Grid(rocks)
    result += grid.reflection(0)
    fun otherReflection(): Int {
        val prev = grid.reflection(0)
        for (r in 0..<grid.rows()) {
            for (c in 0..<grid.cols()) {
                val fixed = HashSet(grid.rocks); fixed.add(Pair(c, r))
                val or = Grid(fixed).reflection(prev)
                if (or != 0 && or != prev) {
                    if (or / 100 != prev / 100) return or - (or % 100)
                    if (or % 100 != prev % 100) return or % 100
                }
            }
        }
        for (r in grid.rocks) {
            val fixed = HashSet(grid.rocks);
            fixed.remove(r)
            val or = Grid(fixed).reflection(prev)
            if (or != 0 && or != prev) {
                if (or / 100 != prev / 100) return or - (or % 100)
                if (or % 100 != prev % 100) return or % 100
            }
        }
        return 0
    }
    result2 += otherReflection()
}
println(listOf(result, result2))
