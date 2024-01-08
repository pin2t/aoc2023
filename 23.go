package main

import "os"
import "bufio"
import "fmt"

var grid []string

type pos struct { x, y int }

var splits []pos
var graph = make(map[pos]map[pos]int)
var seen = make(map[pos]bool)

func maxi(a int, b int) int { if a > b { return a }; return b }

func _build(from pos, p pos, l int, dirs map[uint8][]struct {dx, dy int}) {
	if seen[p] { return }
	seen[p] = true
	for _,sp := range splits {
		if p == sp && p != from {
			if graph[from] == nil {
				graph[from] = make(map[pos]int)
			}
			graph[from][p] = l
			return
		}
	}
	for _, dir := range dirs[grid[p.y][p.x]] {
		var next = pos{p.x + dir.dx, p.y + dir.dy}
		if next.x >= 0 && next.y >= 0 && next.x < len(grid[p.y]) && next.y < len(grid) && grid[next.y][next.x] != '#' {
			_build(from, next, l + 1, dirs)
		}
	}
}

func build(dirs map[uint8][]struct {dx, dy int}) {
	graph = make(map[pos]map[pos]int)
	seen = make(map[pos]bool)
	for _, p := range splits {
		seen = make(map[pos]bool)
		_build(p, p, 0, dirs)
	}
}

var part1dirs = map[uint8][]struct {dx, dy int} {
	'^': {{0, -1}},
	'v': {{0, 1}},
	'<': {{-1, 0}},
	'>': {{1, 0}},
	'.': {{0, -1}, {0, 1}, {-1, 0}, {1, 0}},
}

var part2dirs = map[uint8][]struct {dx, dy int} {
	'^': {{0, -1}, {0, 1}, {-1, 0}, {1, 0}},
	'v': {{0, -1}, {0, 1}, {-1, 0}, {1, 0}},
	'<': {{0, -1}, {0, 1}, {-1, 0}, {1, 0}},
	'>': {{0, -1}, {0, 1}, {-1, 0}, {1, 0}},
	'.': {{0, -1}, {0, 1}, {-1, 0}, {1, 0}},
}

var start, end pos

func _longest(p pos) (result int) {
	if p == end { return 0 }
	if seen[p] { return -1000000000 }
	seen[p] = true
    result = -1000000000
	for next, l := range graph[p] {
		result = maxi(result, l + _longest(next))
	}
	seen[p] = false
	return
}

func longest() (result int) {
	seen = make(map[pos]bool)
	return _longest(start)
}

func main() {
	var scanner = bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		grid = append(grid, scanner.Text())
	}
	for x, _ := range grid[0] {
		if grid[0][x] == '.' {
			start = pos{ x, 0 }
		}
		if grid[len(grid) - 1][x] == '.' {
			end = pos{ x, len(grid) - 1 }
		}
	}
	splits = append(splits, start)
	splits = append(splits, end)
	for y, _ := range grid {
		for x, c := range grid[y] {
			if c != '#' {
				var neightbors = 0
				for _, dir := range []struct { dx, dy int }{{1, 0}, {0, 1}, {-1, 0}, {0, -1}} {
					var n = pos{x + dir.dx, y + dir.dy}
					if n.x >= 0 && n.y >= 0 && n.x < len(grid[y]) && n.y < len(grid) && grid[n.y][n.x] != '#' {
						neightbors++
					}
				}
				if neightbors >= 3 {
					splits = append(splits, pos{x, y})
				}
			}
		}
	}
	build(part1dirs)
	var part1 = longest()
	build(part2dirs)
	fmt.Println(part1, longest())
}
