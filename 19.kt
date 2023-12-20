data class Workflow (val rules: List<List<String>>, val default: String)

fun main() {
    val workflows = HashMap<String, Workflow>()
    val parts = ArrayList<Map<String, Int>>()
    while (true) {
        val l = readlnOrNull() ?: break
        if (l.startsWith('{')) {
            val part = HashMap<String, Int>()
            l.substring(1, l.length - 1).split(',').forEach {
                part[it.split('=')[0]] = it.split('=')[1].toInt()
            }
            parts.add(part)
        } else if (l.isNotBlank()) {
            val items = l.substring(l.indexOf('{') + 1, l.length - 1).split(',')
            workflows[l.substring(0, l.indexOf('{'))] = Workflow(
                items.dropLast(1).map {
                    listOf(it[0].toString(), it[1].toString(), it.substring(2, it.indexOf(':')), it.substring(it.indexOf(':') + 1))
                }.toList(),
                items.last()
            )
        }
    }
    var res1 = 0; var res2: Long = 0
    for (part in parts) {
        var wf = "in"
        do {
            var matched = false
            for (rule in workflows[wf]!!.rules) {
                val value = part[rule[0]]!!
                val arg = rule[2].toInt()
                matched = if (rule[1] == "<") value < arg else value > arg
                if (matched) { wf = rule[3]; break }
            }
            if (!matched) wf = workflows[wf]!!.default
        } while (wf != "A" && wf != "R")
        if (wf == "A") res1 += part["x"]!! + part["m"]!! + part["a"]!! + part["s"]!!
    }
    val queue = ArrayDeque<Pair<String, Map<String, IntRange>>>()
    queue.add(Pair("in", hashMapOf("x" to 1..4000, "m" to 1..4000, "a" to 1..4000, "s" to 1..4000)))
    while (!queue.isEmpty()) {
        val r = queue.removeFirst()
        if (r.first == "A") {
            fun parts(name: String): Long = r.second[name]!!.count().toLong()
            res2 += parts("x") * parts("m") * parts("a") * parts("s")
            continue
        } else if (r.first == "R") {
            continue
        }
        var ranges = r.second
        for (rule in workflows[r.first]!!.rules) {
            val part = rule[0]
            val arg = rule[2].toInt()
            val matched = HashMap(ranges); val left = HashMap(ranges)
            when (rule[1]) {
                "<" -> {
                    matched[part] = ranges[part]!!.first..<arg
                    left[part] = arg..ranges[part]!!.last
                }
                ">" -> {
                    matched[part] = (arg + 1)..ranges[part]!!.last
                    left[part] = ranges[part]!!.first..arg
                }
            }
            if (matched[part]!!.first <= matched[part]!!.last) {
                queue.add(Pair(rule[3], matched))
            }
            if (left[part]!!.first <= left[part]!!.last) {
                ranges = left
            } else {
                ranges = emptyMap()
                break
            }
        }
        if (!ranges.isEmpty()) queue.add(Pair(workflows[r.first]!!.default, ranges))
    }
    println(listOf(res1, res2))
}
