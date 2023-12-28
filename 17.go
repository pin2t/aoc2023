package main
import "bufio"
import "os"
import "fmt"

var grid []string

type crucible struct { x, y, dx, dy, loss, straights int }
var queue = make([]crucible, 0)

func enqueue(c crucible) {
	for i, qc := range queue {
		if c.loss <= qc.loss {
			queue = append(queue[:i+1], queue[i:]...)
			queue[i] = c
			return
		}
	}
	queue = append(queue, c)
}

func dequeue() (result crucible) {
	result = queue[0]
	queue = queue[1:]
	return
}

func path(canMove func (c crucible, dx int, dy int) bool) int {
	var processed = make(map[struct{x, y, dx, dy, straights int}]bool)
	queue = make([]crucible, 0)
	enqueue(crucible{0, 0, 1, 0, 0, 0})
	for len(queue) > 0 {
		var c = dequeue()
		if c.x == len(grid[0]) - 1 && c.y == len(grid) - 1 { return c.loss }
		var key = struct{x, y, dx, dy, straights int}{c.x, c.y, c.dx, c.dy, c.straights}
		if processed[key] { continue }
		processed[key] = true
		for _, dir := range []struct{dx, dy int}{{1, 0}, {0, 1}, {-1, 0}, {0, -1}} {
			var nextx, nexty = c.x + dir.dx, c.y + dir.dy
			if nextx < 0 || nexty < 0 || nextx >= len(grid[0]) || nexty >= len(grid) { continue }
			if !canMove(c, dir.dx, dir.dy) { continue }
			var straights = c.straights + 1
			if dir.dx != c.dx || dir.dy != c.dy { straights = 1 }
			enqueue(crucible{nextx, nexty, dir.dx, dir.dy, c.loss + int(grid[c.y][c.x] - '0'), straights})
		}
	}
	return 0
}

func main() {
	var scanner = bufio.NewScanner(os.Stdin)
	for scanner.Scan() { grid = append(grid, scanner.Text()) }
	var part1 = path(func (c crucible, dx int, dy int) bool {
		if c.dx == dx && c.dy == dy { return c.straights < 3 }
		if c.dx == 1 && dx != -1 || c.dy == 1 && dy != -1 || c.dx == -1 && dx != 1 ||
			c.dy == -1 && dy != 1 { return true }
		return false
	});
	var part2 = path(func (c crucible, dx int, dy int) bool {
		if c.dx == dx && c.dy == dy { return c.straights < 10 }
		if c.straights >= 4 {
			if c.dx == 1 && dx != -1 || c.dy == 1 && dy != -1 || c.dx == -1 && dx != 1 ||
				c.dy == -1 && dy != 1 { return true }
		}
		return false
	});
	fmt.Println(part1, part2)
}
