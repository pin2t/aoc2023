import java.util.*

val scanner = Scanner(System.`in`)
val instructions = scanner.nextLine()
scanner.nextLine()
val map = HashMap<String, Pair<String, String>>()
val node = Regex("[A-Z]+")
while (scanner.hasNext()) {
    val item = node.findAll(scanner.nextLine()).map { it.value }.toList()
    map.put(item[0], Pair(item[1], item[2]))
}
var steps = 0
var n = "AAA"
while (n != "ZZZ") {
    if (instructions[steps % instructions.length] == 'L')
        n = map[n]!!.first
    else
        n = map[n]!!.second
    steps++
}
val nn = ArrayList(map.keys.filter { it.endsWith('A') })
var steps2 = ArrayList<Int>()
for (i in nn.indices) steps2.add(0)
for (i in nn.indices) {
    while (!nn[i].endsWith('Z')) {
        nn[i] = if (instructions[steps2[i] % instructions.length] == 'L') map[nn[i]]!!.first else map[nn[i]]!!.second
        steps2[i]++
    }
}
fun gcd(a: Long, b: Long): Long {
    if (b == 0L) return a
    return gcd(b, a % b)
}
fun lcm(a: Long, b: Long): Long {
    return a / gcd(a, b) * b
}
var result2: Long = 1
for (s in steps2) result2 = lcm(result2, s.toLong())
println("$steps $result2")
