#   NOTE: This file takes in one command line argument: 
#   the number of games you would would like it to run.
#   For example, to run 5 games, use 'python run.py 5'

from multiprocessing import Pool
from random import randint
import sys
import os

NETWORK_WIN = 0
NETWORK_LOSS = 1
NETWORK_ERROR = 2


def simulate(i):
    print "---- Running iteration %s ----" % str(i)
    output = os.popen('java Network -q machine random').read()
    if "MachinePlayer returned a null move, quitting." in output:
        return NETWORK_ERROR
    if ">>>> MachinePlayer <<<< WINS!" in output:
        return NETWORK_WIN
    if ">>>> RandomPlayer <<<< WINS!" in output:
        return NETWORK_LOSS
    # run simulation here
    return NETWORK_ERROR

# Start thread pool
pool = Pool(processes=4)

# Get num iterations
num_simulations = int(sys.argv[1])

# For gathering results
async_results = []

# Queue up asynchronous tasks
for i in range(num_simulations):
    async_results.append(
        pool.apply_async(simulate, (i,))
    )

def show_results():
    wins, losses, errors = 0, 0, 0
    # Fetch results
    num_results = len(async_results)
    for i in range(num_results):
        result = async_results.pop(0).get()
        if result == NETWORK_WIN:
            wins += 1
        elif result == NETWORK_LOSS:
            losses += 1
        else:
            errors += 1
    print "TOTAL: %s, Wins: %s, Losses: %s, Errors: %s" % tuple(str(i) for i in [sys.argv[1], wins, losses, errors])

show_results()