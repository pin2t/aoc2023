import java.util.*
import kotlin.collections.ArrayList

data class PartNumber(val start: Pair<Int, Int>, val end: Pair<Int, Int>, val value: Int)

val scanner = Scanner(System.`in`)
val numbers = HashSet<PartNumber>()
val symbols = HashSet<Pair<Int, Int>>()
val stars = HashSet<Pair<Int, Int>>()
var y = 0
while (scanner.hasNext()) {
    val line = scanner.nextLine()
    var x = 0
    while (x < line.length) {
        if (line[x].isDigit()) {
            val start = Pair(x, y)
            x++
            while (x < line.length && line[x].isDigit()) x++
            numbers.add(PartNumber(start, Pair(x - 1, y), line.substring(start.first, x).toInt()))
            x--
        } else if (line[x] == '*') {
            stars.add(Pair(x, y))
            symbols.add(Pair(x, y))
        } else if (line[x] != '.') {
            symbols.add(Pair(x, y))
        }
        x++
    }
    y++
}

fun around(pos: Pair<Int, Int>, symbols: Set<Pair<Int, Int>>): Boolean {
    return symbols.contains(Pair(pos.first - 1, pos.second - 1)) || symbols.contains(Pair(pos.first - 1, pos.second)) ||
            symbols.contains(Pair(pos.first - 1, pos.second + 1)) ||
            symbols.contains(Pair(pos.first, pos.second - 1)) || symbols.contains(Pair(pos.first, pos.second + 1)) ||
            symbols.contains(Pair(pos.first + 1, pos.second - 1)) || symbols.contains(Pair(pos.first + 1, pos.second)) ||
            symbols.contains(Pair(pos.first + 1, pos.second + 1))
}

var ratios = 0
for (star in stars) {
    val naround = numbers.filter { around(it.start, Collections.singleton(star)) || around(it.end, Collections.singleton(star)) }.toList()
    if (naround.size == 2) {
        ratios += naround[0].value * naround[1].value
    }
}
var sum = numbers.filter { around(it.start, symbols) || around(it.end, symbols) }.sumOf { it.value }
println("$sum $ratios")
