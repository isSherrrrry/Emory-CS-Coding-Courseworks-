// THIS CODE IS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING
// A TUTOR OR CODE WRITTEN BY OTHER STUDENTS - Yijun Liu

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <getopt.h>
#include "pbm.h"


// define the statements used 
#define USEAGE "Useage: ppmcvt [-bgirsmtno] [FILE]\n"
#define ERROR_GREY "Error: Invalid max grayscale pixel value: %s; must be less than 65,536\n"
#define ERROR_SCALE "Error: Invalid scale factor: %s; must be 1-8\n"
#define ERROR_NO_INPUT "Error: No input file specified\n"
#define ERROR_NO_OUTPUT "Error: No output file specified\n"
#define ERROR_MULT "Error: Multiple transformations specified\n"
#define red "red"
#define blue "blue"
#define green "green"

// Number of helper functions I used
// for each method 

// print the error statement 

void error(char *errorMessage)
{
    fprintf(stderr, "%s", errorMessage);
    exit(1);
}

// convert the ppm image to pbm
PBMImage * convert_ppm_pbm (PPMImage * ppm)
{
    // create a new pbm image to be returned 
    PBMImage *pbm = new_pbmimage(ppm->width, ppm->height);

    // use the formula Average(R, G, B) < PPMMax/2 to convert ppm to pbm 
    for(int i = 0; i < ppm->height; i++){
        for(int j = 0; j < ppm->width; j++){
            // find Average(R, G, B)
            unsigned int avgRGB = (ppm->pixmap[0][i][j] + ppm->pixmap[1][i][j] + ppm->pixmap[2][i][j]) / 3;
            // compare if Average(R, G, B) < PPMMax/2
            pbm->pixmap[i][j] = (avgRGB < (ppm->max/2)) ? 0:1;
        }
    }
    return pbm;
}

// convert the ppm image to pgm 
PGMImage * convert_ppm_pgm (PPMImage * ppm, unsigned int maxGrey)
{
    // create a new pgm image to be returned 
    PGMImage *pgm = new_pgmimage(ppm->width, ppm->height, maxGrey);

    // use the formula Average(R, G, B)/PPMMax*PGM max to convert ppm to pbm
    for(int i = 0; i < ppm->height; i++){
        for(int j = 0; j < ppm->width; j++){
            //calculate the Average(R, G, B)
            unsigned int avgRGB = (ppm->pixmap[0][i][j] + ppm->pixmap[1][i][j] + ppm->pixmap[2][i][j]);
            // assign the Average(R, G, B)/PPMMax*PGM to the new image 
            pgm->pixmap[i][j] = avgRGB/3.0/ppm->max*maxGrey;
        }
    }
    return pgm;

}

// isolate the specified color channel
void isolate_rgb (PPMImage * ppm, const char * channel)
{
     
    for(int i = 0; i < ppm->height; i++){
        for(int j = 0; j < ppm->width; j++){
           // if the color is red, remove green and blue 
            if(strcmp(channel, "red") == 0){
                ppm->pixmap[1][i][j] = 0;
                ppm->pixmap[2][i][j] = 0;
            }
            // if the color is green, remove red and blue
            if(strcmp(channel, "green") == 0){
                ppm->pixmap[0][i][j] = 0;
                ppm->pixmap[2][i][j] = 0;
            }
            // if the color is blue, remove green and red
            if(strcmp(channel, "blue") == 0){
                ppm->pixmap[0][i][j] = 0;
                ppm->pixmap[1][i][j] = 0;
            }

        }
    }
    
    
}

// remove the specified color channel
void remove_rgb (PPMImage * ppm, const char * channel)
{
    // use index to represent the color channel 
    int index;

    if(strcmp(channel, "red") == 0){
        index = 0;
    }
    if(strcmp(channel, "green") == 0){
        index = 1;
    }
    if(strcmp(channel, "blue") == 0){
        index = 2;
    }

    // clean the color channel 
    for(int i = 0; i < ppm->height; i++){
        for(int j = 0; j < ppm->width; j++){
            ppm->pixmap[index][i][j] = 0;
        }
    }
}

// helper function for apply_sepia
int min(int a, int b){
    if(a <= b) return a;
    else return b;
}

// Use the formulas below to transform the image
// NewR = 0.393(OldR) + 0.769(OldG) + 0.189(OldB) 
// NewG = 0.349(OldR) + 0.686(OldG) + 0.168(OldB) 
// NewB = 0.272(OldR) + 0.534(OldG) + 0.131(OldB)
void apply_sepia (PPMImage * ppm)
{
    // apply the formula 
    for(int i = 0; i < ppm->height; i++){
        for(int j = 0; j < ppm->width; j++){
            ppm->pixmap[0][i][j] = 0.393 * ppm->pixmap[0][i][j] + 0.769 * ppm->pixmap[1][i][j] + 0.189 * ppm->pixmap[2][i][j];
            ppm->pixmap[1][i][j] = 0.349 * ppm->pixmap[0][i][j] + 0.686 * ppm->pixmap[1][i][j] + 0.168 * ppm->pixmap[2][i][j];
            ppm->pixmap[2][i][j] = 0.272 * ppm->pixmap[0][i][j] + 0.534 * ppm->pixmap[1][i][j] + 0.131 * ppm->pixmap[2][i][j];
        }
    }

    // make sure that the caculated value is less than the max value, avoid overflow causing weird colors
    for(int i = 0; i < ppm->height; i++){
        for(int j = 0; j < ppm->width; j++){
            ppm->pixmap[0][i][j] = min(ppm->max, ppm->pixmap[0][i][j]);
            ppm->pixmap[1][i][j] = min(ppm->max, ppm->pixmap[1][i][j]);
            ppm->pixmap[2][i][j] = min(ppm->max, ppm->pixmap[2][i][j]);
        }
    }
}


void vertical_mirror (PPMImage * ppm)
{
    // mirror the first half to second half
    for(int i = 0; i < ppm->height; i++){
        for(int j = 0; j < ppm->width; j++){
            ppm->pixmap[0][i][ppm->width - j - 1] = ppm->pixmap[0][i][j]; // width-j-1 is the second half 
            ppm->pixmap[1][i][ppm->width - j - 1] = ppm->pixmap[1][i][j];
            ppm->pixmap[2][i][ppm->width - j - 1] = ppm->pixmap[2][i][j];
        }
    }
}

PPMImage * reduce_thumbnail (PPMImage * ppm, unsigned int factor)
{
    // calculate the new height and width 
    int new_width = ppm->width/factor;
    int new_height = ppm->height/factor;

    // rounding up as required 
    if(ppm->width % factor){
        new_width++;
    }
    if(ppm->height % factor){
        new_height++;
    }
    //create the output file 
    PPMImage *outImage = new_ppmimage(new_width, new_height, ppm->max);

    // complete the transformation
    for(int i = 0; i < new_height; i++){
        for(int j = 0; j < new_width; j++){
            outImage->pixmap[0][i][j] = ppm->pixmap[0][i*factor][j*factor];
            outImage->pixmap[1][i][j] = ppm->pixmap[1][i*factor][j*factor];
            outImage->pixmap[2][i][j] = ppm->pixmap[2][i*factor][j*factor];
        }
    }

    return outImage;

}

void nup(PPMImage * ppm, unsigned int factor) {
    
    // use the previous reduce_thumbnail to obtian the thumbnail
    PPMImage *new_thumb = reduce_thumbnail(ppm, factor);

    // define variables, new_width is the width of the thubmnail
    int new_width = ppm->width/factor;
    int new_height = ppm->height/factor;

    // indicate whether there's a rounding happening
    int width_flag = 0, height_flag = 0;

    // rounding up as required 
    if(ppm->width % factor){
        new_width++;
        width_flag = 1;
    }
    if(ppm->height % factor){
        new_height++;
        height_flag = 1; 
    }
    // complete the transformation
    for(int i = 0; i < ppm->height; i++){
        for(int j = 0; j < ppm->width; j++){
            // copy the thumbnail to the image
            if(i<new_height && j<new_width){
                ppm->pixmap[0][i][j] = new_thumb->pixmap[0][i][j];
                ppm->pixmap[1][i][j] = new_thumb->pixmap[1][i][j];
                ppm->pixmap[2][i][j] = new_thumb->pixmap[2][i][j];
            }
            else{
                // use the % functionality to always refer back to position where the thubmnail copied
                ppm->pixmap[0][i][j] = new_thumb->pixmap[0][i%new_height][j%new_width];
                ppm->pixmap[1][i][j] = new_thumb->pixmap[1][i%new_height][j%new_width];
                ppm->pixmap[2][i][j] = new_thumb->pixmap[2][i%new_height][j%new_width];
            }
        }
    }

    // set the hieght to black to areas that overflows (cannot have a entire image)
    if(height_flag){
        for(int i = new_height*factor-1; i < ppm->height; i++){
            for(int j = 0; j < ppm->width; j++){
                ppm->pixmap[0][i][j] = 0;
                ppm->pixmap[1][i][j] = 0;
                ppm->pixmap[2][i][j] = 0;
            }
        }
    }

    // set the width to black to areas that overflows (cannot have a entire image)
    if(width_flag){
        for(int i = 0; i < ppm->height; i++){
            for(int j = new_width*factor-1; j < ppm->width; j++){
                ppm->pixmap[0][i][j] = 0;
                ppm->pixmap[1][i][j] = 0;
                ppm->pixmap[2][i][j] = 0;
            }
        }
    }
    // free memory
    del_ppmimage(new_thumb);
}


// Main function 

int main( int argc, char *argv[] )
{
    int opt; 
    char caseChar = 'b'; // default is b
    int hasLine = 0;
    int grayScale, shrinkScale;
    char *channel = NULL, *pEND;
    char *fileName = NULL;
    PPMImage * ppm;
    PBMImage * pbm;
    PGMImage * pgm;
    
    while((opt = getopt(argc, argv, ":bg:i:r:smt:n:o:")) != -1){
        switch (opt)
        {
        //error messages applicable to grayscale
        case 'g': //greyscale 
            if(hasLine){ // if the hasline variable is flagged, there must be some commands input elsewhere
                error(ERROR_MULT);
            }
            caseChar = opt; // set the operation code
            hasLine = 1;

            grayScale = strtol(optarg, &pEND, 10); // get the grayscale number 
            if(grayScale >= 65536 || grayScale <= 0){ // print error if the number out of range
                fprintf(stderr, ERROR_GREY, optarg);
                exit(1);
            }
            break;

        //error messages applicable to channel
        case 'i': // isolate the color 
        case 'r': // remove the color
            if(hasLine){
                error(ERROR_MULT);
            }
            caseChar = opt;
            hasLine = 1;
            channel = optarg;

            if(strcmp(optarg, red)&& strcmp(optarg, green)&& strcmp(optarg, blue)){ // not one of the colors
                fprintf(stderr, "Error: Invalid channel specification: (%s); should be ‘red’, ‘green’, or ‘blue’ \n", optarg);
                exit(1);
            }
            break;

        // error messages applicable for scaling 
        case 't': // get thumbnail
        case 'n': // get nup 
            if(hasLine){
                error(ERROR_MULT);
            }
            caseChar = opt;
            hasLine = 1;

            shrinkScale = strtol(optarg, &pEND, 10); // get shrinksclae number 
            if(shrinkScale > 8 || shrinkScale <1){ // out of range 
                fprintf(stderr, ERROR_SCALE, optarg);
                exit(1);
            }
            break;

        // No specific error messages 
        case 's': // sepia 
        case 'm': // mirror
        case 'b': // ppm to pbm 
            if(hasLine){
                error(ERROR_MULT);
            }
            caseChar = opt;
            hasLine = 1;

            break;
        
        case 'o':
            fileName = optarg;
            break;
        
        case '?':
            error(USEAGE);
            break;

        case ':':
            error(ERROR_NO_INPUT);
            break;
        }
    }

    // output if no file name
    if(fileName == NULL){
        error(ERROR_NO_OUTPUT);
    }

    // read file 
    if (optind == argc - 1) {
        ppm = read_ppmfile(argv[optind]);
    } else {
        error(ERROR_NO_INPUT);
    }

    // case b 
    if(caseChar == 'b'){
        pbm = convert_ppm_pbm(ppm); // create output 
        write_pbmfile(pbm, fileName); // write output 
        del_pbmimage(pbm); // free created pbm memory
        del_ppmimage(ppm); // free input ppm memory
        exit(0);
    }
    if(caseChar == 'g'){
        pgm = convert_ppm_pgm(ppm, grayScale); // create output 
        write_pgmfile(pgm, fileName); // write output 
        del_pgmimage(pgm);  // free created pgm memory
        del_ppmimage(ppm); // free input ppm memory
        exit(0);
    }
    if(caseChar == 'i'){
        isolate_rgb(ppm, channel); // run void function 
        write_ppmfile(ppm, fileName); // write output 
        del_ppmimage(ppm); // free input ppm memory
        exit(0);
    }
    if(caseChar == 'r'){
        remove_rgb(ppm, channel); // run void function 
        write_ppmfile(ppm, fileName); // write output 
        del_ppmimage(ppm); // free input ppm memory
        exit(0);
    }
    if(caseChar == 's'){
        apply_sepia(ppm); // run void function 
        write_ppmfile(ppm, fileName); // write output 
        del_ppmimage(ppm); // free input ppm memory
        exit(0);
    }
    if(caseChar == 'm'){
        vertical_mirror(ppm); // run void function 
        write_ppmfile(ppm, fileName); // write output 
        del_ppmimage(ppm); // free input ppm memory
        exit(0);
    }
    if(caseChar == 't'){
        PPMImage *result = reduce_thumbnail(ppm, shrinkScale); // create output ppm 
        write_ppmfile(result, fileName); // write output 
        del_ppmimage(result); // free created ppm memory
        del_ppmimage(ppm); // free input ppm memory
        exit(0); 
    }
    if(caseChar == 'n'){
        nup(ppm, shrinkScale); // run void function 
        write_ppmfile(ppm, fileName); // write output 
        del_ppmimage(ppm); // free input ppm memory
        exit(0); 
    }

    return 0;
}
