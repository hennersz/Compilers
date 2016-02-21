main {
	alias seq<char> string;
	alias fred spud;
	tdef person { string name, string surname, int age};
	tdef family { person mother, person father, seq<person> children };
	return;
};
