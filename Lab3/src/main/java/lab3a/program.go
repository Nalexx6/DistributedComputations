package main

import (
	"fmt"
	"sync"
)

type counter struct {
	sync.Mutex
	count int
}

func (c *counter) Increment() {
	c.Lock()
	defer c.Unlock()
	c.count++
}
func (c *counter) Decrement() {
	c.Lock()
	defer c.Unlock()
	c.count--
}

func (c *counter) getCounter() int {
	c.Lock()
	defer c.Unlock()
	return c.count
}

var QUIT = new(counter)
var wg sync.WaitGroup

func fill(ch chan int) {
	defer wg.Done()

	if QUIT.getCounter() == 1 {
		return
	}

	select {

	case ch <- len(ch):
		fmt.Printf("Filling the jug %v\n", len(ch))
	default:
		QUIT.Increment()
		awakeAndEat(ch)
	}
}

func awakeAndEat(ch chan int) {
	close(ch)

	fmt.Printf("Jug is full %v\n", len(ch))

	fmt.Println("Bear awake")
	for i := range ch {
		fmt.Printf("Eating honey %v\n", i)

	}
}

func main() {

	jugCapacity := 10
	beeAmount := 7

	ch := make(chan int, jugCapacity)

	for QUIT.getCounter() == 0 {
		for i := 0; i < beeAmount; i++ {
			if QUIT.getCounter() == 1 {
				break
			}

			fmt.Printf("Bee is heading to jug %v\n", i)
			wg.Add(1)
			go fill(ch)
		}
	}

	wg.Wait()
	fmt.Println("Jug is empty")

}
