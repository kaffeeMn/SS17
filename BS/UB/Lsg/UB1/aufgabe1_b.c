#include <stdio.h>
#include <unistd.h>
#include <pwd.h>
#include <sys/types.h>
#include <limits.h>
#include <string.h>


int main(int argc, char **argv){
    if(argc < 3){
        struct passwd *pwd = getpwuid(geteuid());
        char hostname[HOST_NAME_MAX] = "\0";
        char path[PATH_MAX] = "\0";
        char funfunfunction[256];
        char wheeeeeeeeeee[256];

        gethostname(hostname, HOST_NAME_MAX);
        getcwd(path, PATH_MAX);

        while(1){
            printf("%s@%s %s$ ", pwd->pw_name, hostname, path);
            scanf("%s %s", &funfunfunction, &wheeeeeeeeeee);
            printf("Befehl: %s\nArgument: %s\n", &funfunfunction, &wheeeeeeeeeee);
            if(strcmp(funfunfunction,"ls") != 0 || strcmp(wheeeeeeeeeee, "-al")){
                printf("You did not type ls -al\n");
                return 1;
            }else{
                execlp(funfunfunction, wheeeeeeeeeee);
                return 0;
            }

        }
    }else if(argc == 3){
        if(strcmp(argv[1],"ls") != 0 || strcmp(argv[2], "-al")){
            printf("You did not type ls -al\n");
            return 1;
        }else{
            execlp(argv[1], argv[2]);
            return 0;
        }
    }
    return 1;
}
