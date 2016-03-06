import subprocess
import concurrent.futures
import threading
import os
import sys

rootDirectory, filename = os.path.split(os.path.abspath(__file__))
progressbar = False
BLACK, RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN, WHITE = range(8)

lock = threading.RLock()
workers = 10

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


def runtest(file, testfailed, positive):
    string = rootDirectory + '/bin/:lib/java-cup-11b-runtime.jar'
    p = subprocess.run(['java', '-cp', string, 'SC', file], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    err, out = (str(p.stderr), str(p.stdout))
    lock.acquire()
    if positive:
        if "error" in err and "parsing successful" not in out:
            testfailed.append((file, err[2:-3]))
    else:
        if "error" not in err:
            testfailed.append((file, ''))
    lock.release()

def runtests(files, positive):
    testfailed = []
    if(positive):
        print("Testing positive files")
    else:
        print("Testing negative files")

    with concurrent.futures.ThreadPoolExecutor(max_workers=workers) as executor:
        for file in files:
            executor.submit(runtest, file, testfailed, positive)
        executor.shutdown(wait=True)
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

    subprocess.call(['make', '-C', rootDirectory])
    files = getFileNames()

    testfailed = runtests(files['negatives'], False)
    testfailed.extend(runtests(files['positives'], True))

    printFailedTests(testfailed, len(files['negatives']) + len(files['positives']))
