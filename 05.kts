import java.util.*

val scanner = Scanner(System.`in`)
val mappings = ArrayList<List<Pair<LongRange, Long>>>()
val seeds = ArrayList<Long>()
val number = Regex("\\d+")
while (scanner.hasNext()) {
    val line = scanner.nextLine()
    if (line.startsWith("seeds:")) {
        seeds.addAll(number.findAll(line).map { it.value.toLong() })
    } else if (line.endsWith("map:")) {
        val m = ArrayList<Pair<LongRange, Long>>()
        while (scanner.hasNext()) {
            val l = scanner.nextLine()
            if (l == "") { break }
            val mm = number.findAll(l).map { it.value.toLong() }.toList()
            m.add(Pair((mm[1]..<(mm[1] + mm[2])), mm[0]))
        }
        mappings.add(m)
    }
}
var minLocation = Long.MAX_VALUE
for (seed in seeds) {
    var mapped = seed
    for (mapping in mappings) {
        for (r in mapping) {
            if (r.first.contains(mapped)) {
                mapped = r.second + (mapped - r.first.first)
                break
            }
        }
    }
    minLocation = minOf(minLocation, mapped)
}
var ranges = ArrayList<LongRange>()
for (i in 0..<seeds.size step 2) {
    ranges.add((seeds[i]..<seeds[i] + seeds[i + 1]))
}
for (mapping in mappings) {
    val mapped = ArrayList<LongRange>()
    fun map(rr: LongRange) {
        var mr: Optional<LongRange> = Optional.empty()
        for (r in mapping) {
            val offset = r.second - r.first.first
            if (r.first.contains(rr.first) && r.first.contains(rr.last)) {
                mr = Optional.of(LongRange(rr.first + offset, rr.last + offset))
            } else if (r.first.contains(rr.first)) {
                mr = Optional.of(LongRange(rr.first + offset, r.first.last + offset))
                map(LongRange(r.first.last + 1, rr.last))
            } else if (r.first.contains(rr.last)) {
                mr = Optional.of(LongRange(r.first.first + offset, rr.last + offset))
                map(LongRange(rr.first, r.first.first - 1))
            } else if (rr.contains(r.first.first) && rr.contains(r.first.last)) {
                map(LongRange(rr.first, r.first.first - 1))
                map(LongRange(r.first.last + 1, rr.last))
                mr = Optional.of(LongRange(r.first.first + offset, r.first.last + offset))
            }
            if (mr.isPresent) break
        }
        mapped.add(if (mr.isPresent) mr.get() else rr)
    }
    for (r in ranges) map(r)
    ranges = mapped
}
println(listOf(minLocation, ranges.minOf { it.first }))
