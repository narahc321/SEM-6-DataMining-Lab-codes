#!/usr/bin/env python
# coding: utf-8

# In[1]:


get_ipython().run_line_magic('matplotlib', 'inline')
import matplotlib.pyplot as plt
import numpy as np
import seaborn as sns
import scipy as sp
from sklearn import datasets
from numpy import linalg as LA
from sklearn.metrics.pairwise import pairwise_distances
import sys


# In[2]:


#Our Dataset
data = np.array([1.0,1.0,2.0,2.0,3.0,3.0,4.0,4.0,5.0,5.0,6.0,6.0]).reshape(6,2)
print(data)


# Visualising Data

# In[3]:


fig = plt.figure()
fig.suptitle('Scatter Plot for clusters')
ax = fig.add_subplot(1,1,1)
ax.set_xlabel('X')
ax.set_ylabel('Y')
ax.scatter(data[:,0],data[:,1])


#   Distance Matrix: It is the matrix which contains distances between all the datapoints. If you have n datapoints, the distance matrix will be of order nXn.
#   To calculate distance matrix, we use pairwise distance function of sklearn which returns distance matrix. For hierarchical clustering we use euclidean distance to calculate distance between two datapoints.

# In[4]:


def hierarchical_clustering(data,linkage,no_of_clusters):  
    #first step is to calculate the initial distance matrix
    #it consists distances from all the point to all the point
    color = ['r','g','b','y','c','m','k','w']
#     initial_distances = pairwise_distances(data,metric='euclidean')
    #making all the diagonal elements infinity 
    
    initial_distances = [0, 0.71, 5.66, 3.61, 4.24, 3.20, 0.71, 0, 4.95, 2.92, 3.54, 2.50, 5.66, 4.95, 0, 2.24, 1.41, 2.50, 3.61, 2.92, 2.24, 0, 1.0, 0.5, 4.24, 3.54, 1.41, 1.0, 0, 1.12, 3.20, 2.50, 2.50, 0.5, 1.12, 0]
    initial_distances = np.array(initial_distances)
    initial_distances = np.reshape(initial_distances, (6,6))
    np.fill_diagonal(initial_distances,sys.maxsize)
    print(initial_distances)
    clusters = find_clusters(initial_distances,linkage) 
    
    #plotting the clusters
    iteration_number = initial_distances.shape[0] - no_of_clusters
    clusters_to_plot = clusters[iteration_number]
    arr = np.unique(clusters_to_plot)
    
    indices_to_plot = []
    fig = plt.figure()
    fig.suptitle('Scatter Plot for clusters')
    ax = fig.add_subplot(1,1,1)
    ax.set_xlabel('X')
    ax.set_ylabel('Y')
    for x in np.nditer(arr):
        indices_to_plot.append(np.where(clusters_to_plot==x))
    p=0
    
    print(clusters_to_plot)
    for i in range(0,len(indices_to_plot)):
        for j in np.nditer(indices_to_plot[i]):
               ax.scatter(data[j,0],data[j,1], c= color[p])
        p = p + 1
        
    plt.show()
    


# In[5]:


def find_clusters(input,linkage):
    clusters = {}
    row_index = -1
    col_index = -1
    array = []
    

    for n in range(input.shape[0]):
        array.append(n)
        
    clusters[0] = array.copy()

    #finding minimum value from the distance matrix
    #note that this loop will always return minimum value from bottom triangle of matrix
    for k in range(1, input.shape[0]):
        min_val = sys.maxsize
        
        for i in range(0, input.shape[0]):
            for j in range(0, input.shape[1]):
                if(input[i][j]<=min_val):
                    min_val = input[i][j]
                    row_index = i
                    col_index = j
                    
        #once we find the minimum value, we need to update the distance matrix
        #updating the matrix by calculating the new distances from the cluster to all points
        
        #for Single Linkage
        if(linkage == "single" or linkage =="Single"):
            for i in range(0,input.shape[0]):
                if(i != col_index):
                    #we calculate the distance of every data point from newly formed cluster and update the matrix.
                    temp = min(input[col_index][i],input[row_index][i])
                    #we update the matrix symmetrically as our distance matrix should always be symmetric
                    input[col_index][i] = temp
                    input[i][col_index] = temp
        #for Complete Linkage
        elif(linkage=="Complete" or linkage == "complete"):
             for i in range(0,input.shape[0]):
                if(i != col_index and i!=row_index):
                    temp = min(input[col_index][i],input[row_index][i])
                    input[col_index][i] = temp
                    input[i][col_index] = temp
        #for Average Linkage
        elif(linkage=="Average" or linkage == "average"):
             for i in range(0,input.shape[0]):
                if(i != col_index and i!=row_index):
                    temp = (input[col_index][i]+input[row_index][i])/2
                    input[col_index][i] = temp
                    input[i][col_index] = temp
        
        elif(linkage=="Centroid" or linkage =="centroid"):
            for i in range(0,input.shape[0]):
                if(i!=col_index and i!=row_index):
                    dist_centroid = cal_dist_from_centroid(i,row_index,col_index)
                    input[col_index][i] = dist_centroid
                    input[i][col_index] = dist_centroid
                   
        #set the rows and columns for the cluster with higher index i.e. the row index to infinity
        #Set input[row_index][for_all_i] = infinity
        #set input[for_all_i][row_index] = infinity
        for i in range (0,input.shape[0]):
            input[row_index][i] = sys.maxsize
            input[i][row_index] = sys.maxsize
            
        #Manipulating the dictionary to keep track of cluster formation in each step
        #if k=0,then all datapoints are clusters
       
        minimum = min(row_index,col_index)
        maximum = max(row_index,col_index)
        for n in range(len(array)):
            if(array[n]==maximum):
                array[n] = minimum
        clusters[k] = array.copy()
        
    return clusters


# ###### Every datapoint is different cluster initially 

# In[6]:


hierarchical_clustering(data,"average",6)


# ###### First data[5] and data[2] will merge, i.e. point P3 and P6 from the blog

# In[7]:


hierarchical_clustering(data,"average",5)
#you can see that the color of data[2] and data[5] became same, thus they are in same cluster now


# ###### Next merger data[1] and data[4],  i.e. P2 and P5 

# In[8]:


hierarchical_clustering(data,"average",4)
#you can see that the color of data[1] and data[4] also became same in color, thus they are in same cluster now


# ###### Next merger, 2 clusters formed above will merge

# In[9]:


hierarchical_clustering(data,"average",3)


# In[10]:


hierarchical_clustering(data,"average",2)


# ###### Finally all are in one cluster

# In[11]:


hierarchical_clustering(data,"average",1)


# In[ ]:




