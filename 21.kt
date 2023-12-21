val up = Pair(0, -1); val right = Pair(1, 0); val down = Pair(0, 1); val left = Pair(-1, 0)
val rocks = HashSet<Pair<Int, Int>>()

fun main() {
    var rows = 0
    var start = Pair(0, 0)
    while (true) {
        val l = readlnOrNull() ?: break
        l.forEachIndexed { col, ch ->
            when (ch) { 'S' -> start = Pair(col, rows); '#' -> rocks.add(Pair(col, rows)) }
        }
        rows++
    }
    var reach = HashSet<Pair<Int, Int>>()
    reach.add(start)
    for (i in 1..64) {
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
    println(reach.size)
}
