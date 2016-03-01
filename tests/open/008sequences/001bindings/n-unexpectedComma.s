main {
    seq<int> s = [];
    seq<int> s = [0];
    seq<int> s = [0,1,2];
    seq<top> s = [0, 0.1, (3+4)];
    seq<top> s = [0, 0.1, (3+4), [0, 0.1, (3+4)]];
    seq<char> s = "hello";
    seq<char> s = "";
    
    ## fail
    seq<int> s = [,];
    
};