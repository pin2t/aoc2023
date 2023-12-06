val input = System.`in`.bufferedReader().readLines()
val number = Regex("\\d+")
val times = number.findAll(input[0]).map { it.value.toInt() }.toList()
val distances = number.findAll(input[1]).map { it.value.toInt() }.toList()
var ways = 1
for (i in times.indices) {
    ways *= (0..times[i]).count { (it * (times[i] - it)) > distances[i] }
}
val distance = number.find(input[1].replace(" ", ""))!!.value.toLong()
val time = number.find(input[0].replace(" ", ""))!!.value.toLong()
val wins = (0..time).count { (it * (time - it)) > distance }
println("$ways $wins")
