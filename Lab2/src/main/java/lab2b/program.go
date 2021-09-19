package main

import (
	"fmt"
	"sync"
)

var wg sync.WaitGroup

func steal(ch1, quitS chan int) {

	for i := 0; i < 10; i++ {
		fmt.Printf("Things which values %v stolen\n", i)
		ch1 <- i
	}

	fmt.Println("Quit stealing")

	close(ch1)

	quitS <- 1
	wg.Done()
}

func loading(ch1, ch2, quitS, quitL chan int) {

	for {
		select {
		case x, ok := <-ch1:
			if ok {
				fmt.Printf("Things which values %v loaded\n", x)
				ch2 <- x
			}
		case <-quitS:
			for x := range ch1 {
				fmt.Printf("Things which values %v loaded. Preparing for quit..\n", x)
				ch2 <- x
			}

			fmt.Println("Quit loading")
			quitL <- 1
			close(ch2)
			wg.Done()
			return

		}
	}
}

func count(ch2, quitL chan int, price *int) {
	for {
		select {

		case x, ok := <-ch2:
			if ok {
				*price += x
				fmt.Printf("Things which values %v counted. Price is %v\n", x, *price)
			}
		case <-quitL:
			for x := range ch2 {
				*price += x
				fmt.Printf("Things which values %v counted. Price is %v. Preparing for quit..\n", x, *price)
			}

			fmt.Println("Quit counting")
			wg.Done()
			return
		}
	}
}

func main() {
	ch1 := make(chan int, 10)
	ch2 := make(chan int, 10)
	quitStealing := make(chan int, 1)
	quitLoading := make(chan int, 1)

	price := 0

	wg.Add(1)
	go count(ch2, quitLoading, &price)

	wg.Add(1)
	go loading(ch1, ch2, quitStealing, quitLoading)

	wg.Add(1)
	go steal(ch1, quitStealing)

	wg.Wait()
	fmt.Printf("Final stolen price is %v\n", price)
}
