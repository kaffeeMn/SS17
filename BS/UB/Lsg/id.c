#include <stdio.h>
#include <unistd.h>

int main(void){
    printf("User: %d\n", getuid());
    printf("Group: %d\n", getgid());

    return 0;
}
