package main

import "strings"
import "bufio"
import "fmt"
import "os"

var grid []string

type pos struct {
	row, col int
}

func count(from pos, steps int) int64 {
	var reach = make(map[pos]bool)
	reach[from] = true
	for i := 1; i <= steps; i++ {
		var nexts = make(map[pos]bool)
		for p, _ := range reach {
			for _, dir := range []struct { dr, dc int }{{0, 1}, {0, -1}, {1, 0}, {-1, 0}} {
				var next = pos{p.row + dir.dr, p.col + dir.dc}
				if next.row >= 0 && next.col >= 0 && next.row < len(grid) && next.col < len(grid[0]) &&
					grid[next.row][next.col] != '#' {
					nexts[next] = true
				}
			}
		}
		reach = nexts
	}
	return int64(len(reach))
}

func main() {
	var start pos
	var scanner = bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		if strings.Contains(scanner.Text(), "S") {
			start = pos{len(grid), strings.Index(scanner.Text(), "S")}
		}
		grid = append(grid, scanner.Text())
	}
    var grids = int64(26501365) / int64(len(grid)) - 1
    var oddGrids = (grids / 2 * 2 + 1) * ((grids / 2 * 2 + 1))
    var evenGrids = ((grids + 1) / 2 * 2) * ((grids + 1) / 2 * 2)
	fmt.Println(count(start, 64),
    	oddGrids * count(start, len(grid) * 2 + 1) +
            evenGrids * count(start, len(grid) * 2) +
            count(pos{len(grid) - 1, start.col}, len(grid) - 1) +
            count(pos{start.row, 0}, len(grid) - 1) +
            count(pos{0, start.col}, len(grid) - 1) +
            count(pos{start.row, len(grid) - 1}, len(grid) - 1) +
            ((grids + 1) * (count(pos{0, len(grid) - 1}, len(grid) / 2 - 1) +
                count(pos{len(grid) - 1, len(grid) - 1}, len(grid) / 2 - 1) +
                count(pos{0, 0}, len(grid) / 2 - 1) +
                count(pos{len(grid) - 1, 0}, len(grid) / 2 - 1))) +
            (grids * (count(pos{0, len(grid) - 1}, len(grid) * 3 / 2 - 1) +
                count(pos{len(grid) - 1, len(grid) - 1}, len(grid) * 3 / 2 - 1) +
                count(pos{0, 0}, len(grid) * 3 / 2 - 1) +
                count(pos{len(grid) - 1, 0}, len(grid) * 3 / 2 - 1))),
	)
}
