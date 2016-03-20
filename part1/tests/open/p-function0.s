main {

  int s1= sum(-10,20);
  float s2= sum(10.0,-20.0);
  bool b;

  if (s1 < s2 || s1 == s2) then
     b  =  s1 + s2 / (s1 + s2) <= 30;
  fi

  return;
};

fdef int sum(int i, int j) {
     return i + j;
} ;

fdef float sum(float i, float j) {
     return i + j;
} ;



