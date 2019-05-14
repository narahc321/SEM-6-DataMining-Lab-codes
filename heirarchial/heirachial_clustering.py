import pandas as pd 
import numpy as np
from math import *
from copy import deepcopy


def single(data):
	dt = deepcopy(data)
	maxi = ceil(np.max(dt)) + 1
	for i in range(dt.shape[0]):
		dt[i][i] = maxi
	steps = []
	for step in range(dt.shape[0]-1):
		index = np.argmin(dt)
		x,y = index//dt.shape[0], index%dt.shape[0]
		steps.append([x,y])
		for i in range(dt.shape[0]):
			if i!=x :
				dt[x][i] = min(dt[x][i], dt[y][i])
				dt[i][x] = dt[x][i]
			dt[y][i] = maxi
			dt[i][y] = maxi
		dt[x][y] = maxi
		print(dt)
	return steps

def complete(data):
	dt = deepcopy(data)
	maxi = ceil(np.max(dt)) + 1
	for i in range(dt.shape[0]):
		dt[i][i] = maxi
	steps = []
	for step in range(dt.shape[0]-1):
		index = np.argmin(dt)
		x,y = index//dt.shape[0], index%dt.shape[0]
		steps.append([x,y])
		for i in range(dt.shape[0]):
			if i!=x :
				dt[x][i] = max(dt[x][i], dt[y][i])
				dt[i][x] = dt[x][i]
			dt[y][i] = maxi
			dt[i][y] = maxi
		dt[x][y] = maxi
	return steps

def average(data):
	dt = deepcopy(data)
	maxi = ceil(np.max(dt)) + 1
	for i in range(dt.shape[0]):
		dt[i][i] = maxi
	size = np.ones(dt.shape[0])
	steps = []
	for step in range(dt.shape[0]-1):
		index = np.argmin(dt)
		x,y = index//dt.shape[0], index%dt.shape[0]
		steps.append([x,y])
		for i in range(dt.shape[0]):
			if i!=x :
				dt[x][i] = ((size[x]*dt[x][i]) + (size[y]*dt[y][i])) / (size[x]+size[y])
				dt[i][x] = dt[x][i]
			dt[y][i] = maxi
			dt[i][y] = maxi
		dt[x][y] = maxi
		size[x] = size[x]+size[y]
	return steps


dt = np.array([	[0, 0.71, 5.66, 3.61, 4.24, 3.20],
		[0.71, 0, 4.95, 2.92, 3.54, 2.50],
		[5.66, 4.95, 0, 2.24, 1.41, 2.50],
		[3.61, 2.92, 2.24, 0, 1.0, 0.5],
		[4.24, 3.54, 1.41, 1.0, 0, 1.12],
		[3.20, 2.50, 2.50, 0.5, 1.12, 0],	])

# dt = pd.read_excel('matrix.xlsx',header=None)
# dt = dt.values
print("Single :",single(dt))
print("Complete :",complete(dt))
print("Average :",average(dt))

