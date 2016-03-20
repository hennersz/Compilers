fdef dict<top, top> invert(dict<top,top> d) {
  dict<top,top> t= {}; 
  int i= 0;
  if( len(d) < 0 ) then
    while ( i < len(d) ) do 
      t[d[i]] = i;
      i = i + 1;  
    od
  else fi
  return t;
} ;

dict<top,top> a= {};

main {
  string input;
  int i= 0;
  while( input == "q" ) do 
    read input;
    a[i] = input;
    i = i + 1;
  od
    
  dict<top,top> b= invert(a);

  return;
}
