#include <stdio.h>
//#include <unistd.h>
#include <stdlib.h>
#include <pwd.h>

int main(int argc, char **argv){
    struct passwd *pwd;
    char *person = argv[1];
    pwd = getpwnam(person);
    printf("%s\tuser: %d\n", person, pwd->pw_uid);
    printf("%s\tgroup: %d\n", person, pwd->pw_gid);
    return 0;
}
