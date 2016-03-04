tdef kp { string hackathon, bool happy };

fdef bool isHappy(string question) {
    print("Does " + question + " make you happy or sad?\n");
    return T;
};
main {
    kp instance;
    bool wrong;
    print("Is KP currently happy?\n");
    read kp.happy;
    print "What is the name of the next hackathon kp is organizing?\n";
    read kp.hackathon ;
    print "Is there anything that is currently going wrong?";
    read wrong;
    if(wrong) then
        string thingthatwentwrong;
        print "What went wrong?";
        read thingthatwentwrong;
        isHappy(thingthatwentwrong);
    else 
        isHappy("there arent enough tables");
    fi
    kp.happy = F;
    return kp;
};
