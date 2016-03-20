seq<string> l1= ["a","b", "c", "d", "e"];
seq<top> l2= [1,2,3,4,5];
 
string s1= "";
string s2= "hello";

main {

  seq<top> fnewlist= l1 :: l2;
  seq<top> anotherlist= [s1] :: [s2] :: newlist;
  seq<top> thirdlist= l2[:-2] :: l2[3:];

  bool b= len(thirdlist) == len(l2);


  if (len(newlist) <= len(anotherlist)) then
     newlist = newlist + anotherlist[0];
  else 
     nelist = newlist - newlist[0];
  fi
  
  return;
};
