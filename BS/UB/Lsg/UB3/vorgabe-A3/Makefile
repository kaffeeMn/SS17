CC = gcc
CFLAGS = -Wall -std=c11

all: deadlock

deadlock: vorgabe3.o aufgabe3_main.o aufgabe3_semaphores.o aufgabe3_memory.o aufgabe3_threads.o aufgabe3_deadlock.o aufgabe3_sanity.o
	${CC} ${CFLAGS} $^ -o deadlock -pthread

%.o: %.c vorgabe3.h
	${CC} ${CFLAGS} $< -c -o $@

clean:
	rm -f *.o
	rm -f deadlock
