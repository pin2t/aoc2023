import java.util.*

val scanner = Scanner(System.`in`)
var sum = 0
var powers = 0
while (scanner.hasNext()) {
    val line = scanner.nextLine()
    val mins = HashMap<String, Int>()
    for (show in line.split(':')[1].split(';')) {
        for (cubes in show.split(',')) {
            val color = cubes.split(' ')[2]
            mins[color] = Math.max(mins.getOrDefault(color, 0), cubes.split(' ')[1].toInt())
        }
    }
    if (mins["red"]!! <= 12 && mins["green"]!! <= 13 && mins["blue"]!! <= 14) {
        sum += line.split(':')[0].split(' ')[1].toInt()
    }
    powers += mins["red"]!! * mins["green"]!! * mins["blue"]!!
}
println("$sum $powers")
