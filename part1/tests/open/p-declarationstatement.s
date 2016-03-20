int test = 3;
alias int hello;
tdef person  {string firstname, string lastname, int age};

fdef int getAge(person p) {
    return p.age;
};

main {
    person henry = "Henry", "Mortimer", 20;
    person axel = "Axel", "Goetz", 19;
    return [henry, axel]; 
};
