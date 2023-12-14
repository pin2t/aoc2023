package main

import "bufio"
import "os"
import "fmt"

func main() {
	var scanner = bufio.NewScanner(os.Stdin)
	var galaxies = make([][]int64, 0, 1000)
	var rows = int64(0)
	var cols = int64(0)
	for scanner.Scan() {
		cols = int64(len(scanner.Text()))
		for i, c := range scanner.Text() {
			if c == '#' { galaxies = append(galaxies, []int64{int64(i), rows}) }
		}
		rows++
	}
	var galaxies2 = make([][]int64, len(galaxies))
	for i := 0; i < len(galaxies); i++ { galaxies2[i] = []int64{galaxies[i][0], galaxies[i][1]}}
	for r := rows - 1; r >= 0; r-- {
		var empty = true
		for _, g := range galaxies { if g[1] == r { empty = false; break } }
		if empty {
			for i := 0; i < len(galaxies); i++ {
				if galaxies[i][1] > r { galaxies[i][1] = galaxies[i][1] + 1 }
				if galaxies2[i][1] > r { galaxies2[i][1] += 999999 }
			}
		}
	}
	for c := cols - 1; c >= 0; c-- {
		var empty = true
		for _, g := range galaxies { if g[0] == c { empty = false; break } }
		if empty {
			for i := 0; i < len(galaxies); i++ {
				if galaxies[i][0] > c { galaxies[i][0] = galaxies[i][0] + 1 }
				if galaxies2[i][0] > c { galaxies2[i][0] += 999999 }
			}
		}
	}
	var abs = func (a int64) int64 { if a < 0 { return -a }; return a }
	var sum, sum2 = int64(0), int64(0)
	for i := 0; i < len(galaxies) - 1; i++ {
		for j := i + 1; j < len(galaxies); j++ {
			var gi, gj = galaxies[i], galaxies[j]
			sum += abs(gi[0] - gj[0]) + abs(gi[1] - gj[1])
			gi, gj = galaxies2[i], galaxies2[j]
			sum2 += abs(gi[0] - gj[0]) + abs(gi[1] - gj[1])
		}
	}
	fmt.Println(sum, sum2)
}
