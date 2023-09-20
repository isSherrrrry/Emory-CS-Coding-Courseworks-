void nearest();
void prime(int n);
void rem(char *s);
void perfect(long n);

int main(int argc, char const *argv[])
{
    for(int i = 1; i< 20; i++){
        prime(i); 
    }
    rem("I am writing a sentence.");
    for(int i = 2; i< 30; i++){
        perfect(i); 
    }
    nearest();
    return 0;
}
