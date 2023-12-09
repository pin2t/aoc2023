import java.util.*

var sum = 0
var prevSum = 0
val scanner = Scanner(System.`in`)
val number = Regex("-?\\d+")
while (scanner.hasNext()) {
    val numbers = number.findAll(scanner.nextLine()).map { it.value.toInt() }.toList()
    var diff = (1..numbers.lastIndex).map { numbers[it] - numbers[it - 1] }
    var next = numbers.last() + diff.last()
    var prev = numbers.first() - diff.first()
    var sign = 1
    while (!diff.all { it == 0 }) {
        diff = (1..diff.lastIndex).map { diff[it] - diff[it - 1] }
        next += diff.last()
        prev += sign * diff.first()
        sign = -sign
    }
    sum += next; prevSum += prev
}
println("$sum $prevSum")
