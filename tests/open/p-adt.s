
tdef person {string name, string surname, int age}; # person fdefinition

tdef family {person mother, person father, seq<person> children}; # family fdefinition

main {

# here we generate
/# a family #/

  person m= "aaaaAAA", "bbBB0_i", 40;
  person p= "aaabAAA", "bbBB0_i", 35;
  person c1= "aaabAAA", "bbBB0_i", 1;
  person c2= "aaadAAA", "bbBB0_i", 2;
  person c3= "aaaeAAA", "bbBB0_i", 3;

  family f= m,p,[c1,c2];
  f.children = f.children :: [c3];

  return;
};

fdef void bar() {
  print "Another function after main.";
};
