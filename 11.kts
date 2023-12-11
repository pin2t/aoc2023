import java.util.*
import kotlin.math.abs

data class Galaxy(var col: Long, var row: Long)
val scanner = Scanner(System.`in`)
val galaxies = ArrayList<Galaxy>()
val galaxies2 = ArrayList<Galaxy>()
var rows: Long = 0
var cols: Long = 0
while (scanner.hasNext()) {
    val line = scanner.nextLine()
    line.forEachIndexed { index, c ->
        if (c == '#') {
            galaxies.add(Galaxy(index.toLong(), rows))
            galaxies2.add(Galaxy(index.toLong(), rows))
        }
    }
    cols = line.length.toLong()
    rows++
}
var r = rows - 1
while (r >= 0) {
    if (galaxies.all { it.row != r }) {
        galaxies.forEach { if (it.row > r) it.row++ }
        galaxies2.forEach { if (it.row > r) it.row+=999999 }
        rows++
    }
    r--
}
var c  = cols - 1
while (c >= 0) {
    if (galaxies.all { it.col != c }) {
        galaxies.forEach { if (it.col > c) it.col++ }
        galaxies2.forEach { if (it.col > c) it.col+=999999 }
        cols++
    }
    c--
}
var sum: Long = 0
var sum2: Long = 0
for (i in 0..<(galaxies.size - 1)) for (j in i..<galaxies.size) {
    val s = abs(galaxies[i].row - galaxies[j].row) + abs(galaxies[i].col - galaxies[j].col)
    sum += s
    sum2 += abs(galaxies2[i].row - galaxies2[j].row) + abs(galaxies2[i].col - galaxies2[j].col)
}
println(listOf(sum, sum2))
