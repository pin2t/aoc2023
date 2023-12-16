import kotlin.math.max

var grid = System.`in`.bufferedReader().lines().toList()
data class Beam(val pos: Pair<Int, Int>, val dir: Pair<Int, Int>)
val up = Pair(0, -1); val right = Pair(1, 0); val down = Pair(0, 1); val left = Pair(-1, 0)
fun energized(b: Beam): Int {
    val queue = ArrayDeque<Beam>()
    val processed = HashSet<Beam>()
    val energized = HashSet<Pair<Int, Int>>()
    queue.add(b)
    while (!queue.isEmpty()) {
        val beam = queue.removeFirst()
        if (beam.pos.first < 0 || beam.pos.second < 0 || beam.pos.first >= grid[0].length || beam.pos.second >= grid.size ||
            processed.contains(beam))
            continue
        energized.add(beam.pos)
        processed.add(beam)
        when (grid[beam.pos.second][beam.pos.first]) {
            '-' -> {
                if (beam.dir == left || beam.dir == right)
                    queue.add(Beam(pos = Pair(beam.pos.first + beam.dir.first, beam.pos.second + beam.dir.second), dir = beam.dir))
                else {
                    queue.add(Beam(pos = Pair(beam.pos.first - 1, beam.pos.second), dir = left))
                    queue.add(Beam(pos = Pair(beam.pos.first + 1, beam.pos.second), dir = right))
                }
            }
            '|' -> {
                if (beam.dir.first == 0)
                    queue.add(Beam(pos = Pair(beam.pos.first + beam.dir.first, beam.pos.second + beam.dir.second), dir = beam.dir))
                else {
                    queue.add(Beam(pos = Pair(beam.pos.first, beam.pos.second - 1), dir = Pair(0, -1)))
                    queue.add(Beam(pos = Pair(beam.pos.first, beam.pos.second + 1), dir = Pair(0, 1)))
                }
            }
            '/' -> {
                when (beam.dir) {
                    up -> queue.add(Beam(pos = Pair(beam.pos.first + 1, beam.pos.second), dir = right))
                    right -> queue.add(Beam(pos = Pair(beam.pos.first, beam.pos.second - 1), dir = up))
                    down -> queue.add(Beam(pos = Pair(beam.pos.first - 1, beam.pos.second), dir = left))
                    left -> queue.add(Beam(pos = Pair(beam.pos.first, beam.pos.second + 1), dir = down))
                }
            }
            '\\' -> {
                when (beam.dir) {
                    up -> queue.add(Beam(pos = Pair(beam.pos.first - 1, beam.pos.second), dir = left))
                    right -> queue.add(Beam(pos = Pair(beam.pos.first, beam.pos.second + 1), dir = down))
                    down -> queue.add(Beam(pos = Pair(beam.pos.first + 1, beam.pos.second), dir = right))
                    left -> queue.add(Beam(pos = Pair(beam.pos.first, beam.pos.second - 1), dir = up))
                }
            }
            '.' -> queue.add(Beam(pos = Pair(beam.pos.first + beam.dir.first, beam.pos.second + beam.dir.second), dir = beam.dir))
        }
    }
    return energized.size
}
var result2 = 0
for (r in 0..<grid.size) {
    result2 = max(result2,
        max(energized(Beam(pos = Pair(0, r), dir = right)), energized(Beam(pos = Pair(grid[0].length - 1, r), dir = left))))
}
for (c in 0..<grid[0].length) {
    result2 = max(result2,
        max(energized(Beam(pos = Pair(c, 0), dir = down)), energized(Beam(pos = Pair(c, grid.size - 1), dir = up))))
}
println(listOf(energized(Beam(pos = Pair(1, 0), dir = right)), result2))
