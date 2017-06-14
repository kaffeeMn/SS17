#include "vorgabe3.h"

#include <unistd.h>

/**********************************************************/
/* In dieser Datei die Threads implementieren und starten */
/**********************************************************/

struct ThreadData thread_data[THREAD_NUM];

void* thread_function(void *args)
{
	struct ThreadData *this = (struct ThreadData *) args;	
    //
    while(1){
        for(int i = 0; i < THREAD_NUM; ++i){
            for(int j = 0; j < MAX_REQUEST_NUM; ++j){
                request_memory(&thread_data[i], thread_data->requests[j]);
                sleep(1);
            }
            free_all_memory(&thread_data[i]);
        }
    }
}

void cleanup()
{
    // end threads
    pthread_exit(NULL);
    // end programm
    running = 0;
}

void start_threads()
{
	for(int i = 0; i < THREAD_NUM ; i++)
	{
		for(int j = 0; j < MAX_REQUEST_NUM ; j++)
		{
			thread_data[i].requests[j] = (rand() % MAX_REQUEST_SIZE) + 1;
		}
		if(pthread_create(&(thread_data[i].thread_id), NULL, &thread_function, thread_data + i)) {
			perror("pthread_create");
			exit(EXIT_FAILURE);
		}
	} 
}
