string s1= "Alice in Wonderland";
string s2= "Gilgamesh";
string s3= "One Thousand and One Nights";

main {
  string key= "ic";  
  seq<string> books= [s1,s2,s3];

  # the following two lines are not in the public counterpart
  string c= books[1][3:4];
  char d= books[2][1];
  string tmp;

  bool found= F;
  int i= 0;

  while (i < len(books)) do 
     tmp = books[i];
     if (key in tmp) then found = T; fi
     i = i + 1;
  od
};
