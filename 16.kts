import kotlin.math.max

val grid = System.`in`.bufferedReader().lines().toList()
val up = Pair(0, -1); val right = Pair(1, 0); val down = Pair(0, 1); val left = Pair(-1, 0)
data class Beam(val pos: Pair<Int, Int>, val dir: Pair<Int, Int>) {
    private fun inside() = pos.first >= 0 && pos.second >= 0 && pos.first < grid[0].length && pos.second < grid.size
    private fun move(): List<Beam> {
        when (grid[pos.second][pos.first]) {
            '-' -> 
                if (dir == left || dir == right) 
                    return listOf(Beam(Pair(pos.first + dir.first, pos.second + dir.second), dir))
                else 
                    return listOf(Beam(Pair(pos.first - 1, pos.second), left), Beam(Pair(pos.first + 1, pos.second), right))
            '|' ->
                if (dir.first == 0)
                    return listOf(Beam(Pair(pos.first + dir.first, pos.second + dir.second), dir))
                else
                    return listOf(Beam(Pair(pos.first, pos.second - 1), Pair(0, -1)), Beam(Pair(pos.first, pos.second + 1), Pair(0, 1)))
            '/' ->
                when (dir) {
                    up -> return listOf(Beam(Pair(pos.first + 1, pos.second), right))
                    right -> return listOf(Beam(Pair(pos.first, pos.second - 1), up))
                    down -> return listOf(Beam(Pair(pos.first - 1, pos.second), left))
                    left -> return listOf(Beam(Pair(pos.first, pos.second + 1), down))
                }
            '\\' ->
                when (dir) {
                    up -> return listOf(Beam(Pair(pos.first - 1, pos.second), left))
                    right -> return listOf(Beam(Pair(pos.first, pos.second + 1), down))
                    down -> return listOf(Beam(Pair(pos.first + 1, pos.second), right))
                    left -> return listOf(Beam(Pair(pos.first, pos.second - 1), up))
                }
            '.' -> return listOf(Beam(Pair(pos.first + dir.first, pos.second + dir.second), dir))
        }
        throw IllegalStateException()
    }
    fun energized(): Int {
        val queue = ArrayDeque<Beam>()
        val processed = HashSet<Beam>()
        val energized = HashSet<Pair<Int, Int>>()
        queue.add(this)
        while (!queue.isEmpty()) {
            val beam = queue.removeFirst()
            if (!beam.inside() || processed.contains(beam))
                continue
            energized.add(beam.pos)
            processed.add(beam)
            queue.addAll(beam.move())
        }
        return energized.size
    }
}
var result2 = 0
for (r in 0..<grid.size) {
    result2 = max(result2, max(Beam(Pair(0, r), right).energized(), Beam(Pair(grid[0].length - 1, r), left).energized()))
}
for (c in 0..<grid[0].length) {
    result2 = max(result2, max(Beam(Pair(c, 0), down).energized(), Beam(Pair(c, grid.size - 1), up).energized()))
}
println(listOf(Beam(Pair(1, 0), right).energized(), result2))
