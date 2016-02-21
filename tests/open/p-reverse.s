main {
	seq<int> a = [1,2,3];
	seq<int> b = reverse(a);
	return b;
};

fdef seq<top> reverse (seq<top> inseq) { 
	seq<top> outseq = [];
	int i = 0;
    	while (i < len(l)) do
		outseq = inseq[i] :: outseq;
		i = i + 1;
   	od
	return outseq; 
} ;


