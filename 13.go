package main

import "bufio"
import "os"
import "fmt"

func mini(a int, b int) int { if a < b { return a }; return b }

func horizontalReflection(grid [][]bool, other int) int {
	for i := 0; i < len(grid) - 1; i++ {
		var height = mini(i + 1, len(grid) - i - 1)
		var reflected = true
		for c := 0; (c < len(grid[0])) && reflected; c++ {
			for j := 1; (j <= height) && reflected; j++ {
				if grid[i + 1 - j][c] != grid[i + j][c] {
					reflected = false
				}
			}
		}
		if reflected && (i + 1) != other { return i + 1 }
	}
	return 0
}

func reflection(grid [][]bool, other int) int {
	var res = 100 * horizontalReflection(grid, other / 100)
	if res == 0 {
		var flipped [][]bool
		for c := 0; c < len(grid[0]); c++ {
			var row []bool
			for r := 0; r < len(grid); r++ { row = append(row, grid[r][c]) }
			flipped = append(flipped, row)
		}
		res = horizontalReflection(flipped, other % 100)
	}
	return res
}

func main() {
	var scanner = bufio.NewScanner(os.Stdin)
	var grid [][]bool
	var part1, part2 = 0, 0
	var process = func() {
		var refl = reflection(grid, 0)
		var other = 0
		for r := 0; r < len(grid) && other == 0; r++ {
			for c := 0; c < len(grid[0]) && other == 0; c++ {
				grid[r][c] = !grid[r][c]
				other = reflection(grid, refl)
				grid[r][c] = !grid[r][c]
			}
		}
		part1 += refl
		part2 += other
	}
	for scanner.Scan() {
		var line = scanner.Text()
		if line == "" {
			process()
			grid = [][]bool{}
			continue
		}
		var row []bool
		for _, c := range line { row = append(row, c == '#') }
		grid = append(grid, row)
	}
	process()
	fmt.Println(part1, part2)
}
