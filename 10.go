package main

import "bufio"
import "os"
import "fmt"

type pos struct { col, row int }
var up = pos{0, -1}; var right = pos{1, 0}; var down = pos{0, 1}; var left = pos{-1, 0}

func main() {
	var scanner = bufio.NewScanner(os.Stdin)
	var r, rows, cols = 0, 0, 0
	var start pos
	var grid, loop = make(map[pos]bool, 100000), make(map[pos]bool, 100000)
	var set = func(c int, r int) { grid[pos{c, r}] = true; }

	//      .*.      ...      .*.      .*.      ...      ...      ...      .*.
	// | -> .*. - -> *** L -> .** J -> **. 7 -> **. F -> .** . -> ... S -> ***
	//      .*.      ...      ...      ...      .*.      .*.      ...      .*.
	for scanner.Scan() {
		cols = len(scanner.Text())
		for c, tile := range scanner.Text() {
			switch tile {
			case '|': set(c * 3 + 1, r * 3); set(c * 3 + 1, r * 3 + 1); set(c * 3 + 1, r * 3 + 2)
			case '-': set(c * 3, r * 3 + 1); set(c * 3 + 1, r * 3 + 1); set(c * 3 + 2, r * 3 + 1)
			case 'L': set(c * 3 + 1, r * 3); set(c * 3 + 1, r * 3 + 1); set(c * 3 + 2, r * 3 + 1)
			case 'J': set(c * 3 + 1, r * 3); set(c * 3 + 1, r * 3 + 1); set(c * 3, r * 3 + 1)
			case '7': set(c * 3, r * 3 + 1); set(c * 3 + 1, r * 3 + 1); set(c * 3 + 1, r * 3 + 2)
			case 'F': set(c * 3 + 1, r * 3 + 2); set(c * 3 + 1, r * 3 + 1); set(c * 3 + 2, r * 3 + 1)
			case 'S': set(c * 3 + 1, r * 3); set(c * 3 + 1, r * 3 + 1); set(c * 3 + 1, r * 3 + 2);
			          set(c * 3, r * 3 + 1); set(c * 3 + 2, r * 3 + 1); start = pos{c * 3 + 1, r * 3 + 1}
			}
		}
		r++
	}
	rows = r
	var p = start
	var move = func (dir pos) bool {
		var dest = pos{p.col + dir.col, p.row + dir.row}
		if grid[dest] && !loop[dest] { p = dest; return true }
		return false
	}
	loop[p] = true
	if grid[pos{start.col + 2, start.row}] { p = pos{p.col + 1, p.row} } else
	if grid[pos{start.col - 2, start.row}] { p = pos{p.col - 1, p.row} } else
	if grid[pos{start.col, start.row + 2}] { p = pos{p.col, p.row + 1} } else
	if grid[pos{start.col, start.row - 2}] { p = pos{p.col, p.row - 2} }
	var moved = true
	for moved {
		loop[p] = true
		moved = move(up) || move(right) || move(down) || move(left)
	}
	var inner = func (c int, r int) bool {
		if loop[pos{c, r}] { return false }
		var processed, queue = make(map[pos]bool, 100000), make([]pos, 0, 100000)
		queue = append(queue, pos{c, r})
		for len(queue) > 0 {
			var p = queue[0]; queue = queue[1:]
			if processed[p] { continue }
			processed[p] = true
			if p.col < 0 || p.row < 0 || p.row >= rows * 3 || p.col >= cols * 3 { return false }
			var expand = func (dir pos) {
				var dest = pos{p.col + dir.col, p.row + dir.row}
				if !loop[dest] { queue = append(queue, dest) }
			}
			expand(up); expand(right); expand(down); expand(left)
		}
		return true
	}
	var inners = 0
	for r := 0; r < rows; r++ {
		for c := 0; c < cols; c++ { if inner(c * 3 + 1, r * 3 + 1) { inners++ } }
	}
	fmt.Println(len(loop) / 6, inners)
}
