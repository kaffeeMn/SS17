#include <stdio.h>
#include <unistd.h>
#include <pwd.h>
#include <sys/types.h>
#include <limits.h>
#include <string.h>


int main(void){
    struct passwd *pwd = getpwuid(geteuid());
    char hostname[HOST_NAME_MAX] = "\0";
    char path[PATH_MAX] = "\0";
    char method[256];
    char args[256];

    gethostname(hostname, HOST_NAME_MAX);
    getcwd(path, PATH_MAX);

    int newPid = 0;    
    int status = 0;

    while(1){
        printf("%s@%s %s$ ", pwd->pw_name, hostname, path);
        scanf("%s %s", &method, &args);
        if(strcmp(method,"ls") != 0 || strcmp(args, "-al") != 0){
            printf("You did not type ls -al\n");
            return 1;
        }else{
            newPid = fork();
            if(newPid == 0){
                execlp(method, args, ".");
            }else{
                waitpid(newPid, &status, NULL);
            }
        }
    }
    return 1;
}
