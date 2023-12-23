package main

import "strings"
import "bufio"
import "fmt"
import "strconv"
import "os"

func hash(s string) (result int) {
	for _, c := range s {
		result += int(c)
		result *= 17
		result %= 256
	}
	return
}

type lens struct { label string; focal int }

func main() {
	var scanner = bufio.NewScanner(os.Stdin)
	scanner.Scan()
	var steps = strings.Split(scanner.Text(), ",")
	var res1 int
	var boxes [256][]lens
	for _, s := range steps {
		res1 += hash(s)
		if strings.Contains(s, "=") {
			var items = strings.Split(s, "=")
			var label = items[0]
			focal, _ := strconv.ParseInt(items[1], 10, 64)
			var i = hash(label)
			var j = 0
			for ;j < len(boxes[i]); j++  {
				if boxes[i][j].label == label {
					boxes[i][j].focal = int(focal)
					break
				}
			}
			if j == len(boxes[i]) { boxes[i] = append(boxes[i], lens{label, int(focal)})}
		} else if '-' == s[len(s)-1] {
			var label = s[:(len(s) - 1)]
			var i = hash(label)
			for j, l := range boxes[i] {
				if l.label == label {
					 copy(boxes[i][j:], boxes[i][(j + 1):])
                     boxes[i] = boxes[i][:(len(boxes[i]) - 1)]
					 break
				}
			}
		}
	}
	var result2 int
	for i, box := range boxes { for j, lens := range box { result2 += (i + 1) * (j + 1) * lens.focal } }
	fmt.Println(res1, result2)
}
