import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Workflow (val rules: List<List<String>>, val result: String)

fun main() {
    val workflows = HashMap<String, Workflow>()
    val parts = ArrayList<Map<String, Int>>()
    while (true) {
        val l = readlnOrNull() ?: break;
        if (l.startsWith('{')) {
            val part = HashMap<String, Int>()
            l.substring(1, l.length - 1).split(',').forEach {
                part[it.split('=')[0]] = it.split('=')[1].toInt()
            }
            parts.add(part)
        } else if (!l.isBlank()) {
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
                when (rule[1]) {
                    "<" -> matched = part[rule[0]]!! < rule[2].toInt()
                    ">" -> matched = part[rule[0]]!! > rule[2].toInt()
                }
                if (matched) { wf = rule[3]; break }
            }
            if (!matched) wf = workflows[wf]!!.result
        } while (wf != "A" && wf != "R")
        if (wf == "A") res1 += part["x"]!! + part["m"]!! + part["a"]!! + part["s"]!!
    }
    val queue = ArrayDeque<Pair<String, Map<String, IntRange>>>()
    queue.add(Pair("in", hashMapOf("x" to 1..4000, "m" to 1..4000, "a" to 1..4000, "s" to 1..4000)))
    while (!queue.isEmpty()) {
        val r = queue.removeFirst()
        if (r.first == "A") {
            res2 += (r.second["x"]!!.last - r.second["x"]!!.first).toLong() *
                    (r.second["m"]!!.last - r.second["m"]!!.first).toLong() *
                    (r.second["a"]!!.last - r.second["a"]!!.first).toLong() *
                    (r.second["s"]!!.last - r.second["s"]!!.first).toLong()
            continue
        } else if (r.first == "R") continue
        var matched = false
        for (rule in workflows[r.first]!!.rules) {
            when (rule[1]) {
                "<" -> {
                    if (r.second[rule[0]]!!.contains(rule[2].toInt())) {
                        val ranges = HashMap(r.second)
                        ranges[rule[0]] = ranges[rule[0]]!!.first..<min(ranges[rule[0]]!!.last, rule[2].toInt())
                        queue.add(Pair(rule[3], ranges))
                        val ranges2 = HashMap(r.second)
                        ranges2[rule[0]] = min(ranges[rule[0]]!!.last, rule[2].toInt())..(ranges[rule[0]]!!.last)
                        queue.add(Pair(r.first, ranges2))
                        matched = true
                    }
                }
                ">" -> {
                    if (r.second[rule[0]]!!.contains(rule[2].toInt())) {
                        val ranges = HashMap(r.second)
                        ranges[rule[0]] = max(ranges[rule[0]]!!.first, rule[2].toInt()) ..<(ranges[rule[0]]!!.last)
                        queue.add(Pair(rule[3], ranges))
                        val ranges2 = HashMap(r.second)
                        ranges2[rule[0]] = (ranges[rule[0]]!!.last)..(max(ranges[rule[0]]!!.first, rule[2].toInt()))
                        queue.add(Pair(r.first, ranges2))
                        matched = true
                    }
                }
            }
            if (matched) break
        }
        if (!matched) queue.add(Pair(workflows[r.first]!!.result, r.second))
    }
    println(listOf(res1, res2))
}
