val steps = System.`in`.bufferedReader().readLine().split(',')
fun hash(s: String): Int {
    var result = 0
    for (c in s) { result += c.code; result *= 17; result %= 256 }
    return result
}
val boxes = ArrayList<ArrayList<Pair<String, Int>>>()
for (i in 0..255) boxes.add(ArrayList())
for (step in steps) {
    if (step.contains('=')) {
        val lens = step.split('=')[0]
        val focus = step.split('=')[1].toInt()
        val i = hash(lens)
        if (boxes[i].any { it.first == lens }) {
            boxes[i][boxes[i].indexOfFirst { it.first == lens }] = Pair(lens, focus)
        } else {
            boxes[i].add(Pair(lens, focus))
        }
    } else if (step.endsWith('-')) {
        var lens = step.split('-')[0]
        boxes[hash(lens)].removeIf { it.first == lens }
    }
}
var total = 0
boxes.forEachIndexed { bi, pairs -> pairs.forEachIndexed { li, pair -> total += (bi + 1) * (li + 1) * pair.second } }
println(listOf(steps.sumOf { hash(it) }, total))
