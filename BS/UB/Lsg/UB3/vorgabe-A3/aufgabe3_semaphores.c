#include "vorgabe3.h"
#include <semaphore.h>
#include <errno.h>

/***************************************************************/
/* In dieser Datei die Semaphoren initialisieren und zerstören */
/***************************************************************/

void initialize_semaphores()
{
    for(int i = 0; i < THREAD_NUM; ++i){
        // using a none zero value here, so other threads are granted access
        // initial value is THREAD_NUM
        if(sem_init(&(thread_data[i].sem), 1, THREAD_NUM)){
            // Fehlerbehandlung
            if(errno == EINTR) continue;
            perror("An error occured while initializing a semaphor");
            exit(EXIT_FAILURE);
        }
    }
}

void destroy_semaphores()
{
    for(int i = 0; i < THREAD_NUM; ++i){
        if(sem_destroy(&(thread_data[i].sem))){
            // Fehlerbehandlung
            if(errno == EINTR) continue;
            perror("An error occured while destroying a semaphor");
            exit(EXIT_FAILURE);
        }
    }
}
