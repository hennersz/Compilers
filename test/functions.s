fdef string helloworld() {
    return "Hello World";
};

fdef int fact(int i) {
    int result = 1;
    while(i > 1) do
        result = result * i;
        i = i - 1;
    od
    return result;
};

fdef bool random() {
    return 4; # Random from dice roll
    
};
