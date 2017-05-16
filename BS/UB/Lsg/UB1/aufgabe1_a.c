#include <stdio.h>
#include <unistd.h>
#include <pwd.h>
#include <sys/types.h>
#include <limits.h>

int main(void){
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
    }

    return 0;
}
