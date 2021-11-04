package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

var quantity = 5.0
var normalIndex = 10.0
var seed = rand.NewSource(time.Now().UnixNano())
var random = rand.New(seed)

type Market struct {
	index float64
	lock  sync.Mutex
}

type Stock struct {
	lock  sync.Mutex
	value int
	name  string
}

func buyStock(stock *Stock, market *Market, endChan chan bool) {
	stock.lock.Lock()
	if stock.value > 1 {
		stock.value -= 1
	}
	fmt.Println("buying "+stock.name+" stock | stock value is: ", stock.value)
	stock.lock.Unlock()

	market.lock.Lock()
	market.index -= 1.0 / quantity
	if normalIndex-market.index > 0.5 {
		fmt.Println("Normalizing index")
		time.Sleep(2000)
		market.index = normalIndex
	}
	market.lock.Unlock()
	endChan <- true
}

func sellStock(stock *Stock, market *Market, endChan chan bool) {
	stock.lock.Lock()
	stock.value += 1
	fmt.Println("selling "+stock.name+" stock | stock value is: ", stock.value)

	stock.lock.Unlock()

	market.lock.Lock()
	market.index += 1.0 / quantity
	market.lock.Unlock()

	endChan <- true

}

func main() {
	var companiesNumber = 5
	var startingIndex = 10

	var market = Market{index: normalIndex, lock: sync.Mutex{}}

	var s = make([]Stock, companiesNumber)
	for i := 0; i < companiesNumber; i++ {
		s[i] = Stock{lock: sync.Mutex{}, value: startingIndex, name: string(i)}
	}
	endChan := make(chan bool, companiesNumber*15)

	for i := 0; i < companiesNumber*3; i++ {
		r := random.Intn(5)
		if r > 0 {
			go buyStock(&s[random.Intn(5)], &market, endChan)
		} else {
			go sellStock(&s[random.Intn(5)], &market, endChan)
		}
	}

	for i := 0; i < companiesNumber*3; i++ {
		<-endChan
	}
}
