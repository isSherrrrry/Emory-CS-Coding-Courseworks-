// Yijun Liu
// yijun.liu2@emory.edu
// THIS CODE WAS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING ANY SOURCE OUTSIDE OF 
// THOSE APPROVED BY THE INSTURCTOR Yijun Liu

//
//  my_malloc.c
//  Lab1: Malloc
//


#include "my_malloc.h"
#include <unistd.h>
#define MAGICNUM 0xa5231112

// define global variables
MyErrorNo my_errno=MYNOERROR;
static FreeListNode free_list_head = NULL;

void *my_malloc(uint32_t size){

    // initialize the error statements
    // the errors in this function are all MYNOERROR
    my_errno = MYNOERROR;

    // update the size
    // +8 to include header files
    // if it's not a multiple of 8, change it to 
    uint32_t total_size = size + 8;
    if(size % 8 != 0){
        total_size += (8 - size % 8); 
    }
    // if it is smaller than 16, change it to 16
    total_size = (total_size < 16) ? 16 : total_size;
    
    // traverse the list to find 
    FreeListNode record_node = NULL;
    FreeListNode node = free_list_head;

    // #1 senario
    // find free chunks in existing free list 

    while(node != NULL){ // while we have elements in the free list 

        if(total_size <= node->size){ // if we can fit in the node into the existing free chunck

            // when we need to split after fit in
            // meaning that there are remaining empty spaces after allocating out the memory
            if(node->size - total_size > 16){ 
                FreeListNode remaining = (FreeListNode)(((char *)node) + total_size); // create the free list node
                remaining->size = node->size - total_size; // remaining node size
                remaining->flink = node->flink; // connect the remaining to the free list 
                if(node == free_list_head){ // if the node is the first node
                    free_list_head = remaining; // start from remaining
                }
                else{ // not the first node, skip the node directly, connect to remaining
                    record_node->flink = remaining;
                }
            }
            else{
                // when no need to split, fit in perfectly
                // the total size might be larger since the difference can be anything below 16
                total_size = node->size;
                if(node == free_list_head){ // if the node is the first node
                    free_list_head = node->flink; // skip the first node
                }
                else{ // not the first node, skip the node directly
                    record_node->flink = node->flink;
                }
            }

            // put in the size & magicnumber (signature)
            *((uint32_t *)((char *)node)) = total_size;
            *((uint32_t *)((char *)node + 4)) = MAGICNUM;

            // return without the header
            return ((char *)node) + 8;
            
        }

        // next node
        record_node = node;
        node = node->flink;
    }

    // #2 need new memory

    // find the size we need to increment for
    uint32_t increment_size = (total_size <= 8192) ? 8192 : total_size;
    void *new_memory = sbrk(increment_size);

    // if there's an error 
    if(new_memory == (void *)-1) return NULL;

    // define the header files
    uint32_t *header_chunk = new_memory;
    *header_chunk = total_size;
    *(header_chunk + 1) = MAGICNUM; // +1 because it's unit32_t, +1 = 4 bytes 

    // splitting for the newly allocated memory if needed
    uint32_t remain_size = increment_size - total_size;
    if (remain_size > 16) {
        FreeListNode new_node = (FreeListNode)((char *)new_memory + total_size);
        new_node->size = remain_size; // create the node add to the free list
        new_node->flink = NULL; // flink should be NULL for the end of the element
        // if the free list is empty, it becomes the first node in the free list
        if (!free_list_head) {
            free_list_head = new_node;
            return (void *)new_memory + 8;
        } 
        
        // traverse to the last and add the new node to the end of the list
        FreeListNode find = free_list_head;
        while (find->flink) {
            find = find->flink;
        }
        find->flink = new_node;
    }
    
    return (void *)new_memory + 8;

}

void my_free(void *ptr) {
    // set the error number to indicate the error come from free pointer
    my_errno = MYBADFREEPTR; 
    
    //check for potential errors 
    if (ptr == NULL || ptr > sbrk(0)) return; // don't fee the memory 
    
    // find the header files
    ptr = (void *)ptr - 8;
    uint32_t *header = (uint32_t *)(ptr); // cast ptr to uint32_t * to read the header files
    if (*(header + 1) != MAGICNUM) return; // return without free if the header file does not exist

    // set the error back to the memory error; if the program fails yet run to here, it would be a memory error
    my_errno = MYNOERROR;

    // set a new pointer at ptr 
    FreeListNode new_node = (FreeListNode)ptr;
    new_node->size = *header; // check the size and read it the size
    
    // the new chunk is after the free list head, so we need to traverse
    if(new_node > free_list_head){
        // find the position of the free chunk in the free list
        FreeListNode loc = free_list_head;
        // if the free list head is null, we set it at the beggining and return 
        if(!free_list_head){
            new_node->flink = free_list_head;
            free_list_head = new_node;
            return;
        }
        // traverse
        while (loc->flink && loc->flink < new_node) {
            loc = loc->flink;
        }
        // add the new_node to the list 
        new_node->flink = loc->flink;
        loc->flink = new_node;
    }
    else{ // before the free list
        new_node->flink = free_list_head;
        free_list_head = new_node;
    }

}


// find the first node of the free list, which is defined in the file globally
FreeListNode free_list_begin(){
    return free_list_head;
}

// merge locally adjacent chunks on the free list into single larger chunks
void coalesce_free_list(){
    if (!free_list_head) { // there is no free list
        return;
    }

    FreeListNode current_node = free_list_head; // start from the first node
    while(current_node && current_node->flink){ //when there is a next 
        if((char *)(current_node) + current_node->size == (char *)(current_node->flink)){ //if the memories are locally adjacent
            current_node->size += current_node->flink->size; //merge the size by combining them 
            current_node->flink = current_node->flink->flink; //merge the flink, set to the flink of the next chunck
            continue; // break the loop
        }
        current_node = current_node->flink; //continue to check the next node
    }
}

