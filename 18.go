package main

import "bufio"
import "fmt"
import "os"
import "strconv"
import "strings"

var directions = map[string]struct{dx, dy int64}{
	"D": {0, 1}, "U": {0, -1}, "R": {1, 0}, "L": {-1, 0},
}
var part2dirs = map[uint8]struct{dx, dy int64}{
	'3': {0, -1}, '1': {0, 1}, '2': {-1, 0}, '0': {1, 0},
}

type pos struct {x, y int64}

func area(corners []pos, l int64) int64 {
	var result int64
	for i := 1; i < len(corners) - 1; i++ {
		result += corners[i].x * (corners[i + 1].y - corners[i - 1].y)
	}
	return result / 2 + l / 2 + 1
}

func main() {
	var scanner = bufio.NewScanner(os.Stdin)
	var part1corners = []pos{{0, 0}}
	var part2corners = []pos{{0, 0}}
	var pos1, pos2, len1, len2 = pos{0, 0}, pos{0, 0}, int64(0), int64(0)
	for scanner.Scan() {
		var items = strings.Split(scanner.Text(), " ")
		{
		var dir, found = directions[items[0]]
		if !found { panic("invalid directions " + items[0]) }
		n, _ := strconv.ParseInt(items[1], 10, 32)
		pos1 = pos{pos1.x + dir.dx * n, pos1.y + dir.dy * n}
		part1corners = append(part1corners, pos1)
		len1 += n
		}
		{
		var dir, found = part2dirs[items[2][7]]
		if !found { panic("invalid directions " + items[0]) }
		n, _ := strconv.ParseInt(items[2][2:7], 16, 32)
		len2 += n
		pos2 = pos{pos2.x + dir.dx * n, pos2.y + dir.dy * n}
		part2corners = append(part2corners, pos2)
		}
	}
	fmt.Println(area(part1corners, len1), area(part2corners, len2))
}
