// Yijun Liu
// yijun.liu2@emory.edu
// THIS CODE WAS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING ANY SOURCE OUTSIDE OF 
// THOSE APPROVED BY THE INSTURCTOR Yijun Liu

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <stdint.h>
#include <getopt.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <time.h>
#include <unistd.h>
#include <dirent.h>
#include <fcntl.h> 
#include <utime.h>
#include <sys/time.h>
#include <errno.h>


#include "inodemap.h"

// define the error statements used 
#define ERROR_NO_TARFILE "Error: No tarfile specified.\n"
#define ERROR_MULT "Error: Multiple modes specified.\n"
#define ERROR_NO_MODE "Error: No mode specified.\n"
#define ERROR_NO_DIR "Error: No directory target specified.\n"

#define ERROR_NO_TARGET "Error: Specified target(%s does not exist.)\n"
#define ERROR_NOT_DIR "Error: Specified target(%s is not a directory.)\n"
#define ERROR_MAG_NUM "Error: Bad magic number %d, should be: %d.\n"

// define the magic number
#define MAGIC_NUM 0x7261746D

// helper functions to print error message and exit
void error(char *errorMessage){
    fprintf(stderr, "%s", errorMessage);
    exit(-1);
}
void p_error(char *errorMessage){
    perror(errorMessage);
    exit(-1);
}

const char* remove_dots(const char *path) {
    if(strncmp(path, ".", 1) != 0) return path;
    while (strncmp(path, "./", 2) == 0 || strncmp(path, "../", 3) == 0) {
        if (strncmp(path, "./", 2) == 0) {
            path += 2;
        } else if (strncmp(path, "../", 3) == 0) {
            path += 3;
        }
    }
    return path;
}

// recursive function to find all the files in the directory
void find_directory(int fd, const char* filepath){

    // open the directory
    DIR *dir = opendir(filepath);
    if(!dir){
        fprintf(stderr, ERROR_NOT_DIR, filepath);
        exit(-1);
    }
    
    struct dirent *d;

    // metadata
    uint64_t inode_num;
    uint32_t name_len;
    uint32_t file_mode;
    uint64_t file_modefication_time;
    uint64_t file_size;

    // reformat the filepath, get the name of the directory
    const char *temp_1_dir = remove_dots(filepath);
    const char *temp_2_dir = temp_1_dir ? temp_1_dir: filepath;

    // get the metadata of the directory
    struct stat s_dir;
    if(stat(filepath, &s_dir)<0) p_error("Stat");
    inode_num = s_dir.st_ino;
    file_mode = s_dir.st_mode;
    file_modefication_time = s_dir.st_mtime;
    name_len = strlen(temp_2_dir);

    // write the metadata to the tarfile, write here for the directory
    if(write(fd, &inode_num, sizeof(inode_num))<0) p_error("Write");
    if(write(fd, &name_len, sizeof(name_len))<0) p_error("Write");
    if(write(fd, temp_2_dir, name_len)<0) p_error("Write");
    if(write(fd, &file_mode, sizeof(file_mode))<0) p_error("Write");
    if(write(fd, &file_modefication_time, sizeof(file_modefication_time))<0) p_error("Write");

    // read the directory using a while loop
    while((d = readdir(dir))){
        // ignore the . and .. soft links
        if(strcmp(d->d_name, ".") == 0||strcmp(d->d_name, "..") == 0) continue;

        // reformat the filepath
        const char *temp_1 = remove_dots(filepath);
        const char *temp_2 = temp_1 ? temp_1: filepath;

        // dynamically allocate the path memory & store the formatted path
        char *path = malloc(strlen(temp_2)+strlen(d->d_name)+2);
        if(sprintf(path, "%s/%s", temp_2, d->d_name)<0) p_error("Sprintf"); 

        // this is the actual full path that uses to get metadata 
        char *actual_path = malloc(strlen(filepath)+strlen(d->d_name)+2);
        if(sprintf(actual_path, "%s/%s", filepath, d->d_name)<0) p_error("Sprintf");

        // get the metadata of the file
        struct stat s;
        if(stat(actual_path, &s)<0) p_error("Stat");
        if(S_ISLNK(s.st_mode)){
            continue;
        }

        // set the metadata
        inode_num = s.st_ino;
        file_mode = s.st_mode;
        file_modefication_time = s.st_mtime;
        name_len = strlen(path);
        file_size = s.st_size;

        // if the file is a regular file
        if(S_ISREG(s.st_mode)){
            // write these for both hard links and regular files
            if(write(fd, &inode_num, sizeof(inode_num))<0) p_error("Write");
            if(write(fd, &name_len, sizeof(name_len))<0) p_error("Write");
            if(write(fd, path, name_len)<0) p_error("Write");

            
            //write these for regular files only
            if(!get_inode(inode_num)){
                if(write(fd, &file_mode, sizeof(file_mode))<0) p_error("Write");
                if(write(fd, &file_modefication_time, sizeof(file_modefication_time))<0) p_error("Write");
                if(write(fd, &file_size, sizeof(file_size))<0) p_error("Write");

                // read the contents
                int temp_fd = open(actual_path, O_RDONLY);
                if(temp_fd < 0) p_error("Open");


                char buf[2048];
                size_t byte_read;
                size_t byte_writ;

                while((byte_read = read(temp_fd, buf, sizeof(buf)))>0){
                    char *writen = buf;
                    while(byte_read > 0){
                        byte_writ = write(fd, writen, byte_read);
                        if (byte_writ < 0){
                            p_error("Write");
                            break;
                        }
                        byte_read -= byte_writ;
                        writen += byte_writ;
                    }
                }


                if(close(temp_fd)<0) p_error("Close");

                // add to the map
                set_inode(inode_num, path);
                
            }
        }
        // if is it a directory 
        if(S_ISDIR(file_mode)){
            find_directory(fd, actual_path);
        }
        // free malloc memory
        free(path);
        free(actual_path);
    }
    closedir(dir);
}

// create the tarfile
void create_mytar(const char* tarfile, const char* filepath){

    // create a new file
    int fd = open(tarfile, O_CREAT|O_WRONLY|O_TRUNC, 0644);
    if(fd < 0){
        fprintf(stderr, ERROR_NO_TARGET, tarfile);
        exit(-1);
    }

    // write magic number
    uint32_t magic_number = MAGIC_NUM;
    if(write(fd, &magic_number, sizeof(uint32_t))<0) p_error("Write");

    // find the directory
    find_directory(fd, filepath);

    if(close(fd)<0) p_error("Close"); // close the file
}

// extract the tarfile
void extract_mytar(const char *tarfile){
    
    // open the tarfile
    int fd = open(tarfile, O_RDONLY);
    if(fd < 0){
        fprintf(stderr, ERROR_NO_TARGET, tarfile);
        exit(-1);
    }
    // check magic number
    uint32_t magic_num;

    if(read(fd, &magic_num, sizeof(magic_num))<0) p_error("Read");

    if(magic_num != MAGIC_NUM){
        fprintf(stderr, ERROR_MAG_NUM, magic_num, MAGIC_NUM);
        exit(-1);
    }

    uint64_t inode_num;

    // keep track of the inode name for hard links
    char inode_name_here[100000][40];
    unsigned int mode_value = 1037;

    // start reading the files
    while(read(fd, &inode_num, sizeof(inode_num))){

        uint32_t name_len;
        if(read(fd, &name_len, sizeof(name_len))<0) p_error("Read");

        // read the name, add a null terminator
        char *name = malloc(name_len+1);
        if(read(fd, name, name_len)<0) p_error("Read");
        name[name_len] = '\0';

        // if the file is a hard link, check here because it only has inode number, name len, and name
        if(get_inode(inode_num)){
            // skip if the error is the file already exists 
            if(link(inode_name_here[inode_num % mode_value], name)<0){
                if(errno != EEXIST) p_error("Link");
            }
            // break out of teh loop
            continue;
        }

        // get metadata

        uint32_t file_mode;
        if(read(fd, &file_mode, sizeof(file_mode))<0) p_error("Read");

        uint64_t file_modefication_time;
        if(read(fd, &file_modefication_time, sizeof(file_modefication_time))<0) p_error("Read");

        uint64_t file_size;

        // if the file is a regular file
        if(S_ISREG(file_mode)){
            // set the inode name
            if(get_inode(inode_num) == NULL){
                set_inode(inode_num, name);
                strcpy(inode_name_here[inode_num % mode_value], get_inode(inode_num));
            }

            if(read(fd, &file_size, sizeof(file_size))<0) p_error("Read");

            // update the time of the file
            struct timeval timeval[2];
            if(gettimeofday(&timeval[0], NULL)<0) p_error("Gettimeofday");
            timeval[1].tv_sec = file_modefication_time;
            timeval[1].tv_usec = 0;            

            // create the file to be extracted
            int temp_fd = open(name, O_CREAT|O_WRONLY|O_TRUNC, file_mode);
            if(temp_fd < 0) p_error("Open");
            if(utimes(name, timeval)<0) p_error("Utimes");
            
            // write the file contents
            char buf[2048];
            size_t byte_read;
            size_t byte_writ;

            while((byte_read = read(fd, buf, sizeof(buf)))>0){
                char *writen = buf;
                while(byte_read > 0){
                    byte_writ = write(temp_fd, writen, byte_read);
                    if (byte_writ < 0){
                        p_error("Write");
                        break;
                    }
                    byte_read -= byte_writ;
                    writen += byte_writ;
                }
            }

            if(close(temp_fd)<0) p_error("Close");
        }

        // a directory 
        else if(S_ISDIR(file_mode)){
            // create the directory
            // did not check to avoid exist error
            mkdir(name, file_mode);
        }

        free(name);
    }
    if(close(fd)<0) p_error("Close");
}

// print the tarfile metadata
void print_mytar(const char *tarfile){
    
    // open the tarfile
    int fd = open(tarfile, O_RDONLY);
    if(fd < 0){
        fprintf(stderr, ERROR_NO_TARGET, tarfile);
        exit(-1);
    }

    // check magic number
    uint32_t magic_num;
    if(read(fd, &magic_num, sizeof(magic_num))<0) p_error("Read");
    if(magic_num != MAGIC_NUM){
        fprintf(stderr, ERROR_MAG_NUM, magic_num, MAGIC_NUM);
        exit(-1);
    }

    uint64_t inode_num;

    // read the files
    while(read(fd, &inode_num, sizeof(inode_num))){

        uint32_t name_len;
        if(read(fd, &name_len, sizeof(name_len))<0) p_error("Read");

        // read the name, add a null terminator
        char *name = malloc(name_len+1);
        if(read(fd, name, name_len)<0) p_error("Read");
        name[name_len] = '\0';

        // if the file is a hard link, check here because it only has inode number, name len, and name
        if(get_inode(inode_num)){
            printf("%s/ -- inode: %lu\n", name, inode_num);
            continue;
        }
        
        // read more metadata
        uint32_t file_mode;
        if(read(fd, &file_mode, sizeof(file_mode))<0) p_error("Read");
        uint32_t file_mode_print = file_mode % 01000; // keep the last three digits for the mode

        uint64_t file_modefication_time;
        uint64_t file_size;
        if(read(fd, &file_modefication_time, sizeof(file_modefication_time))<0) p_error("Read");

        // if the file is a regular file
        if(S_ISREG(file_mode)){

            if(read(fd, &file_size, sizeof(file_size))<0) p_error("Read");
            // if the file is excutable 
            else if(file_mode & S_IXUSR) printf("%s * -- inode: %lu, mode: %o, mtime: %lu, size: %lu\n", name, inode_num, file_mode_print, file_modefication_time, file_size);
            else printf("%s -- inode: %lu, mode: %o, mtime: %lu, size: %lu\n", name, inode_num, file_mode_print, file_modefication_time, file_size);
            // skip the file contents
            if(lseek(fd, file_size, SEEK_CUR)<0) p_error("Lseek"); 
            // set inode
            set_inode(inode_num, name);
        }
        // if the file is a directory
        else if(S_ISDIR(file_mode)){
            printf("%s/ -- inode: %lu, mode: %o, mtime: %lu\n", name, inode_num, file_mode_print, file_modefication_time);
        }
        free(name);
    }
    if(close(fd)<0) p_error("Close");
}

int main( int argc, char *argv[] )
{
    int opt; 
    char caseChar = 'm'; // random character to check if updated or not 
    int hasLine = 0;
    char *filename = NULL, *filepath = NULL;

    while((opt = getopt(argc, argv, ":cxtf:")) != -1){
        switch (opt)
        {
        case 'c': 
        case 'x':
        case 't': 
            if(hasLine){ // if the hasline variable is flagged, there must be some commands input elsewhere
                error(ERROR_MULT);
            }
            caseChar = opt; // set the operation code
            hasLine = 1;
            break;

        case 'f':
            filename = optarg; // set filename 
            break;
        
        case ':':
            error(ERROR_NO_MODE);
            break;
        }
    }
    // output if no file name
    if(caseChar == 'm'){
        error(ERROR_NO_MODE);
    }
    // if we need to create the file, get the file path 
    if (caseChar == 'c' && optind != argc - 1) {
        error(ERROR_NO_TARFILE);
    }
    else{
        filepath = argv[optind];
    }

    // if we need to extract or list the file, check if the directory is inputted 
    if ((caseChar == 'x' | caseChar == 't') && !filename) {
        error(ERROR_NO_DIR);
    }

    // create!
    if(caseChar == 'c'){
        create_mytar(filename, filepath);
        exit(0);
    }

    // extract!
    if(caseChar == 'x'){
        extract_mytar(filename);
        exit(0);
    }

    // print!
    if(caseChar == 't'){
        print_mytar(filename);
        exit(0);
    }
   
    return 0;
}
