package main
import "regexp"
import "bufio"
import "os"
import "fmt"
import "strconv"

var reNumber = regexp.MustCompile("\\d+")

type brick struct { sx, sy, sz, ex, ey, ez int }

var bricks []brick

func mini(a int, b int) int { if a < b { return a }; return b }
func maxi(a int, b int) int { if a > b { return a }; return b }

func (b brick) intersect(other brick) bool {
	return maxi(b.sx, other.sx) <= mini(b.ex, other.ex) &&
           maxi(b.sy, other.sy) <= mini(b.ey, other.ey) &&
           maxi(b.sz, other.sz) <= mini(b.ez, other.ez)
}

func (b brick) fall() brick {
	return brick{ b.sx, b.sy, b.sz - 1, b.ex, b.ey, b.ez - 1 }
}

func (b brick) canFall(bricks []brick) bool {
	var fallen = b.fall()
	for _, bb := range bricks {
		if b != bb && fallen.intersect(bb) {
			return false
		}
	}
	return b.sz > 1
}

func main() {
	var scanner = bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		sn := reNumber.FindAllString(scanner.Text(), -1)
		sx, _ := strconv.ParseInt(sn[0], 10, 32)
		sy, _ := strconv.ParseInt(sn[1], 10, 32)
		sz, _ := strconv.ParseInt(sn[2], 10, 32)
		ex, _ := strconv.ParseInt(sn[3], 10, 32)
		ey, _ := strconv.ParseInt(sn[4], 10, 32)
		ez, _ := strconv.ParseInt(sn[5], 10, 32)
		bricks = append(bricks, brick{ int(sx), int(sy), int(sz), int(ex), int(ey), int(ez) })
	}
	for i, _ := range bricks {
		for bricks[i].canFall(bricks) {
			bricks[i] = bricks[i].fall()
		}
	}
	var part1, part2 = 0, 0
	for i, b := range bricks {
		var prev = b
		bricks[i] = brick{ -1, -1, -1, -1, -1, -1 }
		var canFall = false
		for j, bb := range bricks {
			if i != j && bb.canFall(bricks) {
				canFall = true
				break
			}
		}
		if canFall {
            var fallen = make([]brick, len(bricks))
			if copy(fallen, bricks) != len(bricks) { panic("copy failed") }
            for j, bb := range fallen {
                if i != j && bb.canFall(fallen) {
                    part2++
					for fallen[j].canFall(fallen) {
						fallen[j] = fallen[j].fall()
					}
                }
            }
		} else {
			part1++
		}
		bricks[i] = prev
	}
	fmt.Println(part1, part2)
}
