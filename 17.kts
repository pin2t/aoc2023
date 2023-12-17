import java.util.*

val grid = System.`in`.bufferedReader().lines().map { it.map { (it - '0') }.toList() }.toList()
val up = Pair(0, -1); val right = Pair(1, 0); val down = Pair(0, 1); val left = Pair(-1, 0)
data class State(val pos: Pair<Int, Int>, val dir: Pair<Int, Int>,
                 val loss: Int, val straight: Int, val path: ArrayList<Pair<Int, Int>>)
val queue = LinkedList<State>()
queue.add(State(Pair(0, 0), right, 0, 1, ArrayList()))
val losses = HashMap<Pair<Int, Int>, Pair<Int, List<Pair<Int, Int>>>>()
losses[Pair(0, 0)] = Pair(0, ArrayList())
while (!queue.isEmpty()) {
    val s = queue.removeFirst()
    fun move(dir: Pair<Int, Int>) {
        val np = Pair(s.pos.first + dir.first, s.pos.second + dir.second)
        if (!(np.first >= 0 && np.second >= 0 && np.first < grid[0].size && np.second < grid.size)) return
        val next = State(np, dir, s.loss + grid[np.second][np.first], s.straight + 1, ArrayList(s.path))
        next.path.add(np)
        if (losses.contains(next.pos) && next.loss >= losses[next.pos]!!.first) return
        if (s.dir == dir) {
            if (next.straight <= 3) {
                losses[np] = Pair(next.loss, next.path)
                queue.add(next)
            }
        } else {
            if (s.dir == up && dir != down || s.dir == right && dir != left ||
                s.dir == down && dir != up || s.dir == left && dir != right) {
                val nn = State(np, dir, next.loss, 0, next.path)
                losses[np] = Pair(next.loss, next.path)
                queue.add(nn)
            }
        }
    }
    move(up); move(right); move(down); move(left)
}
var res = losses.get(Pair(grid[0].size - 1, grid.size - 1))!!
println(res.first)
