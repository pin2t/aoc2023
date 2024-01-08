package main

import "os"
import "bufio"
import "strings"
import "fmt"

type broadcaster struct {
	name string
	outputs []string
}

func (m *broadcaster) process(p pulse) {
	for _, o := range m.outputs {
		pulse{m.name, o, p.high}.send()
	}
}

type flip struct {
	broadcaster
	on bool
}

func (m *flip) process(p pulse) {
	if !p.high {
		m.on = !m.on
		for _, o := range m.outputs {
			pulse{m.name, o, m.on}.send()
		}
	}
}

func (m *flip) reset() {
	m.on = false
}

type conjunction struct {
	broadcaster
	pulses map[string]bool
}

func (m *conjunction) process(p pulse) {
	m.pulses[p.from] = p.high
	var result = true
	for _, p := range m.pulses {
		result = result && p
	}
	for _, o := range m.outputs {
		pulse{m.name, o, !result}.send()
	}
}

func (m *conjunction) reset() {
	for from := range m.pulses {
		m.pulses[from] = false
	}
}

var modules = make(map[string]interface{})
var sentHigh, sentLow = int64(0), int64(0)
var pulses []pulse

type pulse struct {
	from, to string
	high bool
}

func (p pulse) send() {
	pulses = append(pulses, p)
	if p.high { sentHigh++ } else { sentLow++ }
}

func gcd(a int64, b int64) int64 {
    if b == 0 { return a }
    return gcd(b, a % b)
}

func lcm(a int64, b int64) int64 {
    return a / gcd(a, b) * b
}

func main() {
	var scanner = bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		var items = strings.Split(scanner.Text(), " -> ")
		var name = items[0][1:]
		var outputs = strings.Split(items[1], ",")
		for i := range outputs {
			outputs[i] = strings.Trim(outputs[i], " ")
		}
		if items[0] == "broadcaster" {
			modules[items[0]] = &broadcaster{items[0], outputs}
		} else if items[0][0] == '%' {
        	modules[name] = &flip{broadcaster{name, outputs}, false}
		} else if items[0][0] == '&' {
			modules[name] = &conjunction{broadcaster{name, outputs}, make(map[string]bool)}
   		}
	}
	for _, mi := range modules {
		var outputs []string
		var name string
		if m, is := mi.(*broadcaster); is { outputs = m.outputs; name = m.name } else
		if m, is := mi.(*flip); is { outputs = m.outputs; name = m.name } else
		if m, is := mi.(*conjunction); is { outputs = m.outputs; name = m.name }
		for _, o := range outputs {
			if mi, found := modules[o]; found {
				if m, is := mi.(*conjunction); is {
					m.pulses[name] = false
			    }
			}
		}
	}
	for i := 1; i <= 1000; i++ {
		pulse{"button", "broadcaster", false}.send()
		for len(pulses) > 0 {
			var p = pulses[0]
			pulses = pulses[1:]
			if mi, found := modules[p.to]; found {
				if m, casted := mi.(*broadcaster); casted {
					m.process(p)
				} else if m, casted := mi.(*flip); casted {
					m.process(p)
				} else if m, casted := mi.(*conjunction); casted {
					m.process(p)
				} else {
					panic(fmt.Sprint("unknown module", mi))
				}
			}
		}
	}
	var part1 = sentHigh * sentLow
	for _, mi := range modules {
		if m, is := mi.(*conjunction); is { m.reset() }
		if m, is := mi.(*flip); is { m.reset() }
	}
	var cycles = make(map[string]int64)
	var press = 1
	var target = modules["zg"].(*conjunction)
	for {
		pulse{"button", "broadcaster", false}.send()
		for len(pulses) > 0 {
			var p = pulses[0]
			pulses = pulses[1:]
			if mi, found := modules[p.to]; found {
				if m, is := mi.(*broadcaster); is {
					m.process(p)
				} else if m, is := mi.(*flip); is {
					m.process(p)
				} else if m, is := mi.(*conjunction); is {
					m.process(p)
				} else {
					panic(fmt.Sprint("unknown module", mi))
				}
			}
			if high, found := target.pulses[p.to]; found && high  {
				if _, found := cycles[p.to]; !found {
					cycles[p.to] = int64(press)
					if len(cycles) == len(target.pulses) {
						var result = int64(1)
						for _, c := range cycles {
							result = lcm(result, c)
						}
						fmt.Println(part1, result)
						return
					}
				}
			}
		}
		press++
	}
}