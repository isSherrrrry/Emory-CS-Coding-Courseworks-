// THIS CODE IS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING
// A TUTOR OR CODE WRITTEN BY OTHER STUDENTS - Yijun Liu

#include "pbm.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

PPMImage * new_ppmimage( unsigned int w, unsigned int h, unsigned int m )
{
    // create a new ppm inmage and allocate the memory for the pointer 
    PPMImage *img = malloc(sizeof(*img));

    // assign the height, width, and max values 
    img->height = h;
    img->width = w;
    img->max = m;
    
    // assign memory for the pixmap array
    for(int i = 0; i <3; i++){
        img->pixmap[i] = malloc(h * sizeof(unsigned int *));
        for (int j = 0; j < h; j++)
        {
            img->pixmap[i][j] = malloc(w * sizeof(unsigned int));
        }
    }

    return img;
}

PBMImage * new_pbmimage( unsigned int w, unsigned int h )
{
    // create a new pbm inmage and allocate the memory for the pointer
    PBMImage *img = malloc(sizeof(*img));

    // assign the height and width values; assign the memeory for the array pointer
    img->height = h;
    img->width = w;
    img->pixmap = malloc(h * sizeof(unsigned int *));
    
    // assign memory for the pixmap array
    for(int i = 0; i <h; i++){
        img->pixmap[i] = malloc(w * sizeof(unsigned int));
    }

    return img;
}

PGMImage * new_pgmimage( unsigned int w, unsigned int h, unsigned int m )
{
    // create a new pgm inmage and allocate the memory for the pointer
    PGMImage *img = malloc(sizeof(*img));

    // assign the height, width, and max values 
    img->height = h;
    img->width = w;
    img->max = m;

    // assign the memeory for the array pointer
    img->pixmap = malloc(h * sizeof(unsigned int *));
    
    // assign memory for the pixmap array
    for(int i = 0; i <h; i++){
        img->pixmap[i] = malloc(w * sizeof(unsigned int));
        
    }
    return img;
}



void del_ppmimage( PPMImage * p )
{
    // free the pixmap array 
    for(int i = 0; i <3; i++)
    {
        for (int j = 0; j < p->height; j++)
        {
            free(p->pixmap[i][j]);
        }
        free(p->pixmap[i]);
    }

    // free the image pointer 

    free(p);
}

void del_pgmimage( PGMImage * p )
{
    // free the pixmap array 
    for(unsigned int i = 0; i <p->height; i++){
        free(p->pixmap[i]);   
    }

    // free the array pointer
    free(p->pixmap);

    // free the image pointer 
    free(p);
}

void del_pbmimage( PBMImage * p )
{
     // free the pixmap array 
    for(unsigned int i = 0; i <p->height; i++){
        free(p->pixmap[i]);   
    }

    // free the array pointer
    free(p->pixmap);

    // free the image pointer 
    free(p);
}





