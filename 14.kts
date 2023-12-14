val platform = System.`in`.bufferedReader().lines().map { it.toCharArray() }.toList()
fun north() {
    for (c in 0..<platform[0].size) {
        for (r in 0..<platform.size) {
            if (platform[r][c] == 'O') {
                platform[r][c] = '.'
                var i = r - 1; while (i >= 0 && platform[i][c] == '.') i--
                platform[i + 1][c] = 'O'
            }
        }
    }
}
fun west() {
    for (r in 0..<platform.size) {
        for (c in 0..<platform[0].size) {
            if (platform[r][c] == 'O') {
                platform[r][c] = '.'
                var i = c - 1; while (i >= 0 && platform[r][i] == '.') i--
                platform[r][i + 1] = 'O'
            }
        }
    }
}
fun south() {
    for (c in 0..<platform[0].size) {
        for (r in (platform.size-1) downTo 0) {
            if (platform[r][c] == 'O') {
                platform[r][c] = '.'
                var i = r + 1; while (i < platform.size && platform[i][c] == '.') i++
                platform[i - 1][c] = 'O'
            }
        }
    }
}
fun east() {
    for (r in 0..<platform.size) {
        for (c in (platform[0].size - 1) downTo 0) {
            if (platform[r][c] == 'O') {
                platform[r][c] = '.'
                var i = c + 1; while (i < platform[0].size && platform[r][i] == '.') i++
                platform[r][i - 1] = 'O'
            }
        }
    }
}
fun state(): List<Int> {
    val result = ArrayList<Int>()
    platform.forEachIndexed { row, chars -> chars.forEachIndexed { col, c -> if (c == 'O') result.add(platform.size - row) } }
    return result
}
north()
var load1 = state().sum()
var cycle: Long = 0
val states = HashMap<List<Int>, Long>()
while (cycle < 1000000000) {
    north(); west(); south(); east()
    cycle++
    val s = state()
    if (states.contains(s)) {
        val len = cycle - states[s]!!
        while (cycle < 1000000000 - len) cycle += len
        states.clear()
    } else {
        states[s] = cycle
    }
}
println(listOf(load1, state().sum()))
