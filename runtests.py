import subprocess
import os
import sys

rootDirectory, filename = os.path.split(os.path.abspath(__file__))
progressbar = False
BLACK, RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN, WHITE = range(8)

def getFileNames():
    files = [val for sublist in [[os.path.join(i[0], j) for j in i[2]] for i in os.walk(rootDirectory + '/tests')] for val in sublist]
    negatives = []
    positives = []
    for file in files:
        #dont run backup files created by vim
        if file[-1] != '~':
            if "n-" in file:
                negatives.append(file)
            elif "p-" in file:
                positives.append(file)
    return {'negatives': negatives, 'positives': positives}


def printProgressBar(percentage):
    percentageh = int(percentage/2)
    spaces = ' ' * (50 - percentageh)
    if(percentageh < 17):
        sys.stdout.write('\rPercent: [' + '\x1b[1;%dm' % (30+RED) + '{0}{1}'.format('#'*percentageh, spaces) + '\x1b[1;%dm' % (30+WHITE) +'] {0}%'.format(percentage))
    elif(percentageh < 34):
        string = '\rPercent: [' + '\x1b[1;%dm' % (30+RED) + '{0}'.format('#'*17)
        string += '\x1b[1;%dm' % (30+YELLOW) + '{0}{1}'.format('#'*(percentageh - 17), spaces)
        string += '\x1b[1;%dm' % (30+WHITE) + '] {0}%'.format(percentage)
        sys.stdout.write('{0}'.format(string))
    else:
        string = '\rPercent: [' + '\x1b[1;%dm' % (30+RED) + '{0}'.format('#'*17)
        string += '\x1b[1;%dm' % (30+YELLOW) + '{0}'.format('#'*17)
        string += '\x1b[1;%dm' % (30+GREEN) + '{0}{1}'.format('#'*(percentageh - 34), spaces)
        string += '\x1b[1;%dm' % (30+WHITE) + '] {0}%'.format(percentage)
        sys.stdout.write('{0}'.format(string))
    sys.stdout.flush()

def otherProgressBar(errorstring, passed):
    if passed:
        errorstring += '\x1b[1;%dm' % (30+GREEN) + '#'
    else:
        errorstring += '\x1b[1;%dm' % (30+RED) + '#'

    sys.stdout.write(errorstring)
    sys.stdout.flush()

def runtests(files, positive):
    testfailed = []
    errorstring = ''
    if(positive):
        print("Testing positive files")
    else:
        print("Testing negative files")

    string = rootDirectory + '/bin/:lib/java-cup-11b-runtime.jar'
    progress = 1.0
    total = len(files)

    for file in files:
        passed = True
        percentage = int((progress/total)*100.0)
        p = subprocess.run(['java', '-cp', string, 'SC', file], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        err, out = (str(p.stderr), str(p.stdout))
        if positive:
            if "error" in err and "parsing successful" not in out:
                testfailed.append((file, err[2:-3]))
                passed = False
        else:
            if "error" not in err:
                testfailed.append((file, ''))
                passed = False
        progress+=1
        if progressbar:
            printProgressBar(percentage)
        else:
            otherProgressBar(errorstring, passed)
    sys.stdout.write('\x1b[1;%dm' % (30+WHITE) + '\n' )
    return testfailed

def printFailedTests(testfailed, amountOfTests):
    string = rootDirectory + '/bin/:lib/java-cup-11b-runtime.jar'
    for file in testfailed:
        sys.stdout.write('\x1b[1;%dm' % (30+RED) + "Test failed :(\n" + '\x1b[1;%dm' % (30+WHITE))
        print(file[0][len(rootDirectory):] + ' ' + file[1])
        print("Run the test again: java -cp " + string + " SC " + file[0])

    if len(testfailed) == 0:
        sys.stdout.write('\x1b[1;%dm' % (30+GREEN) + "Passed all of the tests, congrats :)\n")
    else:
        sys.stdout.write('\x1b[1;%dm' % (30+RED) + "Failed {0}/{1} tests\n".format(len(testfailed), amountOfTests))

if __name__ == '__main__':
    sys.stdout.write('\x1b[1;%dm' % (30+WHITE))
    print("This might take a while...")

    #subrocess.call() is blocking, need to wait until make finishes executing
    subprocess.call(['make', '-C', rootDirectory])
    files = getFileNames()

    testfailed = runtests(files['negatives'], False)
    testfailed.extend(runtests(files['positives'], True))

    printFailedTests(testfailed, len(files['negatives']) + len(files['positives']))
