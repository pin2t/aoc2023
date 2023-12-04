import java.util.*

var points = 0
val number = Regex("\\d+")
var cards = System.`in`.bufferedReader().readLines()
var copies = ArrayList<Int>()
cards.forEach { copies.add(1) }
for (i in cards.indices) {
    val winning = number.findAll(cards[i].split("|")[0].split(":")[1]).map { it.value }
    var p = 0
    var j = 1
    for (n in number.findAll(cards[i].split("|")[1]).map { it.value }) {
        if (winning.contains(n)) {
            if (p == 0) p = 1 else p *= 2
            copies[i + (j++)] += copies[i]
        }
    }
    points += p
}
println("$points ${copies.sum()}")
