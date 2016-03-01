import subprocess
import time
import os
import sys


def getFileNames():
    files = [val for sublist in [[os.path.join(i[0], j) for j in i[2]] for i in os.walk('./tests')] for val in sublist]
    negatives = []
    positives = []
    for file in files:
        if "n-" in file:
            negatives.append(file)
        elif "p-" in file:
            positives.append(file)
    return {'negatives': negatives, 'positives': positives}


def runtests(files, positive):
    testfailed = 0
    if(positive):
        print("Testing positive files")
    else:
        print("Testing negaive files")

    string = 'bin/:lib/java-cup-11b-runtime.jar'
    progress = 1.0
    total = len(files)

    for file in files:
        percentage = int((progress/total)*100.0)
        hashes = '#' * percentage
        spaces = ' ' * (100 - percentage)
        p = subprocess.Popen(['java', '-cp', string, 'SC', file], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        err, out = p.communicate()
        if err == '':
            if positive:
                if "error" in out:
                    print "Failed test :("
                    print "Run the test again: java -cp " + string + " SC " + file
                    testfailed += 1
            else:
                if "error" not in out:
                    print "Failed test :("
                    print "Run the test again: java -cp " + string + " SC " + file
                    testfailed += 1
        progress+=1
        sys.stdout.write("\rPercent: [{0}] {1}%".format(hashes + spaces,percentage))
        sys.stdout.flush()
    print ' '
    return testfailed

if __name__ == '__main__':
    print "This might take a while..."

    subprocess.call(['make'])
    files = getFileNames()

    testfailed = runtests(files['negatives'], False)
    testfailed += runtests(files['positives'], True)

    if testfailed == 0:
        print "Passed all of the tests, congrats :)"
