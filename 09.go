package main

import "bufio"
import "os"
import "strings"
import "strconv"
import "fmt"

func main() {
	var result1, result2 int
	var scanner = bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		var nums []int
		for _, sn := range strings.Split(scanner.Text(), " ") {
			n, _ := strconv.ParseInt(sn, 10, 32)
			nums = append(nums, int(n))
		}
		var next = nums[len(nums) - 1] + nums[len(nums) - 1] - nums[len(nums) - 2]
		var prev, sign = nums[0] - (nums[1] - nums[0]), 1
		var diff = func() {
			for i := 1; i < len(nums); i++ { nums[i-1] = nums[i] - nums[i - 1] }
			nums = nums[0:len(nums)-1]
		}
		var zeros = func() bool {
			for _, n := range nums { if n != 0 { return false } }
			return true
		}
		diff()
		for !zeros() {
			diff()
			next += nums[len(nums) - 1]
			prev += sign * nums[0]
			sign = -sign
		}
		result1 += next; result2 += prev
	}
	fmt.Println(result1, result2)
}