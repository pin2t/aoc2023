val input = System.`in`.bufferedReader().readLines()
val number = Regex("\\d+")
val times = number.findAll(input[0]).map { it.value.toInt() }.toList()
val distances = number.findAll(input[1]).map { it.value.toInt() }.toList()
var ways = 1
for (i in times.indices) {
    var wins = 0
    for (j in 0..times[i]) {
        if (j * (times[i]-j) > distances[i]) wins++
    }
    ways *= wins
}
var wins = 0
val distance = number.findAll(input[1].replace(" ", "")).map { it.value.toLong() }.first()
val time = number.findAll(input[0].replace(" ", "")).map { it.value.toLong() }.first()
for (j in 0..time) {
    if (j * (time-j) > distance) wins++
}
println("$ways $wins")
