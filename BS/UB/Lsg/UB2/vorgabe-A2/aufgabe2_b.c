#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <pthread.h>

#include "aufgabe2.h"

#include "workingset.h"

// needed to carry out our beloved error-handling
#include <errno.h>

WorkingSet tasks[TASK_NUM];

// Wieviele Aufgaben wurden erledigt?
int global_done;

// Wieviel hat der jeweilige Thread erledigt?
int thread_done[THREAD_NUM];

// Die Eingabewerte der Berechnung
int input_values[ARRAY_SIZE];

// Das Ergebnis der Berechnung
int results[ARRAY_SIZE];


// Die Thread-Funktion
void* run(void *args)
{ 
    int *thread_counter = (int*)args;
    
    while(global_done < TASK_NUM)
    {
        // Eine Aufgabe auswuerfeln
        int task_num = rand() % TASK_NUM;

        // Pointer auf das WorkingSet aufloesen
        WorkingSet *task = &tasks[task_num];
        
        // check whether it's already been dealt with
        // and do the task eventually
        // otherwise a new random task will bes selected
        if(task->done != 1){
            do_WorkingSet(task);
            ++global_done;
            thread_done[thread_counter[0]] = 1;
        }
    }
    // exiting thread
    pthread_exit(NULL);
}
void do_WorkingSet(WorkingSet *workingSet) {
    int i;
    for(i=0; i<workingSet->length; ++i){
        // the output shall be the result of the operation called with the input and the workingset
        workingSet->output[i] = workingSet->operation(workingSet->input[i], workingSet);
    }
    // setting the flag
    workingSet->done = 1;
}
/* Pseudozufallszahlengenerator initialisieren, Arrays befuellen */
int main(int argc, char **argv)
{
    // Initialisiere die Datenkorruption
    initialize();
    
    // Hier sollte euer Code hin


    // List needed for the threads (might aswell initialize a list instead of a new thread in each loop, that cannot be 
    // referenced, if neccessary)
    pthread_t   threadList[THREAD_NUM];
    int         status;
    // loop initialzing threads
    int i;
    for(i=0; i<THREAD_NUM; ++i){
        // @thread          : thread at index i
        // @attr            : None
        // @start_routine   : run-method
        // @arg             : index i of thread
        status = pthread_create(&threadList[i], NULL, &run, &i);
        if(status != 0){
            // Fehlerbehandlung
            if(errno == EINTR) continue;
            perror("An error occured while creating a thread");
            exit(EXIT_FAILURE);
        }
    }
    // loop to call join on each thread
    for(i=0; i<THREAD_NUM; ++i){
        // @thread  : thread at index i
        // @attr    : None
        status = pthread_join(threadList[i], NULL);
        if(status != 0){
            // Fehlerbehandlung
            if(errno == EINTR) continue;
            perror("An error occured while joining a thread");
            exit(EXIT_FAILURE);
        }
    }


    // Pruefe auf Datenkorruption
    test_results();
}

