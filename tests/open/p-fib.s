main {
	fibonacci( 13 );
	return;
};

fdef int fibonacci( int pos ) { 
	if (pos == -1) then
		return 0;
	fi
	if (pos == 0) then
		return 1;
	fi	
	return fibonacci(pos-1) + fibonacci(pos-2);
} ;
