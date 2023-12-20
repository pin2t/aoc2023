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
    for (m in modules) if (m.key.startsWith('&')) conjunctions[m.key] = HashMap()
    for (m in modules) for (o in m.value) if (conjunctions.contains(o)) conjunctions[o]!![m.key] = false
    val pulses = ArrayDeque<Pair<Boolean, String>>()
    val sent = HashMap<Boolean, Long>(); sent[false] = 0; sent[true] = 0
    fun send(from: String, to: String, value: Boolean) {
        if (conjunctions.contains(to)) conjunctions[to]!![from] = value
        pulses.add(Pair(value, to))
        sent[value] = sent[value]!! + 1
    }
    for (i in 1..1000) {
        pulses.clear()
        send("button", "broadcaster", false)
        while (pulses.isNotEmpty()) {
            var pulse = pulses.removeFirst()
            if (!modules.contains(pulse.second)) continue
            if (pulse.second == "broadcaster") {
                for (output in modules[pulse.second]!!) send(pulse.second, output, pulse.first)
            } else if (pulse.second.startsWith('%')) {
                if (!pulse.first) {
                    on[pulse.second] = !on[pulse.second]!!
                    for (output in modules[pulse.second]!!) send(pulse.second, output, on[pulse.second]!!)
                }
            } else if (pulse.second.startsWith('&')) {
                for (output in modules[pulse.second]!!)
                    send(pulse.second, output, !conjunctions[pulse.second]!!.all { it.value })
            }
        }
    }
    val res1 = sent[false]!! * sent[true]!!
    on = HashMap(on.map { Pair(it.key, false) }.toMap())
    conjunctions = HashMap(conjunctions.map { Pair(it.key, HashMap(it.value.map { dest -> Pair(dest.key, false) }.toMap())) }.toMap())
    val cycles = HashMap<String, Long>()
    var press = 1
    while (true) {
        pulses.clear()
        send("button", "broadcaster", false)
        while (pulses.isNotEmpty()) {
            val pulse = pulses.removeFirst()
            if (conjunctions["&zg"]!!.contains(pulse.second)) {
                if (!cycles.contains(pulse.second) && conjunctions["&zg"]!![pulse.second]!!)
                    cycles[pulse.second] = press.toLong()
                if (cycles.keys == conjunctions["&zg"]!!.keys) {
                    println(listOf(res1, cycles.values.reduce { acc, l -> lcm(acc, l) }))
                    return
                }
            }
            if (!modules.contains(pulse.second)) continue
            if (pulse.second == "broadcaster") {
                for (output in modules[pulse.second]!!) send(pulse.second, output, pulse.first)
            } else if (pulse.second.startsWith('%')) {
                if (!pulse.first) {
                    on[pulse.second] = !on[pulse.second]!!
                    for (output in modules[pulse.second]!!) send(pulse.second, output, on[pulse.second]!!)
                }
            } else if (pulse.second.startsWith('&')) {
                for (output in modules[pulse.second]!!)
                    send(pulse.second, output, !conjunctions[pulse.second]!!.all { it.value })
            }
        }
        press++
    }
}
