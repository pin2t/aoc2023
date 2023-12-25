class Day25 {
    val connections = HashMap<String, ArrayList<String>>()

    fun run() {
        while (true) {
            var line = readlnOrNull() ?: break
            val items = Regex("[a-z]+").findAll(line).map { it.value }.toList()
            connections[items[0]] = ArrayList(items.subList(1, items.size))
            for (c in items.subList(1, items.size)) {
                if (c !in connections) {
                    connections[c] = ArrayList()
                }
                connections[c]!!.add(items[0])
            }
        }
        val paths = HashMap<Pair<String, String>, Int>()
        val components = ArrayList(connections.keys)
        for (i in 0..<(components.size - 1)) {
            for (j in (i + 1)..<components.size) {
                val queue = ArrayDeque<String>()
                val seen = HashSet<String>()
                queue.add(components[i])
                while (queue.isNotEmpty()) {
                    val c = queue.removeFirst()
                    if (c == components[j]) break
                    if (!seen.add(c)) continue
                    for (conn in connections[c]!!) {
                        paths[Pair(c, conn)] = (paths[Pair(c, conn)] ?: 0) + 1
                        queue.add(conn)
                    }
                }
            }
        }
        val links = paths.entries.toList().sortedByDescending { it.value }.take(3)
        for (link in links) {
            connections[link.key.first]!!.remove(link.key.second)
            connections[link.key.second]!!.remove(link.key.first)
        }
        val queue = ArrayDeque<String>()
        val seen = HashSet<String>()
        queue.add(connections.keys.last())
        while (queue.isNotEmpty()) {
            val c = queue.removeFirst()
            if (!seen.add(c)) continue
            for (conn in connections[c]!!) {
                queue.add(conn)
            }
        }
        println(seen.size * (components.size - seen.size))
    }
}

fun main() { Day25().run() }
