package main

import "bufio"
import "strings"
import "sort"
import "fmt"
import "strconv"
import "os"

const ( high = 1; onePair = 2; twoPairs = 3; threeOfKind = 4; fullHouse = 5; fourOfKind = 6; fiveOfKind = 7 )

type hand struct { cards [5]byte; bid int }

func (h hand) _type() int {
	var m = map[byte]int{}
	for i := 0; i < 5; i++ { m[h.cards[i]]++ }
	var counts []int
	for _, v := range m { counts = append(counts, v) }
	sort.Ints(counts)
	if len(counts) == 1 { return fiveOfKind }
	if len(counts) == 2 && counts[0] == 1 {	return fourOfKind }
	if len(counts) == 2 && counts[0] == 2 && counts[1] == 3 { return fullHouse }
	if len(counts) == 3 && counts[2] == 3 { return threeOfKind }
	if len(counts) == 3 && counts[0] == 1 && counts[1] == 2 && counts[2] == 2 { return twoPairs }
	if len(counts) == 4 && counts[0] == 1 && counts[1] == 1 && counts[2] == 1 && counts[3] == 2 { return onePair }
	return high
}

func (h hand) bestType() int {
	var maxType = high
	const cards = "AKQJT98765432"
	for _, c := range cards {
		var t = hand{[5]byte([]byte(strings.ReplaceAll(string(h.cards[:]), "J", string(c)))), 0}._type()
		if t > maxType { maxType = t }
	}
	return maxType
}

func (h hand) less(other hand) bool {
	if h._type() < other._type() { return true }
	if h._type() > other._type() { return false }
	const cards = "AKQJT98765432"
	for i := 0; i < 5; i++ {
		if strings.IndexByte(cards, h.cards[i]) > strings.IndexByte(cards, other.cards[i]) { return true }
		if h.cards[i] != other.cards[i] { break }
	}
	return false
}

func (h hand) lessBest(other hand) bool {
	if h.bestType() < other.bestType() { return true }
	if h.bestType() > other.bestType() { return false }
	const cards = "AKQT98765432J"
	for i := 0; i < 5; i++ {
		if strings.IndexByte(cards, h.cards[i]) > strings.IndexByte(cards, other.cards[i]) { return true }
		if h.cards[i] != other.cards[i] { break }
	}
	return false
}

func main() {
	var scanner = bufio.NewScanner(os.Stdin)
	var hands []hand
	var bestHands []hand
	for scanner.Scan() {
		bid, _ := strconv.ParseInt(scanner.Text()[6:], 10, 32)
		var h = hand{[5]byte([]byte(scanner.Text()[:5])), int(bid)}
		hands = append(hands, h)
		bestHands = append(bestHands, h)
	}
	sort.Slice(hands, func(i int, j int) bool { return hands[i].less(hands[j]) })
	sort.Slice(bestHands, func(i int, j int) bool { return bestHands[i].lessBest(bestHands[j]) })
	var winnings, bestWinnings = 0, 0
	for i, h := range hands {
		winnings += (i + 1) * h.bid
		bestWinnings += (i + 1) * bestHands[i].bid
	}
	fmt.Println(winnings, bestWinnings)
}
