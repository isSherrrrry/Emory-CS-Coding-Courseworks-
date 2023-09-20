#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <stdio.h>
#include <stdint.h>

int main() {
    const char *filename = "output.txt";
    int fd = open(filename, O_CREAT | O_WRONLY | O_TRUNC, 0644);

    if (fd < 0) {
        perror("Open");
        return 1;
    }

    uint32_t magic_number = 0x7261746D;
    ssize_t written = write(fd, &magic_number, sizeof(magic_number));

    if (written != sizeof(magic_number)) {
        perror("Write");
        close(fd);
        return 1;
    }

    close(fd);
    return 0;
}
