package main

import "bufio"
import "os"
import "fmt"

//      .*.      ...      .*.      .*.      ...      ...      ...      .*.
// | -> .*. - -> *** L -> .** J -> **. 7 -> **. F -> .** . -> ... S -> ***
//      .*.      ...      ...      ...      .*.      .*.      ...      .*.

type pos struct { col, ro int }



func main() {
	var scanner = bufio.NewScanner(os.Stdin)
	var r, rows, cols = 0, 0, 0
	var start pos
	var grid = make(map[pos]bool, 100000)
	var set = func(c int, r int) {
		grid[pos{c, r}] = true;
	}
	for scanner.Scan() {
		cols = len(scanner.Text())
		for c, tile := range scanner.Text() {
			switch tile {
			case '|': set(c * 3 + 1, r * 3); set(c * 3 + 1, r * 3 + 1); set(c * 3 + 1, r * 3 + 2)
			case '-': set(c * 3, r * 3 + 1); set(c * 3 + 1, r * 3 + 1); set(c * 3 + 2, r * 3 + 2 + 1)
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
	fmt.Println(start, rows, cols)
}
