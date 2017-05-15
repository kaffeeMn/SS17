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
        char method[256];
        char args[256];

        gethostname(hostname, HOST_NAME_MAX);
        getcwd(path, PATH_MAX);

        while(1){
            printf("%s@%s %s$ ", pwd->pw_name, hostname, path);
            scanf("%s %s", &method, &args);
            printf("Befehl: %s\nArgument: %s\n", &method, &args);
            if(strcmp(method,"ls") != 0 || strcmp(args, "-al") != 0){
                printf("You did not type ls -al\n");
            }else{
                execlp(method, args, ".");
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
