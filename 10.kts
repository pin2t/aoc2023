var tiles = System.`in`.bufferedReader().readLines().map { it.toByteArray() }
fun tile(c: Int, r: Int): Char {
    if (r < 0 || r >= tiles.size) return ' '
    if (c < 0 || c >= tiles[r].size) return ' '
    return tiles[r][c].toChar()
}
var start = Pair(0, 0)
for (r in tiles.indices) {
    val c = tiles[r].indexOf('S'.code.toByte())
    if (c != -1) {
        start = Pair(c, r)
        break
    }
}
var steps = 0
var pos = start
fun tile(pos: Pair<Int, Int>, dx: Int, dy: Int): Char {
    return tile(pos.first + dx, pos.second + dy)
}
val UP = Pair(0, -1); val DOWN = Pair(0, 1); val LEFT = Pair(-1, 0); val RIGHT = Pair(1, 0)
var dir = RIGHT
if ("-J7".contains(tile(pos, 1, 0))) dir = RIGHT
else if ("|JL".contains(tile(pos, 0, 1))) dir = DOWN
else if ("-LF".contains(tile(pos, -1, 0))) dir = LEFT
else if ("|F7".contains(tile(pos, 0, -1))) dir = UP
val loop = HashSet<Pair<Int, Int>>()
val loop2 = HashSet<Pair<Int, Int>>()
do {
    loop.add(pos)
    loop2.add(Pair(pos.first * 2, pos.second * 2))
    pos = Pair(pos.first + dir.first, pos.second + dir.second)
    steps++
    when (tile(pos, 0, 0)) {
        'F' -> if (dir == LEFT) dir = DOWN else if (dir == UP) dir = RIGHT
        'L' -> if (dir == LEFT) dir = UP else if (dir == DOWN) dir = RIGHT
        '7' -> if (dir == RIGHT) dir = DOWN else if (dir == UP) dir = LEFT
        'J' -> if (dir == RIGHT) dir = UP else if (dir == DOWN) dir = LEFT
    }
} while (pos != start)
for (r in 0..<(tiles.size * 2)) {
    for (c in 0..<(tiles[0].size * 2)) {
        if (loop2.contains(Pair(c + 1, r)) && loop2.contains(Pair(c - 1, r)) &&
            "L-FS".contains(tile((c - 1) / 2, r / 2)) && "J-7S".contains(tile((c + 1) / 2, r / 2))) {
            loop2.add(Pair(c, r))
        }
        if (loop2.contains(Pair(c, r - 1)) && loop2.contains(Pair(c, r + 1)) &&
            "7|FS".contains(tile(c / 2, (r - 1) / 2)) && "J|LS".contains(tile(c / 2, (r + 1) / 2))) {
            loop2.add(Pair(c, r))
        }
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
for (r in tiles.indices) {
    for (c in tiles[r].indices) {
        if (!loop.contains(Pair(c, r)) && inner(loop2, Pair(c * 2, r * 2))) {
            inners++
        }
    }
}
println(listOf(steps / 2, inners))
