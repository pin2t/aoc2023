package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

func mini(a, b int64) int64 {
	if a < b {
		return a
	}
	return b
}

type _range struct{ first, last int64 }

func (r _range) contains(value int64) bool {
	return value >= r.first && value <= r.last
}

func (r _range) _map(m []mapping) []_range {
	var result []_range
	mapped := false
	for _, mr := range m {
		offset := mr.to - mr.from.first
		if mr.from.contains(r.first) && mr.from.contains(r.last) {
			result = append(result, _range{r.first + offset, r.last + offset})
			mapped = true
		} else if mr.from.contains(r.first) {
			result = append(result, _range{r.first + offset, mr.from.last + offset})
			result = append(result, _range{mr.from.last + 1, r.last}._map(m)...)
			mapped = true
		} else if mr.from.contains(r.last) {
			result = append(result, _range{mr.from.first + offset, r.last + offset})
			result = append(result, _range{r.first, mr.from.first - 1}._map(m)...)
			mapped = true
		} else if r.contains(mr.from.first) && r.contains(mr.from.last) {
			result = append(result, _range{r.first, mr.from.first - 1}._map(m)...)
			result = append(result, _range{mr.from.last + 1, r.last}._map(m)...)
			result = append(result, _range{mr.from.first + offset, mr.from.last + offset})
			mapped = true
		}
		if mapped {
			break
		}
	}
	if !mapped {
		result = append(result, r)
	}
	return result
}

type mapping struct {
	from _range
	to   int64
}

var (
	seeds    = make([]int64, 0, 20)
	mappings = make([][]mapping, 0, 10)
)

func _map(seed int64) int64 {
	result := seed
	for _, m := range mappings {
		for _, r := range m {
			if r.from.contains(result) {
				result = r.to + (result - r.from.first)
				break
			}
		}
	}
	return result
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		line := scanner.Text()
		if strings.HasPrefix(line, "seeds:") {
			for _, s := range strings.Split(line[7:], " ") {
				n, _ := strconv.ParseInt(s, 10, 64)
				seeds = append(seeds, n)
			}
		} else if strings.HasSuffix(line, "map:") {
			m := make([]mapping, 0, 10)
			for scanner.Scan() {
				line := scanner.Text()
				if line == "" {
					break
				}
				to, _ := strconv.ParseInt(strings.Split(line, " ")[0], 10, 64)
				from, _ := strconv.ParseInt(strings.Split(line, " ")[1], 10, 64)
				length, _ := strconv.ParseInt(strings.Split(line, " ")[2], 10, 64)
				m = append(m, mapping{_range{from, from + length - 1}, to})
			}
			mappings = append(mappings, m)
		}
	}
	minLocation := int64(100000000000000)
	for _, seed := range seeds {
		minLocation = mini(minLocation, _map(seed))
	}
	ranges := make([]_range, 0, 100)
	for i := 0; i < len(seeds); i += 2 {
		ranges = append(ranges, _range{seeds[i], seeds[i] + seeds[i+1] - 1})
	}
	for _, m := range mappings {
		mapped := make([]_range, 0, 100)
		for _, r := range ranges {
			mapped = append(mapped, r._map(m)...)
		}
		ranges = mapped
	}
	minFirst := int64(100000000000000)
	for _, m := range ranges {
		minFirst = mini(minFirst, m.first)
	}
	fmt.Println(minLocation, minFirst)
}
