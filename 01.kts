import java.util.*
import kotlin.collections.ArrayList

var n = 0
var n2 = 0
val numbers = arrayOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
val scanner = Scanner(System.`in`)
val digits = ArrayList<Int>()
while (scanner.hasNext()) {
    val line = scanner.nextLine()
    digits.clear()
    line.forEach { if (it.isDigit()) digits.add(it.minus('0')) }
    n += digits.first() * 10 + digits.last()
    digits.clear()
    line.forEachIndexed { index, c ->
        if (c.isDigit()) digits.add(c.minus('0'))
        numbers.forEachIndexed { spellIndex, _ -> if (line.substring(index).startsWith(numbers[spellIndex])) digits.add(spellIndex) }
    }
    n2 += digits.first() * 10 + digits.last()
}
println("$n $n2")
