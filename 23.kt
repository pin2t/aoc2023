import kotlin.math.max

class Day23 {
    val up = Pair(0, -1); val right = Pair(1, 0); val down = Pair(0, 1); val left = Pair(-1, 0)
    val grid = ArrayList<String>()
    val processed = HashSet<Pair<Int, Int>>()

    inline fun move(pos: Pair<Int, Int>, dir: Pair<Int, Int>): Pair<Int, Int> = Pair(pos.first + dir.first, pos.second + dir.second)

    fun longest(from: Pair<Int, Int>, to: Pair<Int, Int>, steps: Int): Int {
        if (from == to) return steps
        processed.add(from)
        var l = 0
        fun tryMove(dir: Pair<Int, Int>) {
            val next = move(from, dir);
            if (next.first >= 0 && next.second >= 0 &&
                next.first < grid[0].length && next.second < grid.size &&
                grid[next.second][next.first] != '#' &&
                !processed.contains(next)) {
                l = max(l, longest(next, to, steps + 1))
            }
        }
        when (grid[from.second][from.first]) {
            '>' -> tryMove(right); 'v' -> tryMove(down); '<' -> tryMove(left); '^' -> tryMove(up,)
            '.' -> { tryMove(right); tryMove(down); tryMove(left); tryMove(up); }
            else -> throw IllegalStateException()
        }
        processed.remove(from)
        return l
    }

    var result2 = 0

    inline fun tryMove2(from: Pair<Int, Int>, dir: Pair<Int, Int>, to: Pair<Int, Int>, steps: Int) {
        val next = move(from, dir);
        if (next.first >= 0 && next.second >= 0 &&
            next.first < grid[0].length && next.second < grid.size &&
            grid[next.second][next.first] != '#' &&
            !processed.contains(next)) {
            longest2(next, to, steps + 1)
        }
    }

    fun longest2(from: Pair<Int, Int>, to: Pair<Int, Int>, steps: Int) {
        if (from == to) {
            result2 = max(result2, steps)
            return
        }
        processed.add(from)
        tryMove2(from, right, to, steps);
        tryMove2(from, down, to, steps);
        tryMove2(from, left, to, steps);
        tryMove2(from, up, to, steps)
        processed.remove(from)
    }

    fun run() {
        while (true) {
            val line = readlnOrNull() ?: break
            grid.add(line)
        }
        val start = Pair(1, 0); val end = Pair(grid[0].length - 2, grid.size - 1)
        val result1 = longest(start, end, 0);
        processed.clear()
        println(result1)
        longest2(start, end, 0)
        println(listOf(result1, result2))
    }
}

fun main() { Day23().run() }
