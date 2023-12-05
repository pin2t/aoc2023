import java.util.*

data class GardenMap(val source: String, val dest: String, val mappings: List<Pair<LongRange, Long>>)

val scanner = Scanner(System.`in`)
val mappings = ArrayList<GardenMap>()
val seeds = ArrayList<Long>()
val number = Regex("\\d+")
while (scanner.hasNext()) {
    val line = scanner.nextLine()
    if (line.startsWith("seeds:")) {
        seeds.addAll(number.findAll(line).map { it.value.toLong() })
    } else if (line.endsWith("map:")) {
        val ranges = ArrayList<Pair<LongRange, Long>>()
        while (scanner.hasNext()) {
            val l = scanner.nextLine()
            if (l == "") { break }
            val range = number.findAll(l).map { it.value.toLong() }.toList()
            ranges.add(Pair(LongRange(range[1], range[1] + range[2] - 1), range[0]))
        }
        mappings.add(GardenMap(line.split(" ")[0].split("-")[0], line.split(" ")[0].split("-")[2], ranges))
    }
}
var minLocation = Long.MAX_VALUE
for (seed in seeds) {
    var mapped = seed
    for (mapping in mappings) {
        for (r in mapping.mappings) {
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
    ranges.add(LongRange(seeds[i], seeds[i] + seeds[i + 1] - 1))
}
for (mapping in mappings) {
    val mapped = ArrayList<LongRange>()
    fun _map(rr: LongRange) {
        var found = false
        for (r in mapping.mappings) {
            val offset = r.second - r.first.first
            if (r.first.contains(rr.first) && r.first.contains(rr.last)) {
                mapped.add(LongRange(rr.first + offset, rr.last + offset))
                found = true
                break
            } else if (r.first.contains(rr.first)) {
                mapped.add(LongRange(rr.first + offset, r.first.last + offset))
                _map(LongRange(r.first.last + 1, rr.last))
                found = true
                break
            } else if (r.first.contains(rr.last)) {
                mapped.add(LongRange(r.first.first + offset, rr.last + offset))
                _map(LongRange(rr.first, r.first.first - 1))
                found = true
                break
            } else if (rr.contains(r.first.first) && rr.contains(r.first.last)) {
                _map(LongRange(rr.first, r.first.first - 1))
                _map(LongRange(r.first.last + 1, rr.last))
                mapped.add(LongRange(r.first.first + offset, r.first.last + offset))
                found = true
                break
            }
        }
        if (!found) mapped.add(rr)
    }
    for (r in ranges) _map(r)
    ranges = mapped
}
println("${minLocation} ${ranges.minOf { it.start }}")
