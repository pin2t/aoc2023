package main

import "bufio"
import "fmt"
import "strconv"
import "os"
import "strings"
import "time"

type part map[uint8]int

var channels = make(map[string]chan part)

// not concurrent safe, should be used on main goroutine only
func channel(name string) chan part {
	if c, found := channels[name]; found { return c }
	channels[name] = make(chan part)
	return channels[name]
}

func startWorkflow(name string, srules []string) {
	var in = channel(name)
	var fallback = channel(srules[len(srules) - 1])
	type rule struct { rating uint8; op uint8; val int; dest chan part; destName string }
	var rules []rule
	for _, r := range srules[:(len(srules) - 1)] {
		v, _ := strconv.ParseInt(r[2:strings.Index(r, ":")], 10, 32)
		var destName = r[(strings.Index(r, ":") + 1):]
		rules = append(rules, rule{
			r[0], r[1], int(v), channel(destName), destName,
		})
	}
	go func(in chan part, rules []rule, fallback chan part) {
		for {
			var p = <- in
			var processed = false
			for _, r := range rules {
				if processed { break }
				switch r.op {
				case '<': if p[r.rating] < r.val { r.dest <- p; processed = true }
				case '>': if p[r.rating] > r.val { r.dest <- p; processed = true }
				default: panic("invalid operation " + string(r.op))
				}
			}
			if !processed {
				fallback <- p
			}
		}
	}(in, rules, fallback)
}

type _range struct { first, last int }
type ranges map[uint8]_range

var rangesChannels = make(map[string]chan ranges)
// not concurrent safe, should be used on main goroutine only
func rangesChannel(name string) chan ranges {
	if c, found := rangesChannels[name]; found { return c }
	rangesChannels[name] = make(chan ranges)
	return rangesChannels[name]
}

func startRangesWorkflow(name string, srules []string) {
	var in = rangesChannel(name)
	var fallback = rangesChannel(srules[len(srules) - 1])
	type rule struct { rating uint8; op uint8; val int; dest chan ranges; destName string }
	var rules []rule
	for _, r := range srules[:(len(srules) - 1)] {
		v, _ := strconv.ParseInt(r[2:strings.Index(r, ":")], 10, 32)
		var destName = r[(strings.Index(r, ":") + 1):]
		rules = append(rules, rule{
			r[0], r[1], int(v), rangesChannel(destName), destName,
		})
	}
	go func(in chan ranges, rules []rule, fallback chan ranges) {
		for {
			var rr = <- in
			for _, r := range rules {
				var matched = ranges{}
				for k, v := range rr { matched[k] = v }
				var left = ranges{}
				for k, v := range rr { left[k] = v }
				switch r.op {
				case '<':
                    matched[r.rating] = _range{rr[r.rating].first, r.val - 1}
					left[r.rating] = _range{r.val, rr[r.rating].last}
				case '>':
                    matched[r.rating] = _range{r.val + 1, rr[r.rating].last}
					left[r.rating] = _range{rr[r.rating].first, r.val}
				default: panic("invalid operation " + string(r.op))
				}
				if matched[r.rating].first <= matched[r.rating].last {
					r.dest <- matched
				}
				if left[r.rating].first <= left[r.rating].last {
					rr = left
				} else {
					rr = ranges{}
					break
				}
			}
			if len(rr) != 0 {
				fallback <- rr
			}
		}
	}(in, rules, fallback)
}

func main() {
	var accepted = channel("A")
	var rejected = channel("R")
	var raccepted = rangesChannel("A")
	var rrejected = rangesChannel("R")
	var rangesQuit = make(chan struct{})
	var all = make(chan part, 1000)  // buffered to avoid deadlocks
	var result1, result2 = 0, int64(0)
	go func() {
		for {
			var p = <-accepted;
			for _, val := range p { result1 += val }
			all <- p
		}
	}()
	go func() { for { all <- <-rejected } }()
	go func() {
		for {
			select {
			case rs := <-raccepted:
				var n = int64(1)
				for _, r := range rs { n *= int64(r.last - r.first + 1) }
				result2 += n
			case <-time.After(2 * time.Second):
				rangesQuit <- struct{}{}
				break
			}
		}
	}()
	go func() { for { <-rrejected } }()
	var parts []part
	var scanner = bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		var line = scanner.Text()
		if line == "" { continue }
		if line[0] == '{' {
			var p = part{}
			for _, rating := range strings.Split(line[1:(len(line) - 1)], ",") {
				value, _ := strconv.ParseInt(rating[2:], 10, 32)
				p[rating[0]] = int(value)
			}
			parts = append(parts, p)
		} else {
			var name = line[0:strings.Index(line, "{")]
			var rules = strings.Split(line[(strings.Index(line, "{") + 1):(len(line) - 1)], ",")
			startWorkflow(name, rules)
			startRangesWorkflow(name, rules)
		}
	}
	var in = channel("in")
	for _, p := range parts { in <- p }
	var rin = rangesChannel("in")
	var allRanges = ranges{}
	allRanges['x'] = _range{1, 4000}; allRanges['m'] = _range{1, 4000}; allRanges['a'] = _range{1,
		4000}; allRanges['s'] = _range{1, 4000}
	rin <- allRanges
	for n := 0; n < len(parts); n++ { <- all }
	<-rangesQuit
	fmt.Println(result1, result2)
}
