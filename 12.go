package main

import "bufio"
import "os"
import "fmt"
import "strings"
import "strconv"

type springs struct { pattern string; groups []int }

func new(text string) springs {
	var items = strings.Split(text, " ")
	var result = springs{items[0], []int{}}
	for _, size := range strings.Split(items[1], ",") {
		nsize, _ := strconv.ParseInt(size, 10, 32)
		result.groups = append(result.groups, int(nsize))
	}
	return result
}

func (sp springs) expand(factor int) springs {
	var result = springs{sp.pattern, sp.groups}
	for i := 1; i < factor; i++ {
		result.pattern += "?" + sp.pattern
		result.groups = append(result.groups, sp.groups...)
	}
	return result
}

func (sp springs) matched(prefix string) bool {
	for i, c := range prefix { if c != rune(sp.pattern[i]) && sp.pattern[i] != '?' { return false } }
	return true
}

type key struct { len, group int }
var cache = make(map[key]int64, 1000000)

func (sp springs) matches(prefix string, group int) int64 {
	if len(prefix) > len(sp.pattern) { return 0 }
	var k = key{len(prefix), group}
	if v, found := cache[k]; found { return v }
	if group == len(sp.groups) {
		if sp.matched(prefix + strings.Repeat(".", len(sp.pattern) - len(prefix))) { return 1 } else
		{ return 0}
	}
	var result int64
	for i := 0; i < (len(sp.pattern) - sp.groups[group]); i++ {
		var p = prefix
		if group > 0 { p += "." }
		p += strings.Repeat(".", i) + strings.Repeat("#", sp.groups[group])
		if (len(p) <= len(sp.pattern) && sp.matched(p)) { result += sp.matches(p, group + 1) }
	}
	cache[k] = result
	return result
}

func (sp springs) arrangements() int64 {
	return sp.matches("", 0)
}

func main() {
	var scanner = bufio.NewScanner(os.Stdin)
	var sum, sum2 = int64(0), int64(0)
	for scanner.Scan() {
		cache = make(map[key]int64, 1000000)
		var sp = new(scanner.Text())
		sum += sp.arrangements()
		cache = make(map[key]int64, 1000000)
		sum2 += sp.expand(5).arrangements()
	}
	fmt.Println(sum, sum2)
}
