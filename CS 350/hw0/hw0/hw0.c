/* THIS CODE WAS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING ANY
SOURCES OUTSIDE OF THOSE APPROVED BY THE INSTRUCTOR. Yijun Liu */

#include <stdio.h>
#include <stdlib.h>

// Question 1

//determine whether the number is a prime number
int isPrime(int n){
    if(n <= 1) return 0;
    for(int i = 2; i < n; i++){
        if(n % i == 0){ //if it is divisible by any number, then it's not a prime number
            return 0;
        }
    }
    return 1;
}

//driver function for question 1
void prime (int n){
    int start = 2, count = 0; //smallest prime starts from 2
    int resultQ1[n]; //array to record the results

    while(n){ //continue finding until n becomes 0
        if(isPrime(start)){ 
            resultQ1[count] = start;
            count++;  
            n--; //decrease the number of prime numbers we need to find
        }
        start++;
    }

    //print out the result
    for (int i = 0; i < count; i++)
    {
        printf("%d", resultQ1[i]);
        if (i < count-1) printf(", "); //no comma for the last element 
    }
    printf("\n");
    
}


//Question 2

void rem(const char *s){ 
    int len;
    for(len = 0; s[len] != '\0'; len++); //find the length of the string
    char resultQ2[len]; 

    int i, j = 0;
    for(i = 0; i <len; i++){
        if (s[i] == 'a' || s[i] == 'e' || s[i] == 'i' || s[i] == 'o' || s[i] == 'u' 
        || s[i] == 'A' || s[i] == 'E' || s[i] == 'I' || s[i] == 'O' || s[i] == 'U'){
            continue; //do not record those vowels into the new result array
        }
        resultQ2[j] = s[i]; //copy the elements from the original array 
        j++; //index for the new char array 
    }
    resultQ2[j] = '\0'; 

    //output for original array 
    for (int k = 0; k < len; k++)
    {
        printf("%c", s[k]);
    }
    printf("\n");

    //output for new modified array 
    for (int k = 0; k < j; k++)
    {
        printf("%c", resultQ2[k]);
    }
    printf("\n");
    
}

//Question 3

void perfect(long num){
    long resultQ3 = 0; //sum of its factors 
    for (long i = 1; i < num/2+1; i++) //find a number's factors
    {
        if(num % i == 0) resultQ3 += i; //if it is a factor, add to result
    }
    if(resultQ3 == num) printf("%ld is perfect\n", num); //compare whether the sum of all factors is same as the original number
    else printf("%ld is not perfect\n", num);
    
}

//Question 4

//find the nearest prime number
void findNearest(int n){
    //handling special case when n=1
    if(n == 1){ 
        printf("The nearest prime number to 1 is 2 \n");
        return;
    }

    //if it's a prime number, just print it
    if(isPrime(n)){
        printf("The number %d is prime.\n", n);
        return;
    }
    
    //find the nearest prime in two directions
    int upper = n+1;
    int lower = n-1;

    //find the nereast prime in the upper direction (increase the number)
    while(!isPrime(upper)){
        upper += 1;
    }

    //find the nereast prime in the lower direction (decrease the number)
    while(!isPrime(lower)){
        lower -= 1;
    }

    //compute the distance of the prime numbers found to the original number
    int upperDiff = upper - n;
    int lowerDiff = n - lower;

    //compare and print the ones with smaller distance; when the difference is same, print out the smaller one
    if(upperDiff < lowerDiff) printf("The nearest prime number to %d is %d \n", n, upper);
    else printf("The nearest prime number to %d is %d \n", n, lower);
    return;
}


//driver function for question 4
void nearest(void){
    printf("Enter a positive integer: ");

    int x; //input digit
    int num = 0; //true inputted number 
    
    //get user input
    while ((x = getchar()) != EOF)
    {
        //convert each digit inputted to a number untiil a new line
        if(x != '\n') num = num * 10 + (x - '0');

        //when reach the new line, start finding the nearest number 
        if(x == '\n'){
            findNearest(num);
            //reset the number inputted to 0
            num = 0;
            //prompt the text again 
            printf("Enter a positive integer: ");
        }
    }
    return;
}


