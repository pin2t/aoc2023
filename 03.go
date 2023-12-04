package main

import (
	"bufio"
	"fmt"
	"os"
	"regexp"
	"strconv"
)

type pos struct{ col, row int }

var reNumber = regexp.MustCompile("\\d+")
var field = []string{}

func symbolAround(col, row int, sym func(col, row int) bool) bool {
	symbol := func(col, row int) bool {
		if row < 0 || row >= len(field) || col < 0 || col >= len(field[row]) {
			return false
		}
		return sym(col, row)
	}
	return symbol(col-1, row-1) || symbol(col-1, row) || symbol(col-1, row+1) || symbol(col, row-1) ||
		symbol(col, row+1) || symbol(col+1, row-1) || symbol(col+1, row) || symbol(col+1, row+1)
}

func any(col, row int) bool {
	return field[row][col] != '.' && (field[row][col] < '0' || field[row][col] > '9')
}

var starPos pos

func star(col, row int) bool {
	if field[row][col] == '*' {
		starPos = pos{col, row}
		return true
	}
	return false
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		field = append(field, scanner.Text())
	}
	sum := 0
	gears := map[pos][]int{}
	addGear := func(value int) {
		gears[starPos] = append(gears[starPos], value)
	}
	for r, row := range field {
		for _, ind := range reNumber.FindAllStringIndex(row, -1) {
			n, _ := strconv.ParseInt(row[ind[0]:ind[1]], 10, 32)
			if symbolAround(ind[0], r, any) || symbolAround(ind[1]-1, r, any) {
				sum += int(n)
			}
			if symbolAround(ind[0], r, star) {
				addGear(int(n))
				savePos := starPos
				if symbolAround(ind[1]-1, r, star) && savePos != starPos {
					addGear(int(n))
				}
			} else if symbolAround(ind[1]-1, r, star) {
				addGear(int(n))
			}
		}
	}
	ratios := 0
	for _, numbers := range gears {
		if len(numbers) == 2 {
			ratios += numbers[0] * numbers[1]
		}
	}
	fmt.Println(sum, ratios)
}
