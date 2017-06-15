#include "vorgabe3.h"

#include <unistd.h>
/********************************************/
/* Implementiert hier die Deadlockerkennung */
/********************************************/

void check_for_deadlocks()
{
    int deadlockCount = 0;
    for(int i = 0; i < THREAD_NUM; ++i){
        if(thread_data[i].requested == 1){
            ++deadlockCount;
        }
    }
    // if all threads flags are set none can acsess the storage
    // hence the threads must be in a deadlock
    if(deadlockCount == THREAD_NUM){
        // informing the user and closing the programm
        printf("Sergeant we are fucked.");
        cleanup();
    }
    sleep(3);
	/* Zum Debuggen */
	print_thread_data();
}
