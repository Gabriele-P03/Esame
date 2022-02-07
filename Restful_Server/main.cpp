/**
 * 
 * Encrypting Function Developing...
 * 
 */ 

#include <string>

using namespace std;

int main(int argv, char* argc[]){

    string* buffer = new string(argc[0]);

    //Retrieve how many times it has to mix
    int times = atoi(&buffer->at(0));
    //Store the half of string. Being intger, even if it is even, it will be roof
    int halfChars = buffer->length()/2;
    
    for(int i = 0; i < halfChars; i++){
        int corrsp = buffer->length() - 1 - i;

    }
}