main {
  string foo = "zork";
  string bar = "bork";
  int baz    = 27;

  if (foo == "qux") then
    print("Not this branch...");

  elif (bar == "toto") then
    print("Nor this one...");

  elif (baz < 42) then
    print("This is the one");

  fi
};
