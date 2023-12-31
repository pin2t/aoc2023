import java.util.*
import java.util.Collections.singleton

data class PartNumber(val start: Pair<Int, Int>, val end: Pair<Int, Int>, val value: Int)

val scanner = Scanner(System.`in`)
val numbers = HashSet<PartNumber>()
val symbols = HashSet<Pair<Int, Int>>()
val stars = HashSet<Pair<Int, Int>>()
var y = 0
while (scanner.hasNext()) {
    val line = scanner.nextLine()
    line.forEachIndexed { x, c -> if (c != '.' && !c.isDigit()) symbols.add(Pair(x, y)) }
    line.forEachIndexed { x, c -> if (c == '*') stars.add(Pair(x, y)) }
    var x = 0
    while (x < line.length) {
        if (line[x].isDigit()) {
            val start = Pair(x, y)
            x++
            while (x < line.length && line[x].isDigit()) x++
            numbers.add(PartNumber(start, Pair(x - 1, y), line.substring(start.first, x).toInt()))
        }
        x++
    }
    y++
}

fun around(pos: Pair<Int, Int>, symbols: Set<Pair<Int, Int>>): Boolean {
    fun contains(dx: Int, dy: Int): Boolean = symbols.contains(Pair(pos.first + dx, pos.second + dy))
    return contains(-1, -1) || contains(-1, 0) || contains(-1, 1) || contains(0, -1) || contains(0, 1) || contains(1, -1) || contains(1, 0) || contains(1, 1)
}

var ratios = 0
for (star in stars) {
    val naround = numbers.filter { around(it.start, singleton(star)) || around(it.end, singleton(star)) }.toList()
    if (naround.size == 2) {
        ratios += naround[0].value * naround[1].value
    }
}
var sum = numbers.filter { around(it.start, symbols) || around(it.end, symbols) }.sumOf { it.value }
println("$sum $ratios")
