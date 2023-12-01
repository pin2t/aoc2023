var n = 0
var n2 = 0
val spell = arrayOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
var line = readLine()
while (line != null) {
    var digits = ArrayList<Int>()
    var digits2 = ArrayList<Int>()
    for (i in 0..(line!!.length - 1)) {
        if (line!![i].isDigit()) {
            digits.add(line!![i].minus('0'))
            digits2.add(line!![i].minus('0'))
        }
        for (j in 1..9) {
            if (line!!.substring(i).startsWith(spell[j - 1])) {
                digits2.add(j)
            }
        }
    }
    n += digits.first() * 10 + digits.last()
    n2 += digits2.first() * 10 + digits2.last()
    line = readLine()
}
println("$n $n2")
