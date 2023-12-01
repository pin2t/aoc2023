import java.util.*
import kotlin.collections.ArrayList

var n = 0
var n2 = 0
val spell = arrayOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
val scanner = Scanner(System.`in`)
while (scanner.hasNext()) {
    val line = scanner.nextLine()
    val digits = ArrayList<Int>()
    val digits2 = ArrayList<Int>()
    for (i in line.indices) {
        if (line[i].isDigit()) {
            digits.add(line[i].minus('0'))
            digits2.add(line[i].minus('0'))
        }
        for (j in spell.indices) {
            if (line.substring(i).startsWith(spell[j])) {
                digits2.add(j)
            }
        }
    }
    n += digits.first() * 10 + digits.last()
    n2 += digits2.first() * 10 + digits2.last()
}
println("$n $n2")
