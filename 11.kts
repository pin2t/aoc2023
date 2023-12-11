import java.util.*
import kotlin.math.abs

data class Galaxy(var col: Long, var row: Long) {
    fun distance(other: Galaxy): Long = abs(this.row - other.row) + abs(this.col - other.col)
}
val scanner = Scanner(System.`in`)
val galaxies = ArrayList<Galaxy>()
var rows: Long = 0
while (scanner.hasNext()) {
    scanner.nextLine().forEachIndexed { index, c -> if (c == '#') galaxies.add(Galaxy(index.toLong(), rows)) }
    rows++
}
val cols: Long = galaxies.maxOf { it.col }
val galaxies2 = galaxies.map { it.copy() }
for (r in rows downTo 0) {
    if (galaxies.all { it.row != r }) {
        galaxies.forEach { if (it.row > r) it.row++ }
        galaxies2.forEach { if (it.row > r) it.row+=999999 }
    }
}
for (c in cols downTo 0) {
    if (galaxies.all { it.col != c }) {
        galaxies.forEach { if (it.col > c) it.col++ }
        galaxies2.forEach { if (it.col > c) it.col+=999999 }
    }
}
var sum: Long = 0;
var sum2: Long = 0
for (i in 0..<(galaxies.size - 1)) {
    for (j in i..<galaxies.size) {
        sum += galaxies[i].distance(galaxies[j])
        sum2 += galaxies2[i].distance(galaxies2[j])
    }
}
println(listOf(sum, sum2))
