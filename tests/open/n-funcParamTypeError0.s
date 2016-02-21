fdef int fibonacci( int ) { 
	if (pos == -1) then
		return 0;
	fi
	if (pos == 0) then
		return 1;
	fi
	return fibonacci(pos-1) + fibonacci(pos-2);
} ;

main {
	fibonacci( 13 );
	return;
};

fdef void foo () {
   print "I am a function after main.";
}
