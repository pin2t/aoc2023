package main
import "fmt"
import "bufio"
import "os"

type beam struct { x, y, dx, dy int }

var grid = make([]string, 0, 100)

func energized(b beam) int {
	var result = make(map[struct{x, y int}]bool)
	var processed = make(map[beam]bool)
	var queue = make([]beam, 0, 10000)
	var move = func (b beam, dx int, dy int) {
		queue = append(queue, beam{b.x + dx, b.y + dy, dx, dy})
	}
	queue = append(queue, b)
	for len(queue) > 0 {
		var b = queue[0]; queue = queue[1:]
		if b.x < 0 || b.x >= len(grid[0]) || b.y < 0 || b.y >= len(grid) { continue }
		if processed[b] { continue }
		processed[b] = true
		result[struct{x, y int}{b.x, b.y}] = true
		switch (grid[b.y][b.x]) {
		case '.': move(b, b.dx, b.dy)
		case '-':
			if b.dy == 0 {
				move(b, b.dx, b.dy)
			} else {
				move(b, 1, 0)
				move(b, -1, 0)
			}
		case '|':
			if b.dx == 0 {
				move(b, b.dx, b.dy)
			} else {
				move(b, 0, 1)
				move(b, 0, -1)
			}
		case '/':
			if b.dx == 1 && b.dy == 0 { move(b, 0, -1) } else
			if b.dx == 0 && b.dy == 1 { move(b, -1, 0) } else
			if b.dx == -1 && b.dy == 0 { move(b, 0, 1) } else
			if b.dx == 0 && b.dy == -1 { move(b, 1, 0) }
		case '\\':
			if b.dx == 1 && b.dy == 0 { move(b, 0, 1) } else
			if b.dx == 0 && b.dy == 1 { move(b, 1, 0) } else
			if b.dx == -1 && b.dy == 0 { move(b, 0, -1) } else
			if b.dx == 0 && b.dy == -1 { move(b, -1, 0) }
		default: panic("unknown symbol " + string(grid[b.y][b.x]))
		}
	}
	return len(result)
}

func maxi(a, b int) int {
	if a > b {
		return a
	}
	return b
}

func main() {
	var scanner = bufio.NewScanner(os.Stdin)
	for scanner.Scan() { grid = append(grid, scanner.Text()) }
    var result2 = 0
    for y, _ := range grid {
        result2 = maxi(result2, energized(beam{0, y, 1, 0}))
        result2 = maxi(result2, energized(beam{len(grid[0]) -1, y, -1, 0}))
    }
    for x, _ := range grid[0] {
        result2 = maxi(result2, energized(beam{x, 0, 0, 1}))
        result2 = maxi(result2, energized(beam{x, len(grid) - 1, 0, -1}))
    }
	fmt.Println(energized(beam{0, 0, 1, 0}), result2)
}
