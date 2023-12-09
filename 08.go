package main

import "bufio"
import "os"
import "regexp"
import "fmt"

func gcd(a int64, b int64) int64 {
    if (b == 0) { return a }
    return gcd(b, a % b)
}

func lcm(a int64, b int64) int64 {
    return a / gcd(a, b) * b
}

func main() {
	var scanner = bufio.NewScanner(os.Stdin)
	var reNode = regexp.MustCompile("[A-Z]+")
	var paths = make(map[string][]string, 200)
	scanner.Scan()
	var inst = scanner.Text()
	scanner.Scan()
	for scanner.Scan() {
		var nodes = reNode.FindAllString(scanner.Text(), -1)
		paths[nodes[0]] = nodes[1:]
	}
	var steps = func(from string, finished func(node string) bool) int64 {
		var n, i = from, 0
		for !finished(n) {
			if inst[i % len(inst)] == 'L' { n = paths[n][0] } else { n = paths[n][1] }
			i++
		}
		return int64(i)
	}
	var result1, result2 = steps("AAA", func (node string) bool { return node == "ZZZ" }), int64(1)
	for node, _ := range paths {
		if node[2] == 'A' { result2 = lcm(result2, steps(node, func (node string) bool { return node[2] == 'Z' })) }
	}
	fmt.Println(result1, result2)
}
