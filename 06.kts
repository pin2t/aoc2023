val input = System.`in`.bufferedReader().readLines()
val number = Regex("\\d+")
val times = number.findAll(input[0]).map { it.value.toInt() }.toList()
val distances = number.findAll(input[1]).map { it.value.toInt() }.toList()
var ways = 1
for (i in times.indices) {
    ways *= (0..times[i]).filter { (it * (times[i]-it)) > distances[i] }.count()
}
val distance = number.findAll(input[1].replace(" ", "")).map { it.value.toLong() }.first()
val time = number.findAll(input[0].replace(" ", "")).map { it.value.toLong() }.first()
val wins = (0..time).filter { (it * (time-it)) > distance }.count()
println("$ways $wins")
