package main
import "regexp"
import "bufio"
import "fmt"
import "strconv"
import "os"

type pos3d struct { x, y, z int64 }
type hailstone struct { p, v pos3d }

var reNumber = regexp.MustCompile("-?\\d+")
var hailstones []hailstone

func main() {
	var scanner = bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		var items = reNumber.FindAllString(scanner.Text(), -1)
		px, _ := strconv.ParseInt(items[0], 10, 64)
		py, _ := strconv.ParseInt(items[1], 10, 64)
		pz, _ := strconv.ParseInt(items[2], 10, 64)
		vx, _ := strconv.ParseInt(items[3], 10, 64)
		vy, _ := strconv.ParseInt(items[4], 10, 64)
		vz, _ := strconv.ParseInt(items[5], 10, 64)
		hailstones = append(hailstones, hailstone{
			pos3d{px, py, pz}, pos3d{vx, vy, vz},
		})
	}
	var part1 = 0
	for i := 0; i < len(hailstones) - 1; i++ {
		for j := i + 1; j < len(hailstones); j++ {
			var h1, h2 = hailstones[i], hailstones[j]
			if h1.v.x * h2.v.y == h1.v.y * h2.v.x { continue }
			var t = ((h2.p.x - h1.p.x) * h2.v.y + (h1.p.y - h2.p.y) * h2.v.x) / (h1.v.x * h2.v.y - h1.v.y * h2.v.x)
			if t < 0 { continue }
			var s = ((h1.p.y - h2.p.y) + t * h1.v.y) / h2.v.y
			if s < 0 { continue }
			var px, py = h1.p.x + t * h1.v.x, h1.p.y + t * h1.v.y
			if px >= 200000000000000 && px <= 400000000000000 &&
				py >= 200000000000000 && py <= 400000000000000 {
				part1++
			}
		}
	}
	fmt.Println(part1)
}