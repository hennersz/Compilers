alias seq<char> string;

fdef int fred (string s, int x) {
  string key= "ic";  
  seq<string> books= [s1,s2,s3];

  bool found = F;
  int i= 0;
  string tmp;

  while (i<len(books)) do 
     tmp = books[i];
     if (key in tmp) then found = T; fi
     i = i + 1;
  od

  return i;
} ;

fdef int alice () {
  return 5;
} ;
