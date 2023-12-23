import kotlin.math.max

class Day23 {
    val up = Pair(0, -1); val right = Pair(1, 0); val down = Pair(0, 1); val left = Pair(-1, 0)
    val grid = ArrayList<String>()

    class State(var pos: Pair<Int, Int>, var steps: Int, val path: HashSet<Pair<Int, Int>>)

    fun run() {
        while (true) {
            val line = readlnOrNull() ?: break
            grid.add(line)
        }
        var result1 = 0; var result2 = 0
        val start = Pair(1, 0); val end = Pair(grid[0].length - 2, grid.size - 1)
        val queue = ArrayDeque<State>()
        queue.add(State(start, 0, hashSetOf(start)))
        fun canMove(s: State, dir: Pair<Int, Int>): Boolean {
            val next = Pair(s.pos.first + dir.first, s.pos.second + dir.second)
            return next.first >= 0 && next.second >= 0 && next.first < grid[0].length && next.second < grid.size &&
                    grid[next.second][next.first] != '#' && !s.path.contains(next)
        }
        fun move(s: State, dir: Pair<Int, Int>) {
            if (canMove(s, dir)) {
                val next = Pair(s.pos.first + dir.first, s.pos.second + dir.second)
                val p = HashSet(s.path)
                p.add(next)
                queue.add(State(next, s.steps + 1, p))
            }
        }
        while (queue.isNotEmpty()) {
            val s = queue.removeFirst()
            if (s.pos == end) { result1 = max(result1, s.steps); continue }
            when (grid[s.pos.second][s.pos.first]) {
                '>' -> move(s, right); 'v' -> move(s, down); '<' -> move(s, left); '^' -> move(s, up)
                '.' -> { move(s, right); move(s, down); move(s, left); move(s, up) }
                else -> throw IllegalStateException()
            }
        }
        queue.clear()
        queue.add(State(start, 0, hashSetOf(start)))
        while (queue.isNotEmpty()) {
            val s = queue.removeFirst()
            if (s.pos == end) { result2 = max(result2, s.steps); continue }
            while (true) {
                var directions = 0;
                if (canMove(s, right)) directions++; if (canMove(s, down)) directions++; if (canMove(s, left)) directions++; if (canMove(s, up)) directions++
                if (directions != 1) break
                if (canMove(s, right)) {
                    s.pos = Pair(s.pos.first + right.first, s.pos.second + right.second)
                    s.steps++
                    s.path.add(s.pos)
                } else if (canMove(s, down)) {
                    s.pos = Pair(s.pos.first + down.first, s.pos.second + down.second)
                    s.steps++
                    s.path.add(s.pos)
                } else if (canMove(s, left)) {
                    s.pos = Pair(s.pos.first + left.first, s.pos.second + left.second)
                    s.steps++
                    s.path.add(s.pos)
                } else if (canMove(s, up))  {
                    s.pos = Pair(s.pos.first + up.first, s.pos.second + up.second)
                    s.steps++
                    s.path.add(s.pos)
                } else throw IllegalStateException()
            }
            when (grid[s.pos.second][s.pos.first]) {
                '.', '>', 'v', '^', '<' -> { move(s, right); move(s, down); move(s, left); move(s, up) }
                else -> throw IllegalStateException()
            }
        }
        println(listOf(result1, result2))
    }
}

fun main() { Day23().run() }
