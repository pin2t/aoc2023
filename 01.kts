import java.util.*
import kotlin.collections.ArrayList

var calibrations = 0
var calibrations2 = 0
val numbers = arrayOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
val scanner = Scanner(System.`in`)
val digits = ArrayList<Int>()
while (scanner.hasNext()) {
    val line = scanner.nextLine()
    digits.clear()
    line.forEach { if (it.isDigit()) digits.add(it.minus('0')) }
    calibrations += digits.first() * 10 + digits.last()
    digits.clear()
    line.forEachIndexed { i, c ->
        if (c.isDigit()) digits.add(c.minus('0'))
        numbers.forEachIndexed { j, _ -> if (line.substring(i).startsWith(numbers[j])) digits.add(j) }
    }
    calibrations2 += digits.first() * 10 + digits.last()
}
println("$calibrations $calibrations2")
