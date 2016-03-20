/# This is going to be a very well documented piece of code that does absolutely    nothing
#/

alias int test;

int x = 5;

# Very unefficient way to test if bool is odd
# we have the computing power so why not use it
fdef bool isOdd(int num) {
  int i = 0;
  bool odd = F;

  while (i != num) do
    odd = !odd;
    i = i + 1;
  od

  return odd;
};

/# Why use a not statement when you can redeclare the function? #/
fdef bool isEven(int num) {
  int i = 0;
  bool even = T;

  while (i != num) do
    even = !even;
    i = i + 1;
  od

  return even;
};

/# Allows you to enter a number
#/ And prints whether it is 
even or odd 
#/

main {
    print("Enter a number: /n");
    test number;
    read number;
    bool isEven = isEven(number);
    bool isOdd = isOdd(number);

    if(isEven) then
        print "The number is even";
    elif(isOdd) then
        print "The number is odd";
    fi

    return 0;
};
