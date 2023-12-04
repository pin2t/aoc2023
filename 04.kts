import java.util.*

var points = 0
val numbers = Regex("\\d+")
var cards = System.`in`.bufferedReader().readLines()
var copies = ArrayList<Int>()
cards.forEach { copies.add(1) }
for (i in cards.indices) {
    val winning = numbers.findAll(cards[i].split("|")[0].split(":")[1]).map { it.value.toInt() }.toList()
    val scratched = numbers.findAll(cards[i].split("|")[1]).map { it.value.toInt() }.toList()
    var p = 0
    var j = 1
    for (n in scratched) {
        if (winning.contains(n)) {
            if (p == 0) p = 1 else p *= 2
            copies[i + (j++)] += copies[i]
        }
    }
    points += p
}
println("$points ${copies.sum()}")
