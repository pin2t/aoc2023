import java.util.*

var scanner = Scanner(System.`in`)
var sum: Long = 0; var sum2: Long = 0
while (scanner.hasNext()) {
    val line = scanner.nextLine()
    val springs = line.split(' ')[0]
    val sizes = ArrayList<Int>(line.split(' ')[1].split(',').map { it.toInt() }.toList())
    var matchCount: Long = 0
    val cache = HashMap<Pair<Int, Int>, Long>()
    fun matchAll(springs: String, sizes: List<Int>, a: String, i: Int) {
        if (a.length <= springs.length && cache.contains(Pair(a.length, i))) {
            matchCount += cache.get(Pair(a.length, i))!!
            return
        }
        if (i == sizes.size) {
            if (a.length <= springs.length) {
                var matched = true
                (a + ".".repeat(springs.length - a.length)).forEachIndexed { index, c ->
                    matched = matched && (c == springs[index] || springs[index] == '?')
                }
                if (matched) { matchCount++ }
            }
            return
        }
        for (ii in 0..(springs.length - sizes[i] - 1)) {
            var aa = a;
            if (i > 0) aa += "."
            aa += ".".repeat(ii) + "#".repeat(sizes[i])
            if (aa.length <= springs.length) {
                var matched = true
                aa.forEachIndexed { index, c -> matched = matched && (c == springs[index] || springs[index] == '?') }
                if (matched) {
                    var prev = matchCount
                    matchAll(springs, sizes, aa, i + 1)
                    cache.put(Pair(aa.length, i + 1), matchCount - prev)
                }
            }
        }
    }
    matchAll(springs, sizes, "", 0)
    sum += matchCount
    matchCount = 0
    cache.clear()
    val sizes2 = ArrayList<Int>(sizes)
    sizes2.addAll(sizes)
    sizes2.addAll(sizes)
    sizes2.addAll(sizes)
    sizes2.addAll(sizes)
    matchAll(springs + "?" + springs + "?" + springs + "?" + springs + "?" + springs, sizes2, "", 0)
    sum2 += matchCount
}
println(listOf(sum, sum2))
