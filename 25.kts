val graph = HashMap<String, ArrayList<String>>()
val edges = HashMap<Pair<String, String>, Int>()

fun max(a: String, b: String): String = if (a > b) a else b
fun min(a: String, b: String): String = if (a < b) a else b

while (true) {
    val line = readlnOrNull() ?: break
    val items = line.split(":", " ").map { it.trim() }
    for (node in items.subList(2, items.size)) {
        graph.computeIfAbsent(items[0]) { ArrayList() }.add(node)
        graph.computeIfAbsent(node) { ArrayList() }.add(items[0])
    }
}
for (start in graph.keys) {
    val queue = ArrayDeque<String>()
    val seen = HashSet<String>()
    val prev = HashMap<String, String>()
    queue.add(start)
    while (queue.isNotEmpty()) {
        val node = queue.removeFirst()
        for (next in graph[node]!!) {
            if (next in seen) continue
            seen.add(next)
            queue.add(next)
            prev[next] = node
        }
    }
    for (node in graph.keys) {
        var n = node
        while (n != start) {
            val pr = prev[n]!!
            val edge = Pair(min(pr, n), max(pr, n))
            if (edge in edges)
                edges[edge] = edges[edge]!! + 1
            else
                edges[edge] = 1
            n = pr
        }
    }
}
for (e in edges.entries.sortedByDescending { it.value }.subList(0, 3)) {
    graph[e.key.first]!!.remove(e.key.second)
    graph[e.key.second]!!.remove(e.key.first)
}
val seen = HashSet<String>()
val queue = ArrayDeque<String>()
queue.add(graph.keys.first())
while (queue.isNotEmpty()) {
    val node = queue.removeFirst()
    for (next in graph[node]!!) {
        if (next in seen) continue
        seen.add(next)
        queue.add(next)
    }
}
println(seen.size * (graph.keys.size - seen.size))
