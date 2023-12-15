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
        val lens = Pair(step.split('=')[0], step.split('=')[1].toInt())
        val i = hash(lens.first)
        if (boxes[i].any { it.first == lens.first }) {
            boxes[i][boxes[i].indexOfFirst { it.first == lens.first }] = lens
        } else {
            boxes[i].add(lens)
        }
    } else if (step.endsWith('-')) {
        val label = step.split('-')[0]
        boxes[hash(label)].removeIf { it.first == label }
    }
}
var total = 0
boxes.forEachIndexed { bi, lenses -> lenses.forEachIndexed { li, lens -> total += (bi + 1) * (li + 1) * lens.second } }
println(listOf(steps.sumOf { hash(it) }, total))
