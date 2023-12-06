package main

import (
	"fmt"
	"math"
	"strconv"
)

// x * (t - x) = d solutions are x1 = t / 2 - sqrt(t * t - 4 * d) / 2 and x2 = t / 2 + sqrt(t * t - 4 * d) / 2
// the winning number of games is difference x2 - x1
func main() {
	var times, distances [4]int
	var prefix string
	fmt.Scanln(&prefix, &times[0], &times[1], &times[2], &times[3])
	fmt.Scanln(&prefix, &distances[0], &distances[1], &distances[2], &distances[3])
	ways := 1
	for i, t := range times {
		x1 := (float64(t) - math.Sqrt(float64(t*t-4*distances[i]))) / 2
		x2 := (float64(t) + math.Sqrt(float64(t*t-4*distances[i]))) / 2
		if float64(int(x1)) == x1 && float64(int(x2)) == x2 {
			ways *= int(x2) - int(x1) - 1
		} else {
			ways *= int(x2) - int(x1)
		}
	}
	t, _ := strconv.ParseInt(strconv.Itoa(times[0])+strconv.Itoa(times[1])+strconv.Itoa(times[2])+strconv.Itoa(times[3]), 10, 64)
	d, _ := strconv.ParseInt(strconv.Itoa(distances[0])+strconv.Itoa(distances[1])+strconv.Itoa(distances[2])+strconv.Itoa(distances[3]), 10, 64)
	x1 := (float64(t) - math.Sqrt(float64(t*t-4*d))) / 2
	x2 := (float64(t) + math.Sqrt(float64(t*t-4*d))) / 2
	fmt.Println(ways, int(x2)-int(x1))
}
