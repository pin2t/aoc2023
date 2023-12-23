import kotlin.math.pow

fun main() {
    val up = Pair(0, -1); val right = Pair(1, 0); val down = Pair(0, 1); val left = Pair(-1, 0)
    val rocks = HashSet<Pair<Int, Int>>()
    var rows = 0

    fun reachable(from: Pair<Int, Int>, steps: Int): Int {
        var reach = HashSet<Pair<Int, Int>>()
        reach.add(from)
        for (i in 1..steps) {
            val next = HashSet<Pair<Int, Int>>()
            fun move(p: Pair<Int, Int>, dir: Pair<Int, Int>) {
                if (!rocks.contains(Pair(p.first + dir.first, p.second + dir.second))) {
                    next.add(Pair(p.first + dir.first, p.second + dir.second))
                }
            }
            for (p in reach) {
                move(p, up); move(p, right); move(p, down); move(p, left)
            }
            reach = next
        }
        return reach.count { it.first >= 0 && it.second >= 0 && it.first < rows && it.second < rows }
    }
    var start = Pair(0, 0)
    while (true) {
        val l = readlnOrNull() ?: break
        l.forEachIndexed { col, ch ->
            when (ch) { 'S' -> start = Pair(col, rows); '#' -> rocks.add(Pair(col, rows)) }
        }
        rows++
    }
    var grids: Long = 26501365.toLong() / rows - 1
    var oddGrids: Long = (grids / 2 * 2 + 1).toDouble().pow(2).toLong()
    var evenGrids: Long = ((grids + 1) / 2 * 2).toDouble().pow(2).toLong()
    var res2: Long = oddGrids * reachable(start, rows * 2 + 1) +
            evenGrids * reachable(start, rows * 2) +
            reachable(Pair(start.first, rows - 1), rows - 1) +
            reachable(Pair(0, start.second), rows - 1) +
            reachable(Pair(start.first, 0), rows - 1) +
            reachable(Pair(rows - 1, start.second), rows - 1) +
            ((grids + 1) * (reachable(Pair(0, rows - 1), rows / 2 - 1) +
                reachable(Pair(rows - 1, rows - 1), rows / 2 - 1) +
                reachable(Pair(0, 0), rows / 2 - 1) +
                reachable(Pair(rows - 1, 0), rows / 2 - 1))) +
            (grids * (reachable(Pair(0, rows - 1), rows * 3 / 2 - 1) +
                reachable(Pair(rows - 1, rows - 1), rows * 3 / 2 - 1) +
                reachable(Pair(0, 0), rows * 3 / 2 - 1) +
                reachable(Pair(rows - 1, 0), rows * 3 / 2 - 1)))
    println(listOf(reachable(start, 64), res2))
}
