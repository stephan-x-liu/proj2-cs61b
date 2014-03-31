#!/usr/bin/python

#	NOTE: This file takes in one command line argument--the number of
#	games you would would like it to run. For example, to run 5 games,
#	use 'python run_games.py 5'

import os
import sys

wins = 0
losses = 0
nulls = 0
for _ in range(eval(sys.argv[1])):
	output = os.popen('java Network -q machine random').read()
	if "MachinePlayer returned a null move, quitting." in output:
		nulls += 1
	if ">>>> MachinePlayer <<<< WINS!" in output:
		wins += 1
	if ">>>> RandomPlayer <<<< WINS!" in output:
		losses += 1

print("Out of %(total)s games, MachinePlayer won %(w)s times and lost %(l)s times. And, it returned a null move %(n)s times." % {'total': sys.argv[1], 'w': str(wins), 'l': str(losses), 'n': str(nulls)})
