#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <pthread.h>

#include "aufgabe2.h"

#include "workingset.h"

// 
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
        if(taks->done != 1){
            do_WorkingSet(task);
            ++global_done;
            ++thread_done[&thread_counter];
        }
    }
    if(pthread_join(threadList[i], NULL) != 0){
        // Fehlerbehandlung
    }
    pthread_exit(NULL);
}
void do_WorkingSet(WorkingSet *workingSet) {
    int i;
    for(i=0; i<workingSet->length; ++i){
        workingSet->output[i] = workingSet->operation(workingSet->input[i], workingSet);
    }
    workingSet->done = 1;
}
/* Pseudozufallszahlengenerator initialisieren, Arrays befuellen */
int main(int argc, char **argv)
{
    // Initialisiere die Datenkorruption
    initialize();
    
    // Hier sollte euer Code hin

    // List needed for the threads
    pthread_t   threadList[THREAD_NUM];
    int         status;
    // loop initialzing threads
    int i;
    for(i=0; i<THREAD_NUM; ++i){
        // @thread          : thread at index i
        // @attr            : None
        // @start_routine   : run - method
        // @arg             : index i of thread
        status = pthread_create(&threadList[i], NULL, &run, i);
        if(status != 0){
            // Fehlerbehandlung
            if(errno == EINTR || errno == EAGAIN) continue;
            perror("An error occured while creating a thread");
            exit(EXIT_FAILURE);
        }
    }
    for(i=0; i<THREAD_NUM; ++i){
        // @thread  : thread at index i
        // @attr    : None
        status = pthread_join(&threadList[i], NULL);
        if(status != 0){
            // Fehlerbehandlung
            if(errno == EINTR || errno == EAGAIN) continue;
            perror("An error occured while joining a thread");
            exit(EXIT_FAILURE);
        }
    }
    // exiting threads
    pthread_exit(NULL);



    // Pruefe auf Datenkorruption
    test_results();
}

