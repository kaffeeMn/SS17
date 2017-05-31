#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <pthread.h>

#include "aufgabe2.h"

#include "workingset.h"

WorkingSet tasks[TASK_NUM];

WorkingSet blur_tasks[TASK_NUM];

// Wieviele Aufgaben wurden erledigt?
int global_done;

// Wieviel hat der jeweilige Thread erledigt?
int thread_done[THREAD_NUM];

// Die Eingabewerte der Berechnung
int input_values[ARRAY_SIZE];

// Das Ergebnis der Berechnung
int results[ARRAY_SIZE];

// Ergebnisse fuer Nachbearbeitung
int results_blur[ARRAY_SIZE];

// Wieviele Nachbearbeitungs-Aufgaben wurden erledigt?
int global_done_post_process;

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
        
        
    }
    pthread_exit(NULL);
}

void *post_process(void *args) {
    while(global_done_post_process < TASK_NUM)
    {
        // Eine Aufgabe auswuerfeln
        int task_num = rand() % TASK_NUM;

        // Pointer auf das WorkingSet aufloesen
        WorkingSet *inputTask = &tasks[task_num];
        
        WorkingSet *outputTask = &blur_tasks[task_num];
        

    }
    pthread_exit(NULL);
}

void do_WorkingSet(WorkingSet *workingSet) {

}

/* Pseudozufallszahlengenerator initialisieren, Arrays befuellen */
int main(int argc, char **argv)
{
    // Initialisiere die Datenkorruption
    initialize();

    // Hier sollte euer Code hin
    
    // Pruefe auf Datenkorruption
    test_results();
}

