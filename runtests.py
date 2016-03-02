import subprocess
import os
import sys

rootDirectoy = os.path.join(os.path.expanduser('~'),'Projects/Compilers')
BLACK, RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN, WHITE = range(8)

def getFileNames():
    print(rootDirectoy + '/tests')
    files = [val for sublist in [[os.path.join(i[0], j) for j in i[2]] for i in os.walk(rootDirectoy + '/tests')] for val in sublist]
    negatives = []
    positives = []
    for file in files:
        if "n-" in file:
            negatives.append(file)
        elif "p-" in file:
            positives.append(file)
    return {'negatives': negatives, 'positives': positives}


def runtests(files, positive):
    testfailed = []
    if(positive):
        print("Testing positive files")
    else:
        print("Testing negative files")

    string = rootDirectoy + '/bin/:lib/java-cup-11b-runtime.jar'
    progress = 1.0
    total = len(files)

    for file in files:
        percentage = int((progress/total)*100.0)
        spaces = ' ' * (100 - percentage)
        p = subprocess.run(['java', '-cp', string, 'SC', file], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        err, out = (p.stderr, p.stdout)
        if err == '':
            if positive:
                if "error" in out:
                    testfailed.append(file)
            else:
                if "error" not in out:
                    testfailed.append(file)
        progress+=1

        if(percentage < 33):
            sys.stdout.write('\rPercent: [' + '\x1b[1;%dm' % (30+RED) + '{0}{1}'.format('#'*percentage, spaces) + '\x1b[1;%dm' % (30+WHITE) +'] {0}%'.format(percentage))
        elif(percentage < 66):
            string = '\rPercent: [' + '\x1b[1;%dm' % (30+RED) + '{0}'.format('#'*33)
            string += '\x1b[1;%dm' % (30+YELLOW) + '{0}{1}'.format('#'*(percentage - 33), spaces)
            string += '\x1b[1;%dm' % (30+WHITE) + '] {0}%'.format(percentage)
            sys.stdout.write('{0}'.format(string))
        else:
            string = '\rPercent: [' + '\x1b[1;%dm' % (30+RED) + '{0}'.format('#'*33)
            string += '\x1b[1;%dm' % (30+YELLOW) + '{0}'.format('#'*33)
            string += '\x1b[1;%dm' % (30+GREEN) + '{0}{1}'.format('#'*(percentage - 66), spaces)
            string += '\x1b[1;%dm' % (30+WHITE) + '] {0}%'.format(percentage)
            sys.stdout.write('{0}'.format(string))
        sys.stdout.flush()
    print(' ')
    return testfailed

def printFailedTests(testfailed):
    string = rootDirectoy + '/bin/:lib/java-cup-11b-runtime.jar'
    for file in testfailed:
        sys.out.write('\x1b[1;%dm' % (30+RED) + "Test failed :(\n" + '\x1b[1;%dm' % (30+WHITE))
        print("Run the test again: java -cp " + string + " SC " + file)

    if len(testfailed) == 0:
        sys.stdout.write('\x1b[1;%dm' % (30+GREEN) + "Passed all of the tests, congrats :)\n")

if __name__ == '__main__':
    sys.stdout.write('\x1b[1;%dm' % (30+WHITE))
    print("This might take a while...")

    #subrocess.call() is blocking, need to wait until make finishes executing
    subprocess.call(['make', '-C', rootDirectoy])
    files = getFileNames()

    testfailed = runtests(files['negatives'], False)
    testfailed += runtests(files['positives'], True)

    printFailedTests(testfailed)
