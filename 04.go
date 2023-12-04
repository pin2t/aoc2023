package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
	"regexp"
	"strings"
)

func main() {
	reNumber := regexp.MustCompile("\\d+")
	scanner := bufio.NewScanner(os.Stdin)
	copies := make([]int, 11)
	total := 0
	points := 0
	for scanner.Scan() {
		card := scanner.Text()
		p, j := 0, 1
		for _, n := range reNumber.FindAllString(strings.Split(card, "|")[1], -1) {
			for _, nn := range reNumber.FindAllString(strings.Split(strings.Split(card, "|")[0], ":")[1], -1) {
				if n == nn {
					p++
					copies[j] += 1 + copies[0]
					j++
				}
			}
		}
		points += int(math.Pow(float64(2), float64(p-1)))
		total += 1 + copies[0]
		copies = append(copies[1:], 0)
	}
	fmt.Println(points, total)
}
