#   NOTE: This file takes in two command line arguments:
#
#   First is the number of games you would would like it to run.
#   For example, to run 5 games, use 'python run.py 5'
#
#   Second is the name of the file you'd like to write the
#   system output to (excluding '.txt') For example, to run
#   5 games and write it to test.txt, use 'python run.py 5 test'
#   
#   If you don't include a second argument, the script will write
#   to 'run_x'.txt, where 'x' is the number of games. For example,
#   'python run.py 5' will write to 'run_5.txt'

from __future__ import print_function
from multiprocessing import Pool
from random import randint
import sys
import os

NETWORK_WIN = 0
NETWORK_LOSS = 1
NETWORK_ERROR = 2


def simulate(i):
    print ("---- Running iteration %s ----" % str(i))
    output = os.popen('java Network -q machine random').read()
    if "MachinePlayer returned a null move, quitting." in output or "Exception" in output:
        return NETWORK_ERROR, output
    if ">>>> MachinePlayer <<<< WINS!" in output:
        return NETWORK_WIN, output
    if ">>>> RandomPlayer <<<< WINS!" in output:
        return NETWORK_LOSS, output
    # run simulation here
    return NETWORK_ERROR, output

# Start thread pool
pool = Pool(processes=16)

# Get num iterations
num_simulations = int(sys.argv[1])

# For gathering results
async_results = []

#File name
filename = None
if len(sys.argv) > 2:
    filename = sys.argv[2]

# Queue up asynchronous tasks
for i in range(num_simulations):
    async_results.append(
        pool.apply_async(simulate, (i,))
    )

def show_results():
    wins, losses, errors = 0, 0, 0
    output_str = ""
    # Fetch results
    num_results = len(async_results)
    for i in range(num_results):
        result = async_results.pop(0).get()
        if result[0] == NETWORK_WIN:
            wins += 1
        elif result[0] == NETWORK_LOSS:
            losses += 1
            output_str+="\n\n\n"+result[1]
        else:
            errors += 1
            output_str+="\n\n\n"+result[1]
    print ("TOTAL: %s, Wins: %s, Losses: %s, Errors: %s" % tuple(str(i) for i in [sys.argv[1], wins, losses, errors]))

    if filename:
        with open(filename+'.txt', 'w') as f:
            f.write(output_str)
            f.close()
    else:
        with open('run_'+str(num_simulations)+'.txt', 'w') as f:
            f.write(output_str)
            f.close()


show_results()
