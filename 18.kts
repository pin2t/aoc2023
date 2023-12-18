import java.util.*

val up = Pair(0, -1); val right = Pair(1, 0); val down = Pair(0, 1); val left = Pair(-1, 0)
val scanner = Scanner(System.`in`)
var pos = Pair(0, 0)
var pos2 = Pair(0, 0)
val loop = HashSet<Pair<Int, Int>>()
val corners = ArrayList<Pair<Int, Int>>()
fun dig(dir: Pair<Int, Int>, n: Int) {
    loop.add(pos)
    for (i in 1..n) {
        pos = Pair(pos.first + dir.first, pos.second + dir.second)
        loop.add(pos)
    }
}
fun move(pos: Pair<Int, Int>, dir: Pair<Int, Int>): Pair<Int, Int> = Pair(pos.first + dir.first, pos.second + dir.second)
var loop2 = 1
while (scanner.hasNext()) {
    val line = scanner.nextLine().split(' ')
    val n = line[1].toInt()
    when (line[0][0]) {
        'U' -> dig(up, n); 'D' -> dig(down, n); 'L' -> dig(left, n); 'R' -> dig(right, n)
    }
    val n2 = line[2].substring(2, 7).toInt(16)
    when (line[0][0]) {
        'U' -> for (i in 1..n2) pos2 = move(pos2, up)
        'D' -> for (i in 1..n2) pos2 = move(pos2, down)
        'L' -> for (i in 1..n2) pos2 = move(pos2, left)
        'R' -> for (i in 1..n2) pos2 = move(pos2, right)
    }
    loop2 += n2
    corners.add(pos2)
}
corners.add(Pair(0, 0))
val l = loop.minOf { it.first }; val r = loop.maxOf { it.first }
val t = loop.minOf { it.second }; val b = loop.maxOf { it.second }
fun insideCount(pos: Pair<Int, Int>): Int {
    val processed = HashSet<Pair<Int, Int>>()
    val queue = LinkedList<Pair<Int, Int>>()
    queue.add(pos)
    while (!queue.isEmpty()) {
        val p = queue.removeFirst()
        if (loop.contains(p) || processed.contains(p)) continue
        if (p.first < l || p.first > r || p.second < t || p.second > b) return 0
        processed.add(p)
        queue.addAll(listOf(move(p, up), move(p, down), move(p, right), move(p, left)))
    }
    return processed.size
}
var res1 = 0
for (r in t..b) {
    for (c in l..r) {
        val count = insideCount(Pair(c, r))
        if (count != 0) { res1 = loop.size + count; break }
    }
    if (res1 != 0) break
}
var res2: Long = 0
for (i in 0..<(corners.size - 1))
    res2 += corners[i].first.toLong() * corners[i + 1].second.toLong() -
            corners[i + 1].first.toLong() * corners[i].second.toLong()
res2 = res2 / 2 + loop2 / 2
println(listOf(res1, res2))
