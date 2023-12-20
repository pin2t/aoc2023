fun gcd(a: Long, b: Long): Long {
    if (b == 0L) return a
    return gcd(b, a % b)
}

fun lcm(a: Long, b: Long): Long {
    return a / gcd(a, b) * b
}

fun main() {
    val modules = HashMap<String, ArrayList<String>>()
    var on = HashMap<String, Boolean>()
    var conjunctions = HashMap<String, HashMap<String, Boolean>>()
    while (true) {
        val line = readlnOrNull() ?: break
        val name = line.split(" -> ")[0]
        modules[name] = ArrayList(line.split(" -> ")[1].split(',').map { it.trim() }.toList())
        if (name.startsWith('%')) on[name] = false
    }
    for (m in modules) {
        for (i in 0..<m.value.size) {
            if (modules.contains("%" + m.value[i])) m.value[i] = "%" + m.value[i]
            if (modules.contains("&" + m.value[i])) m.value[i] = "&" + m.value[i]
        }
    }
    for (m in modules) {
        if (m.key.startsWith('&')) conjunctions[m.key] = HashMap()
    }
    for (m in modules) {
        for (o in m.value) {
            if (conjunctions.contains(o)) conjunctions[o]!![m.key] = false
        }
    }
    val pulses = ArrayDeque<Pair<Boolean, String>>()
    val sent = HashMap<Boolean, Long>(); 
    sent[false] = 0; sent[true] = 0
    fun send(from: String, to: String, value: Boolean) {
        if (conjunctions.contains(to)) conjunctions[to]!![from] = value
        pulses.add(Pair(value, to))
        sent[value] = sent[value]!! + 1
    }
    for (i in 1..1000) {
        send("button", "broadcaster", false)
        while (pulses.isNotEmpty()) {
            var pulse = pulses.removeFirst()
            val value = pulse.first
            val to = pulse.second
            if (!modules.contains(to)) continue
            if (to == "broadcaster") {
                for (output in modules[to]!!) {
                    send(to, output, value)
                }
            } else if (to.startsWith('%')) {
                if (!value) {
                    on[to] = !on[to]!!
                    for (output in modules[to]!!) {
                        send(to, output, on[to]!!)
                    }
                }
            } else if (to.startsWith('&')) {
                for (output in modules[to]!!) {
                    send(to, output, !conjunctions[to]!!.all { it.value })
                }
            }
        }
    }
    val res1 = sent[false]!! * sent[true]!!
    on = HashMap(on.map { Pair(it.key, false) }.toMap())
    conjunctions = HashMap(conjunctions.map { Pair(it.key, HashMap(it.value.map { dest -> Pair(dest.key, false) }.toMap())) }.toMap())
    val cycles = HashMap<String, Long>()
    var press = 1
    while (true) {
        send("button", "broadcaster", false)
        while (pulses.isNotEmpty()) {
            val pulse = pulses.removeFirst()
            val to = pulse.second
            val value = pulse.first
            if (conjunctions["&zg"]!!.contains(to)) {
                if (!cycles.contains(to) && conjunctions["&zg"]!![to]!!) {
                    cycles[to] = press.toLong()
                }
                if (cycles.keys == conjunctions["&zg"]!!.keys) {
                    println(listOf(res1, cycles.values.reduce { acc, c -> lcm(acc, c) }))
                    return
                }
            }
            if (!modules.contains(to)) continue
            if (to == "broadcaster") {
                for (output in modules[to]!!) {
                    send(to, output, value)
                }
            } else if (to[0] == '%') {
                if (!value) {
                    on[to] = !on[to]!!
                    for (output in modules[to]!!) {
                        send(to, output, on[to]!!)
                    }
                }
            } else if (to[0] == '&') {
                for (output in modules[to]!!) {
                    send(to, output, !conjunctions[to]!!.all { it.value })
                }
            }
        }
        press++
    }
}
