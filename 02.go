package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

func maxi(a, b int) int {
	if a > b {
		return a
	}
	return b
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	sum, powers := 0, 0
	for scanner.Scan() {
		game := strings.Split(scanner.Text(), ": ")
		mins := map[string]int{}
		for _, show := range strings.Split(game[1], ";") {
			for _, c := range strings.Split(show, ",") {
				cubes := strings.Split(strings.TrimSpace(c), " ")
				n, _ := strconv.ParseInt(cubes[0], 10, 32)
				mins[cubes[1]] = maxi(mins[cubes[1]], int(n))
			}
		}
		if mins["red"] <= 12 && mins["green"] <= 13 && mins["blue"] <= 14 {
			n, _ := strconv.ParseInt(strings.Split(game[0], " ")[1], 10, 32)
			sum += int(n)
		}
		powers += mins["red"] * mins["green"] * mins["blue"]
	}
	fmt.Println(sum, powers)
}
