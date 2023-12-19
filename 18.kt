fun main() {
     val up = Pair(0, -1); val right = Pair(1, 0); val down = Pair(0, 1); val left = Pair(-1, 0)

    fun move(pos: Pair<Int, Int>, dir: Pair<Int, Int>, n: Int): Pair<Int, Int> = Pair(pos.first + dir.first * n, pos.second + dir.second * n)

    fun result(len: Long, corners: List<Pair<Int, Int>>): Long {
        var result: Long = 0
        for (i in 1..<(corners.size - 1))
            result += corners[i].first.toLong() * (corners[i + 1].second.toLong() - corners[i - 1].second.toLong())
        return result / 2 + len / 2 + 1
    }

    var pos = Pair(0, 0); var pos2 = Pair(0, 0)
    val corners = ArrayList<Pair<Int, Int>>(); val corners2 = ArrayList<Pair<Int, Int>>()
    var len1 = 0; var len2: Long = 0
    corners.add(Pair(0, 0))
    corners2.add(Pair(0, 0))
    do {
        val l = readlnOrNull() ?: break;
        val line = l.split(' ')
        val n = line[1].toInt()
        when (line[0][0]) {
            'U' -> pos = move(pos, up, n); 'D' -> pos = move(pos, down, n); 'L' -> pos = move(pos, left, n); 'R' -> pos = move(pos, right, n)
        }
        corners.add(pos)
        len1 += n
        val n2 = line[2].substring(2, 7).toInt(16)
        when (line[2][7]) {
            '3' -> pos2 = move(pos2, up, n2); '1' -> pos2 = move(pos2, down, n2); '2' -> pos2 = move(pos2, left, n2); '0' -> pos2 = move(pos2, right, n2)
        }
        len2 += n2
        corners2.add(pos2)
    } while (true)
    println(listOf(result(len1.toLong(), corners), result(len2, corners2)))
}
