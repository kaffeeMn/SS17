#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <pthread.h>

#include "aufgabe2.h"

#include "workingset.h"

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

    workingSet->done = 1;
}
void initThreads(&threadList, &statusList){
    int i;
    for(i=0; i<THREAD_NUM; ++i){
        &statusList[i] = pthread_create(
            &threadList[i], NULL, &run, NULL
        );
    }
}
void checkExceptions(&int statusList[]){
    int i;
    for(i=0; i<THREAD_NUM; ++i){
        // Fehlerbehandlung
    }
}
/* Pseudozufallszahlengenerator initialisieren, Arrays befuellen */
int main(int argc, char **argv)
{
    // Initialisiere die Datenkorruption
    initialize();

    // List needed for the threads
    pthread_t   threadList[THREAD_NUM];
    int         statusList[THREAD_NUM];
    // loop initialzing threads
    initThreads(&threadList, &statusList);
    // loop that checks for exceptions
    checkExceptions(&statusList);
    // exiting threads (just to be sure)
    pthread_exit(NULL);

    // Hier sollte euer Code hin

    // Pruefe auf Datenkorruption
    test_results();
}

