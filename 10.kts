var tiles = System.`in`.bufferedReader().readLines().map { it.toByteArray() }
var start = Pair(0, 0)
for (y in tiles.indices) {
    if (tiles[y].indexOf('S'.code.toByte()) != -1) {
        start = Pair(tiles[y].indexOf('S'.code.toByte()), y)
        break
    }
}
var steps = 0
var pos = start
fun tile(x: Int, y: Int): Char {
    if (y < 0 || y >= tiles.size) return ' '
    if (x < 0 || x >= tiles[y].size) return ' '
    return tiles[y][x].toChar()
}
var dir = Pair(1, 0)
if ("-J7".contains(tile(pos.first + 1, pos.second))) dir = Pair(1, 0)
else if ("|JL".contains(tile(pos.first, pos.second + 1))) dir = Pair(0, 1)
else if ("-LF".contains(tile(pos.first - 1, pos.second))) dir = Pair(-1, 0)
else if ("|F7".contains(tile(pos.first, pos.second - 1))) dir = Pair(0, -1)
val loop = HashSet<Pair<Int, Int>>()
val loop2 = HashSet<Pair<Int, Int>>()
do {
    loop.add(pos)
    loop2.add(Pair(pos.first * 2, pos.second * 2))
    pos = Pair(pos.first + dir.first, pos.second + dir.second)
    steps++
    if (tile(pos.first, pos.second) == 'F') {
        if (dir.first == -1) dir = Pair(0, 1)
        else if (dir.second == -1) dir = Pair(1, 0)
    } else if (tile(pos.first, pos.second) == 'L') {
        if (dir.first == -1) dir = Pair(0, -1)
        else if (dir.second == 1) dir = Pair(1, 0)
    } else if (tile(pos.first, pos.second) == '7') {
        if (dir.first == 1) dir = Pair(0, 1)
        else if (dir.second == -1) dir = Pair(-1, 0)
    } else if (tile(pos.first, pos.second) == 'J') {
        if (dir.first == 1) dir = Pair(0, -1)
        else if (dir.second == 1) dir = Pair(-1, 0)
    }
} while (pos != start)
for (r in 0..<(tiles.size * 2)) {
    for (c in 0..<(tiles[0].size * 2)) {
        if (loop2.contains(Pair(c + 1, r)) && loop2.contains(Pair(c - 1, r)) &&
            "L-FS".contains(tile((c - 1) / 2, r / 2)) && "J-7S".contains(tile((c + 1) / 2, r / 2)))
            loop2.add(Pair(c, r))
        if (loop2.contains(Pair(c, r - 1)) && loop2.contains(Pair(c, r + 1)) &&
            "7|FS".contains(tile(c / 2, (r - 1) / 2)) && "J|LS".contains(tile(c / 2, (r + 1) / 2)))
            loop2.add(Pair(c, r))
    }
}
fun inner(loop: Set<Pair<Int, Int>>, pos: Pair<Int, Int>): Boolean {
    val visited = HashSet<Pair<Int, Int>>()
    val queue = ArrayDeque<Pair<Int, Int>>()
    queue.add(pos)
    while (!queue.isEmpty()) {
        val p = queue.removeFirst()
        if (visited.contains(p)) continue
        visited.add(p)
        if (p.first < 0 || p.second < 0 || p.first >= tiles[0].size * 2 || p.second >= tiles.size * 2) return false
        fun move(dx: Int, dy: Int) {
            val pp = Pair(p.first + dx, p.second + dy)
            if (!loop.contains(pp)) queue.add(pp)
        }
        move(1, 0); move(0, 1); move(-1, 0); move(0, -1)
    }
    return true
}
var inners = 0
for (r in tiles.indices) for (c in tiles[r].indices)
    if (!loop.contains(Pair(c, r)) && inner(loop2, Pair(c * 2, r * 2)))
        inners++
println(listOf(steps / 2, inners))
