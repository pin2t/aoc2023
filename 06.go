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
	i := func(s string) int64 {
		result, _ := strconv.ParseInt(s, 10, 64)
		return result
	}
	s := func(i int) string {
		return strconv.Itoa(i)
	}
	t := i(s(times[0]) + s(times[1]) + s(times[2]) + s(times[3]))
	d := i(s(distances[0]) + s(distances[1]) + s(distances[2]) + s(distances[3]))
	x1 := (float64(t) - math.Sqrt(float64(t*t-4*d))) / 2
	x2 := (float64(t) + math.Sqrt(float64(t*t-4*d))) / 2
	fmt.Println(ways, int(x2)-int(x1))
}
