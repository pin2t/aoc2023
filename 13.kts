import java.util.*
import kotlin.collections.HashSet
import kotlin.math.min

fun reflection(rocks: Set<Pair<Int, Int>>, prev: Int): Int {
    val cols = rocks.maxOf { it.first } + 1; val rows = rocks.maxOf { it.second } + 1
    for (i in 0..<(rows - 1)) {
        val height = min(i + 1, rows - i - 1)
        var reflected = true
        for (c in 0..<cols) {
            for (j in 1..height) {
                if (rocks.contains(Pair(c, i + 1 - j)) != rocks.contains(Pair(c, i + j))) {
                    reflected = false
                    break
                }
            }
        }
        if (reflected && (i + 1) != prev) return i + 1
    }
    return 0
}
val scanner = Scanner(System.`in`)
var result = 0; var result2 = 0
while (scanner.hasNext()) {
    val rocks = HashSet<Pair<Int, Int>>()
    var rows = 0
    while (scanner.hasNext()) {
        val line = scanner.nextLine()
        if (line.isBlank()) break
        line.forEachIndexed { index, c -> if (c == '#') rocks.add(Pair(index, rows)) }
        rows++
    }
    fun ref(rocks: Set<Pair<Int, Int>>, prev: Int): Int {
        var result = 100 * reflection(rocks, prev / 100)
        if (result == 0) result = reflection(rocks.map { Pair(it.second, it.first) }.toSet(), prev % 100)
        return result
    }
    val prev = ref(rocks, 0)
    result += prev
    var other = 0
    for (r in 0..<(rocks.maxOf { it.second } + 1)) {
        for (c in 0..<(rocks.maxOf { it.first } + 1)) {
            val fixed = HashSet(rocks);
            if (!fixed.add(Pair(c, r))) continue
            other = ref(fixed, prev)
            if (other != 0) break
        }
        if (other != 0) break
    }
    for (r in rocks) {
        val fixed = HashSet(rocks);
        fixed.remove(r)
        other = ref(fixed, prev)
        if (other != 0) break
    }
    result2 += other
}
println(listOf(result, result2))
