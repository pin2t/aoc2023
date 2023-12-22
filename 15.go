package main

import "strings"
import "bufio"
import "fmt"
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

		} else if "-" == s[:-1] {
			
		}
	}
	fmt.Println(res1)
}
