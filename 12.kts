import java.util.*

var springs: String = ""
var sizes: ArrayList<Int> = ArrayList<Int>()
val cache = HashMap<Pair<Int, Int>, Long>()
fun matched(prefix: String): Boolean {
    var result = true
    prefix.forEachIndexed { index, c -> result = result && (c == springs[index] || springs[index] == '?') }
    return result
}
fun matches(prefix: String, group: Int): Long {
    if (prefix.length <= springs.length && cache.contains(Pair(prefix.length, group))) {
        return cache[Pair(prefix.length, group)]!!
    }
    if (group == sizes.size) {
        if (prefix.length <= springs.length && matched(prefix + ".".repeat(springs.length - prefix.length))) return 1
        return 0
    }
    var result: Long = 0
    for (i in 0..<(springs.length - sizes[group])) {
        var p = prefix;
        if (group > 0) p += "."
        p += ".".repeat(i) + "#".repeat(sizes[group])
        if (p.length <= springs.length && matched(p)) {
            var m = matches(p, group + 1)
            cache[Pair(p.length, group + 1)] = m
            result += m
        }
    }
    return result
}
var sum: Long = 0; var sum2: Long = 0
var scanner = Scanner(System.`in`)
while (scanner.hasNext()) {
    val line = scanner.nextLine()
    springs = line.split(' ')[0]
    sizes = ArrayList(line.split(' ')[1].split(',').map { it.toInt() }.toList())
    cache.clear()
    sum += matches("", 0)
    cache.clear()
    val sizes2 = ArrayList<Int>(sizes);
    sizes.addAll(sizes2); sizes.addAll(sizes2); sizes.addAll(sizes2); sizes.addAll(sizes2)
    springs = "$springs?$springs?$springs?$springs?$springs"
    sum2 += matches("", 0)
}
println(listOf(sum, sum2))
