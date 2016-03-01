tdef kp { string hackathon, bool happy };

fdef bool isHappy(string question) {
    print("Does " + question + " make you happy or sad?\n");
    return T;
};
main {
    kp instance;
    print("Is KP currently happy?\n");
    read kp.happy;
    print "What is the name of the next hackathon kp is organizing?\n";
    read kp.hackathon ;
    bool wrong;
    print "Is there anything that is currently going wrong?";
    read wrong;
    if(wrong) then
        print "What went /""wrong?";
        string thingthatwentwrong;
        read thingthatwentwrong;
        isHappy(thingthatwentwrong);
    else 
        isHappy("there arent enough tables");
    fi
    kp.happy = F;
    return kp;
};
