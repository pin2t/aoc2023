package main
import "bufio"
import "os"
import "strings"
import "fmt"
import "sort"

var graph = make(map[string][]string)

type edge struct { from, to string }

func maxs(a string, b string) string {
	if a > b { return a }
	return b
}

func mins(a string, b string) string {
	if a < b { return a }
	return b
}

func main() {
	var scanner = bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		var items = strings.Split(scanner.Text(), ":")
		var node = strings.TrimSpace(items[0])
		for _, n := range strings.Split(strings.Trim(items[1], " "), " ") {
			graph[node] = append(graph[node], n)
			graph[n] = append(graph[n], node)
		}
	}
	var edges = make(map[edge]int)
	for start, _ := range graph {
		var queue []string
		var seen = make(map[string]bool)
		var prev = make(map[string]string)
		queue = append(queue, start)
		seen[start] = true
		for len(queue) > 0 {
			var node = queue[0]
			queue = queue[1:]
			for _, next := range graph[node] {
				if !seen[next] {
					seen[next] = true
					queue = append(queue, next)
					prev[next] = node
				}
			}
		}
		for node, _ := range graph {
			for node != start {
				var pr = prev[node]
				var e = edge{mins(pr, node), maxs(pr, node)}
				if _, found := edges[e]; found {
					edges[e] = edges[e] + 1
				} else {
					edges[e] = 1
				}
				node = pr
			}
		}
	}
	var sedges []struct { e edge; n int }
	for e, n := range edges {
		sedges = append(sedges, struct{ e edge; n int }{ e, n })
	}
	sort.Slice(sedges, func (i int, j int) bool {
		return sedges[j].n < sedges[i].n
	})
	for _, e := range sedges[0:3] {
		for i, n := range graph[e.e.from] {
			if n == e.e.to {
				graph[e.e.from] =  append(graph[e.e.from][:i], graph[e.e.from][i+1:]...)
				break
			}
		}
		for i, n := range graph[e.e.to] {
			if n == e.e.from {
				graph[e.e.to] =  append(graph[e.e.to][:i], graph[e.e.to][i+1:]...)
				break
			}
		}
	}
	var queue []string
	var seen = make(map[string]bool)
	queue = append(queue, sedges[3].e.from)
	seen[sedges[3].e.from] = true
	for len(queue) > 0 {
		var node = queue[0]
		queue = queue[1:]
		for _, next := range graph[node] {
			if !seen[next] {
				seen[next] = true
				queue = append(queue, next)
			}
		}
	}
	fmt.Println(len(seen) * (len(graph) - len(seen)))
}
