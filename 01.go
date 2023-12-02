package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	calibrations, calibrations2 := int32(0), int32(0)
	for scanner.Scan() {
		digits := []int32{}
		for _, c := range scanner.Text() {
			if c >= '0' && c <= '9' {
				digits = append(digits, c-'0')
			}
		}
		calibrations += digits[0]*10 + digits[len(digits)-1]
		digits = []int32{}
		for i, c := range scanner.Text() {
			if c >= '0' && c <= '9' {
				digits = append(digits, c-'0')
			}
			for j, n := range []string{"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"} {
				if strings.HasPrefix(scanner.Text()[i:], n) {
					digits = append(digits, int32(j))
				}
			}
		}
		calibrations2 += digits[0]*10 + digits[len(digits)-1]
	}
	fmt.Println(calibrations, calibrations2)
}
