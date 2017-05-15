#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pwd.h>
#include <limits.h>
#include <string.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <signal.h>

int main(void){
    struct passwd *pwd = getpwuid(geteuid());
    char hostname[HOST_NAME_MAX] = "\0";
    char path[PATH_MAX] = "\0";
    char method[256];
    char args[256];

    gethostname(hostname, HOST_NAME_MAX);

    int newPid = 0;    
    int status = 0;
    int rVal = 0;

    int parentId = getpid();

    while(1){
        getcwd(path, PATH_MAX);
        printf("%s@%s %s$ ", pwd->pw_name, hostname, path);
        scanf("%s %s", &method, &args);
        newPid = fork();
        if(strcmp(method,"cd") == 0){
            if(newPid == 0){
                rVal = chdir(args);
                if(rVal < 0){
                    printf("passed illegal arguments\n");
                }
            }else{
                waitpid(newPid, &status, 0);
            }
        }else if(strcmp(method,"ls") == 0){
            if(newPid==0){
                rVal = execlp("ls", method, args, path);
                if(rVal < 0){
                    printf("passed illegal arguments\n");
                }
            }else{
                waitpid(newPid, &status, 0);
            }
        }else if(strcmp(method,"exit") == 0){
            // I have no idea what I'm doing
            // ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
            // ░░░░░░░░░░░░░▄▄▄▄▄▄▄░░░░░░░░░
            // ░░░░░░░░░▄▀▀▀░░░░░░░▀▄░░░░░░░
            // ░░░░░░░▄▀░░░░░░░░░░░░▀▄░░░░░░
            // ░░░░░░▄▀░░░░░░░░░░▄▀▀▄▀▄░░░░░
            // ░░░░▄▀░░░░░░░░░░▄▀░░██▄▀▄░░░░
            // ░░░▄▀░░▄▀▀▀▄░░░░█░░░▀▀░█▀▄░░░
            // ░░░█░░█▄▄░░░█░░░▀▄░░░░░▐░█░░░
            // ░░▐▌░░█▀▀░░▄▀░░░░░▀▄▄▄▄▀░░█░░
            // ░░▐▌░░█░░░▄▀░░░░░░░░░░░░░░█░░
            // ░░▐▌░░░▀▀▀░░░░░░░░░░░░░░░░▐▌░
            // ░░▐▌░░░░░░░░░░░░░░░▄░░░░░░▐▌░
            // ░░▐▌░░░░░░░░░▄░░░░░█░░░░░░▐▌░
            // ░░░█░░░░░░░░░▀█▄░░▄█░░░░░░▐▌░
            // ░░░▐▌░░░░░░░░░░▀▀▀▀░░░░░░░▐▌░
            // ░░░░█░░░░░░░░░░░░░░░░░░░░░█░░
            // ░░░░▐▌▀▄░░░░░░░░░░░░░░░░░▐▌░░
            // ░░░░░█░░▀░░░░░░░░░░░░░░░░▀░░░
            // ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
            printf("all processes ended with note/ initialized return value: %s", args);
            kill(0, SIGTERM);
        }else{
            printf("passed illegal arguments\n");
        }
    }
    return 1;
}
