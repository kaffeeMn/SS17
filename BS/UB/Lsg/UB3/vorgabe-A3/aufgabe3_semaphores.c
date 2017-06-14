#include "vorgabe3.h"
#include <semaphore.h>
#include <errno.h>

/***************************************************************/
/* In dieser Datei die Semaphoren initialisieren und zerstören */
/***************************************************************/

void initialize_semaphores()
{
    int i;
    int status;
    for(i = 0; i < THREAD_NUM; ++i){
        // using a none zero value here, so other threads are granted access
        // initial value is THREAD_NUM
        status = sem_init(&(thread_data[i].sem), 1, THREAD_NUM);
        if(status == -1){
            // Fehlerbehandlung
            if(errno == EINTR) continue;
            perror("An error occured while initializing a semaphor");
            exit(EXIT_FAILURE);
        }
    }
}

void destroy_semaphores()
{
    int i;
    int status;
    for(i = 0; i < THREAD_NUM; ++i){
        status = sem_destroy(&(thread_data[i].sem));
        if(status == -1){
            // Fehlerbehandlung
            if(errno == EINTR) continue;
            perror("An error occured while destroying a semaphor");
            exit(EXIT_FAILURE);
        }
    }
}
