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
fun move(pos: Pair<Int, Int>, dir: Pair<Int, Int>, n: Int): Pair<Int, Int> =
    Pair(pos.first + dir.first * n, pos.second + dir.second * n)
var loop2: Long = 1
while (scanner.hasNext()) {
    val line = scanner.nextLine().split(' ')
    val n = line[1].toInt()
    when (line[0][0]) {
        'U' -> dig(up, n); 'D' -> dig(down, n); 'L' -> dig(left, n); 'R' -> dig(right, n)
    }
    val n2 = line[2].substring(2, 7).toInt(16)
    when (line[0][0]) {
        'U' -> pos2 = move(pos2, up, n2)
        'D' -> pos2 = move(pos2, down, n2)
        'L' -> pos2 = move(pos2, left, n2)
        'R' -> pos2 = move(pos2, right, n2)
    }
    loop2 += n2
    corners.add(pos2)
}
corners.add(Pair(0, 0))
val l = loop.minOf { it.first }; val r = loop.maxOf { it.first }
val t = loop.minOf { it.second }; val b = loop.maxOf { it.second }
fun inside(pos: Pair<Int, Int>): Int {
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
fun res1(): Int {
    for (r in t..b) {
        for (c in l..r) {
            val count = inside(Pair(c, r))
            if (count != 0) return loop.size + count
        }
    }
    return 0
}
fun res2(): Long {
    var result: Long = 0
    for (i in 0..<(corners.size - 1))
        result += corners[i].first.toLong() * corners[i + 1].second.toLong() -
                  corners[i + 1].first.toLong() * corners[i].second.toLong()
    return result / 2 + loop2 / 2
}
println(listOf(res1(), res2()))
