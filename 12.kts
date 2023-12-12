import java.util.*

var scanner = Scanner(System.`in`)
var sum: Long = 0; var sum2: Long = 0
while (scanner.hasNext()) {
    val line = scanner.nextLine()
    var springs = line.split(' ')[0]
    val sizes = ArrayList(line.split(' ')[1].split(',').map { it.toInt() }.toList())
    var matches: Long = 0
    val cache = HashMap<Pair<Int, Int>, Long>()
    fun matched(prefix: String): Boolean {
        var result = true
        prefix.forEachIndexed { index, c -> result = result && (c == springs[index] || springs[index] == '?') }
        return result
    }
    fun match(prefix: String, group: Int) {
        if (prefix.length <= springs.length && cache.contains(Pair(prefix.length, group))) {
            matches += cache[Pair(prefix.length, group)]!!
            return
        }
        if (group == sizes.size) {
            if (prefix.length <= springs.length && matched(prefix + ".".repeat(springs.length - prefix.length))) { matches++ }
            return
        }
        for (i in 0..<(springs.length - sizes[group])) {
            var p = prefix;
            if (group > 0) p += "."
            p += ".".repeat(i) + "#".repeat(sizes[group])
            if (p.length <= springs.length && matched(p)) {
                val prev = matches
                match(p, group + 1)
                cache[Pair(p.length, group + 1)] = matches - prev
            }
        }
    }
    match("", 0)
    sum += matches
    matches = 0
    cache.clear()
    val sizes2 = ArrayList<Int>(sizes);
    sizes.addAll(sizes2); sizes.addAll(sizes2); sizes.addAll(sizes2); sizes.addAll(sizes2)
    springs = "$springs?$springs?$springs?$springs?$springs"
    match("", 0)
    sum2 += matches
}
println(listOf(sum, sum2))
