val chars = arrayOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
data class Hand(val cards: String): Comparable<Hand> {
    fun type(): Int {
        val counts = HashMap<Char, Int>(5)
        cards.forEach { counts.merge(it, 1, Int::plus) }
        val s = counts.values.toList().sorted()
        if (s.size == 1) return 7
        if (s.size == 2 && s[0] == 1) return 6
        if (s.size == 2 && s[0] == 2 && s[1] == 3) return 5
        if (s.find { it == 3 } != null) return 4
        if (s.size == 3 && s[0] == 1 && s[1] == 2 && s[2] == 2) return 3
        if (s.size == 4 && s[0] == 1 && s[1] == 1 && s[2] == 1 && s[3] == 2) return 2
        return 1
    }

    override fun compareTo(other: Hand): Int {
        if (other.type() < this.type()) return 1
        if (other.type() > this.type()) return -1;
        for (i in cards.indices) {
            if (chars.indexOf(this.cards[i]) < chars.indexOf(other.cards[i])) return 1
            if (chars.indexOf(this.cards[i]) > chars.indexOf(other.cards[i])) return -1
        }
        return 0
    }
}

data class BestHand(val hand: Hand): Comparable<BestHand> {
    constructor(cards: String): this(hand = Hand(cards))

    private fun type(): Int {
        return chars.map { Hand(hand.cards.replace('J', it)).type() }.max()
    }

    override fun compareTo(other: BestHand): Int {
        if (other.type() < this.type()) return 1
        if (other.type() > this.type()) return -1;
        val chars = arrayOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')
        for (i in this.hand.cards.indices) {
            if (chars.indexOf(this.hand.cards[i]) < chars.indexOf(other.hand.cards[i])) return 1
            if (chars.indexOf(this.hand.cards[i]) > chars.indexOf(other.hand.cards[i])) return -1
        }
        return 0
    }
}

val input = System.`in`.bufferedReader().readLines()
val hands = ArrayList<Pair<Hand, Int>>(input.map {Pair(Hand(it.split(" ")[0]), it.split(" ")[1].toInt()) }.toList()).sortedBy { it.first }
val besthands = ArrayList<Pair<BestHand, Int>>(input.map {Pair(BestHand(it.split(" ")[0]), it.split(" ")[1].toInt()) }.toList()).sortedBy { it.first }
val total = hands.indices.map { (it + 1) * hands[it].second }.reduce(Int::plus)
val bestTotal = besthands.indices.map { (it + 1) * hands[it].second }.reduce(Int::plus)
println("$total $bestTotal")
