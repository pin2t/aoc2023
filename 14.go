package main

import "bufio"
import "os"
import "fmt"

func main() {
	var grid [][]byte
	var scanner = bufio.NewScanner(os.Stdin)
	for scanner.Scan() { grid = append(grid, []byte(scanner.Text())) }
	var apply = func (f func(c int, r int)) {
		for r, _ := range grid {
			for c, _ := range grid[r] {
				f(c, r)
			}
		}
	}
	var state = func () (result []int) {
		apply(func (c int, r int) {
			if grid[r][c] == 'O' {
				result = append(result, len(grid) - r)
			}
		})
		return
	}
	var north = func() {
		apply(func (c int, r int) {
            if grid[r][c] == 'O' {
                grid[r][c] = '.'
				var i = r - 1; for ;i >= 0 && grid[i][c] == '.'; i-- {}
                grid[i + 1][c] = 'O'
            }
		})
	}
	var west = func() {
		apply(func (c int, r int) {
            if grid[r][c] == 'O' {
                grid[r][c] = '.'
                var i = c - 1; for ;i >= 0 && grid[r][i] == '.'; i-- {}
                grid[r][i + 1] = 'O'
            }
		})
	}
	var south = func() {
		for c := 0; c < len(grid[0]); c++ {
			for r := len(grid) - 1; r >= 0; r-- {
	            if grid[r][c] == 'O' {
    	            grid[r][c] = '.'
        	        var i = r + 1; for ;i < len(grid) && grid[i][c] == '.'; i++ {}
            	    grid[i - 1][c] = 'O'
            	}
           }
       }
	}
	var east = func() {
	    for r := 0; r < len(grid); r++ {
    	    for c := len(grid[0]) - 1; c >= 0; c-- {
        	    if grid[r][c] == 'O' {
            	    grid[r][c] = '.'
                	var i = c + 1; for ;i < len(grid[0]) && grid[r][i] == '.'; i++ {}
                	grid[r][i - 1] = 'O'
            	}
			}
		}
	}
	north()
	var load1 = 0; for _, rock := range state() { load1 += rock }
	var states = make(map[string]int64)
	for cycle := int64(1); cycle <= 1000000000; cycle++ {
		north(); west(); south(); east()
		var key = fmt.Sprint(state())
		if v, found := states[key]; found {
			var l = cycle - v
			for cycle < 1000000000 - l {
				cycle += l
			}
			states = make(map[string]int64)
		} else {
			states[key] = cycle
		}
	}
	var load2 = 0;
	for _, rock := range state() {
		load2 += rock
	}
	fmt.Println(load1, load2)
}
