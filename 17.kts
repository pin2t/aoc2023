import java.util.*

val grid = System.`in`.bufferedReader().lines().map { it.map { (it - '0') }.toList() }.toList()
val up = Pair(0, -1); val right = Pair(1, 0); val down = Pair(0, 1); val left = Pair(-1, 0)
data class Crucible(val pos: Pair<Int, Int>, val dir: Pair<Int, Int>, val loss: Int, val straights: Int)
var res1 = 0; var res2 = 0
val queue = PriorityQueue<Crucible>(compareBy { it.loss })
queue.add(Crucible(Pair(0, 0), right, 0, 0))
val processed = HashSet<Triple<Pair<Int, Int>, Pair<Int, Int>, Int>>()
while (!queue.isEmpty()) {
    val c = queue.poll()
    if (c.pos == Pair(grid[0].size - 1, grid.size - 1)) { res1 = c.loss; break }
    val key = Triple(c.pos, c.dir, c.straights)
    if (processed.contains(key)) continue
    processed.add(key)
    listOf(up, right, down, left).forEach { dir ->
        val np = Pair(c.pos.first + dir.first, c.pos.second + dir.second)
        if (np.first >= 0 && np.second >= 0 && np.first < grid[0].size && np.second < grid.size) {
            val next = Crucible(np, dir, c.loss + grid[np.second][np.first], c.straights + 1)
            if (c.dir == dir) {
                if (next.straights <= 3) queue.add(next)
            } else {
                if (c.dir == up && dir != down || c.dir == right && dir != left || c.dir == down && dir != up || c.dir == left && dir != right) {
                    queue.add(Crucible(next.pos, next.dir, next.loss, 1))
                }
            }
        }
    }
}
queue.clear()
queue.add(Crucible(Pair(0, 0), right, 0, 0))
processed.clear()
while (!queue.isEmpty()) {
    val c = queue.poll()
    if (c.pos == Pair(grid[0].size - 1, grid.size - 1)) { res2 = c.loss; break }
    val key = Triple(c.pos, c.dir, c.straights)
    if (processed.contains(key)) continue
    processed.add(key)
    listOf(up, right, down, left).forEach { dir ->
        val np = Pair(c.pos.first + dir.first, c.pos.second + dir.second)
        if (np.first >= 0 && np.second >= 0 && np.first < grid[0].size && np.second < grid.size) {
            val next = Crucible(np, dir, c.loss + grid[np.second][np.first], c.straights + 1)
            if (c.dir == dir) {
                if (next.straights <= 10) queue.add(next)
            } else if (c.straights >= 4) {
                if (c.dir == up && dir != down || c.dir == right && dir != left || c.dir == down && dir != up || c.dir == left && dir != right) {
                    queue.add(Crucible(next.pos, next.dir, next.loss, 1))
                }
            }
        }
    }
}
println(listOf(res1, res2))
