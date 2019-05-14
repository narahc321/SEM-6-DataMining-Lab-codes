
# coding: utf-8

# In[1]:

import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import sklearn
# from sklearn.model_selection import train_test_split
from sklearn.cross_validation import train_test_split
np.random.seed(6)
import math


# In[2]:

from sklearn.datasets.samples_generator import make_blobs

#we need to add 1 to X values (we can say its bias)

data = pd.read_excel("data.xlsx")
data = np.asarray(data)
# print(data.shape)

X = data[:, 0:2]
y = data[:, 2]

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.33, random_state=42)

X1 = np.c_[np.ones((X_train.shape[0])), X_train]

plt.scatter(X1[:,1],X1[:,2],marker='x',c=y_train)
plt.axis([-1,10,-1, 10])
plt.show()


# In[3]:

X2 = np.c_[np.ones((X_test.shape[0])), X_test]

plt.scatter(X2[:,1],X2[:,2],marker='x',c=y_test)
plt.axis([-1,10,-1, 10])
plt.show()


# In[4]:

postiveX=[]
negativeX=[]
for i,v in enumerate(y_train):
    if v==-1:
        negativeX.append(X_train[i])
    else:
        postiveX.append(X_train[i])

#our data dictionary
data_dict = {-1:np.array(negativeX), 1:np.array(postiveX)}


# In[5]:

#all the required variables 
w=[] #weights 2 dimensional vector
b=[] #bias

max_feature_value=float('-inf')
min_feature_value=float('+inf')
        
for yi in data_dict:
    if np.amax(data_dict[yi])>max_feature_value:
        max_feature_value=np.amax(data_dict[yi])
                
    if np.amin(data_dict[yi])<min_feature_value:
        min_feature_value=np.amin(data_dict[yi])
        
#print(min_feature_value)        
        
learning_rate = [max_feature_value * 0.1, max_feature_value * 0.01, max_feature_value * 0.001,]


# In[6]:

def SVM_Training(data_dict):
    i=1
    global w
    global b
    # { ||w||: [w,b] }
    length_Wvector = {}
    transforms = [[1,1],[-1,1],[-1,-1],[1,-1]]
    
    b_step_size = 2
    b_multiple = 5
    w_optimum = max_feature_value*0.5

    for lrate in learning_rate:
        
        w = np.array([w_optimum,w_optimum])     
        optimized = False
        while not optimized:
            #b=[-maxvalue to maxvalue] we wanna maximize the b values so check for every b value
            for b in np.arange(-1*(max_feature_value*b_step_size), max_feature_value*b_step_size, lrate*b_multiple):
                for transformation in transforms:  # transforms = [[1,1],[-1,1],[-1,-1],[1,-1]]
                    w_t = w*transformation
                    
                    correctly_classified = True
                    
                    # every data point should be correct
                    for yi in data_dict:
                        for xi in data_dict[yi]:
                            if yi*(np.dot(w_t,xi)+b) < 1:  # we want  yi*(np.dot(w_t,xi)+b) >= 1 for correct classification
                                correctly_classified = False
                                
                    if correctly_classified:
                        length_Wvector[np.linalg.norm(w_t)] = [w_t,b] #store w, b for minimum magnitude
            
            if w[0] < 0:
                optimized = True
            else:
                w = w - lrate

        norms = sorted([n for n in length_Wvector])
        
        minimum_wlength = length_Wvector[norms[0]]
        w = minimum_wlength[0]
        b = minimum_wlength[1]
        
        w_optimum = w[0]+lrate*2


# In[7]:

SVM_Training(data_dict)


# In[8]:

colors = {1:'g',-1:'y'}
fig = plt.figure()
ax = fig.add_subplot(1,1,1)


# In[9]:

def visualize(data_dict):
       
        
        #[[ax.scatter(x[0],x[1],s=100,color=colors[i]) for x in data_dict[i]] for i in data_dict]
        
        plt.scatter(X1[:,1],X1[:,2],marker='x',c=y_train)

        # hyperplane = x.w+b
        # v = x.w+b
        # psv = 1
        # nsv = -1
        # dec = 0
        def hyperplane_value(x,w,b,v):
            return (-w[0]*x-b+v) / w[1]

        datarange = (min_feature_value*0.9,max_feature_value*1.)
        hyp_x_min = datarange[0]
        hyp_x_max = datarange[1]

        # (w.x+b) = 1
        # positive support vector hyperplane
        psv1 = hyperplane_value(hyp_x_min, w, b, 1)
        psv2 = hyperplane_value(hyp_x_max, w, b, 1)
        ax.plot([hyp_x_min,hyp_x_max],[psv1,psv2], 'y--')

        # (w.x+b) = -1
        # negative support vector hyperplane
        nsv1 = hyperplane_value(hyp_x_min, w, b, -1)
        nsv2 = hyperplane_value(hyp_x_max, w, b, -1)
        ax.plot([hyp_x_min,hyp_x_max],[nsv1,nsv2], 'y--')

        # (w.x+b) = 0
        # positive support vector hyperplane
        db1 = hyperplane_value(hyp_x_min, w, b, 0)
        db2 = hyperplane_value(hyp_x_max, w, b, 0)
        ax.plot([hyp_x_min,hyp_x_max],[db1,db2], 'k')
        
        plt.axis([-1,10,-1,10])
        plt.show()


# In[10]:

visualize(data_dict)


# In[11]:

def predict(features):
        # sign( x.w+b )
        dot_result = np.sign(np.dot(np.array(features),w)+b)
        return dot_result.astype(int)
    
result = []
for i in X_test[:]:
    result.append(predict(i))

count = 0
for i in range(len(result)):
    if(y_test[i]==result[i]):
        count+=1
accuracy = count/len(result)*100
print(accuracy)

