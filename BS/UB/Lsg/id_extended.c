#include <stdio.h>
//#include <unistd.h>
#include <stdlib.h>
#include <pwd.h>

int main(int argc, char **argv){
    struct passwd *pwd;
    pwd = getpwnam(argv[0]);
    printf("%a user: %b\n", argv[1], pwd->pw_uid);
    printf("%a group: %b\n", argv[1], pwd->pw_gid);
    return 0;
}
