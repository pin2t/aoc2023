val chars = arrayOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
enum class Type { HIGH, ONE_PAIR, TWO_PAIR, THREE_OF_KIND, FULL_HOUSE, FOUR_OF_KIND, FIVE_OF_KIND }
data class Hand(val cards: String): Comparable<Hand> {
    fun type(): Type {
        val counts = HashMap<Char, Int>(5)
        cards.forEach { counts.merge(it, 1, Int::plus) }
        val s = counts.values.toList().sorted()
        if (s.size == 1) return Type.FIVE_OF_KIND
        if (s.size == 2 && s[0] == 1) return Type.FOUR_OF_KIND
        if (s.size == 2 && s[0] == 2 && s[1] == 3) return Type.FULL_HOUSE
        if (s.find { it == 3 } != null) return Type.THREE_OF_KIND
        if (s.size == 3 && s[0] == 1 && s[1] == 2 && s[2] == 2) return Type.TWO_PAIR
        if (s.size == 4 && s[0] == 1 && s[1] == 1 && s[2] == 1 && s[3] == 2) return Type.ONE_PAIR
        return Type.HIGH
    }

    override fun compareTo(other: Hand): Int {
        if (other.type().ordinal < this.type().ordinal) return 1
        if (other.type().ordinal > this.type().ordinal) return -1;
        for (i in cards.indices) {
            if (chars.indexOf(this.cards[i]) < chars.indexOf(other.cards[i])) return 1
            if (chars.indexOf(this.cards[i]) > chars.indexOf(other.cards[i])) return -1
        }
        return 0
    }
}

data class BestHand(val hand: Hand): Comparable<BestHand> {
    constructor(cards: String): this(hand = Hand(cards))

    fun type(): Type {
        return chars.map { Hand(hand.cards.replace('J', it)).type() }.maxBy { it.ordinal }
    }

    override fun compareTo(other: BestHand): Int {
        if (other.type().ordinal < this.type().ordinal) return 1
        if (other.type().ordinal > this.type().ordinal) return -1;
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
val bestTotal = besthands.indices.map { (it + 1) * besthands[it].second }.reduce(Int::plus)
println("$total $bestTotal")
