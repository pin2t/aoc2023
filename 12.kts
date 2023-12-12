import java.util.*

var springs: String = ""
var sizes: ArrayList<Int> = ArrayList<Int>()
val cache = HashMap<Pair<Int, Int>, Long>()
fun matched(prefix: String): Boolean {
    return prefix.filterIndexed { index, c -> c == springs[index] || springs[index] == '?' }.count() == prefix.length
}
fun matches(prefix: String, group: Int): Long {
    if (prefix.length > springs.length) return 0
    if (cache.contains(Pair(prefix.length, group))) {
        return cache[Pair(prefix.length, group)]!!
    }
    if (group == sizes.size) {
        return if (matched(prefix + ".".repeat(springs.length - prefix.length))) 1 else 0
    }
    var result: Long = 0
    for (i in 0..<(springs.length - sizes[group])) {
        var p = prefix;
        if (group > 0) p += "."
        p += ".".repeat(i) + "#".repeat(sizes[group])
        if (p.length <= springs.length && matched(p)) result += matches(p, group + 1)
    }
    cache[Pair(prefix.length, group)] = result
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
