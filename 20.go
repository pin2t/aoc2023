package main

import "fmt"
import "os"
import (

"bufio"
"strings"
)

type module struct {
	name string
	outputs []string
}

func (m *module) process(high bool) {
	for _, o := range m.outputs {
		pulse{m.name, o, high}.send()
	}
}

type flip struct {
	module
	on bool
}

func (m *flip) process(high bool) {
	if !high {
		m.on = !m.on
		for _, o := range m.outputs {
			pulse{m.name, o, m.on}.send()
		}
	}
}

type conjunction struct {
	module
	pulses map[string]bool
}

func (m *conjunction) process(from string, high bool) {
	m.pulses[from] = high
	var result = true
	for _, p := range m.pulses {
		result = result && p
	}
	for _, o := range m.outputs {
		pulse{m.name, o, !result}.send()
	}
}

var modules = make(map[string]interface{})
var sentHIgh, sentLow = int64(0), int64(0)
var pulses []pulse

type pulse struct {
	from, to string;
	high bool
}

func (p pulse) send() {
	pulses = append(pulses, p)
	if p.high { sentHIgh++ } else { sentLow++ }
}

func main() {
	var scanner = bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		var items = strings.Split(scanner.Text(), " -> ")
		if items[0] == "broadcaster" {
			modules[items[0]] = &module{items[0], strings.Split(items[1], ",")}
		} else if items[0][0] == '%' {
        	modules[items[0][1:]] = &flip{module{items[0], strings.Split(items[1], ",")}, false}
		} else if items[0][0] == '&' {
			modules[items[0][1:]] = &conjunction{module{items[0], strings.Split(items[1], ",")}, make(map[string]bool)}
   		}
	}
	for i := 1; i <= 4000; i++ {
		pulse{"button", "broadcaster", false}.send()
		for len(pulses) > 0 {
			var p = pulses[0]
			pulses = pulses[1:]
			if mp, found := modules[p.to]; found {
				if m, ok := mp.(*module); ok {
					m.process(p.high)
				} else if m, ok := mp.(*flip); ok {
				    m.process(p.high)
				} else if m, ok := mp.(*conjunction); ok {
				    m.process(p.from, p.high)
				}
			}
		}
	}
	fmt.Println(sentHIgh * sentLow)
}