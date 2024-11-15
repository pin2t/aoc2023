package main

type pulse struct {
	from, to string
	high     bool
}

type module struct {
	name string
	ch   chan pulse
}

type broadcaster struct {
	in      chan pulse
	outputs []chan pulse
}

func main() {

}
