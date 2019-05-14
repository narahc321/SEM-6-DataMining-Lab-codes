import numpy

def MyDBSCAN(D, eps, MinPts):
    labels = [0]*len(D)
    C = 0
    for P in range(0, len(D)):
        if not (labels[P] == 0):
           continue
        NeighborPts = regionQuery(D, P, eps)
        if len(NeighborPts) < MinPts:
            labels[P] = -1
        else: 
           C += 1
           growCluster(D, labels, P, NeighborPts, C, eps, MinPts)
    return labels


def growCluster(D, labels, P, NeighborPts, C, eps, MinPts):
    labels[P] = C
    i = 0
    while i < len(NeighborPts):         
        Pn = NeighborPts[i]
        if labels[Pn] == -1:
           labels[Pn] = C
        elif labels[Pn] == 0:
            labels[Pn] = C
            PnNeighborPts = regionQuery(D, Pn, eps)
            if len(PnNeighborPts) >= MinPts:
                NeighborPts = NeighborPts + PnNeighborPts
        i += 1

def regionQuery(D, P, eps):
    neighbors = []
    for Pn in range(0, len(D)):
        if numpy.linalg.norm(D[P] - D[Pn]) < eps:
           neighbors.append(Pn)
            
    return neighbors


from sklearn.datasets.samples_generator import make_blobs
from sklearn.cluster import DBSCAN
from sklearn.preprocessing import StandardScaler
centers = [[1, 1], [-1, -1], [1, -1]]
X, labels_true = make_blobs(n_samples=750, centers=centers, cluster_std=0.4,
                            random_state=0)

X = StandardScaler().fit_transform(X)
print(X)

print ('Running my implementation...')
my_labels = MyDBSCAN(X, eps=0.3, MinPts=10)

print('Runing scikit-learn implementation...')
db = DBSCAN(eps=0.3, min_samples=10).fit(X)
skl_labels = db.labels_


for i in range(0, len(skl_labels)):
    if not skl_labels[i] == -1:
        skl_labels[i] += 1

num_disagree = 0

for i in range(0, len(skl_labels)):
    print(skl_labels[i])
    if not skl_labels[i] == my_labels[i]:
        print ('Scikit learn:', skl_labels[i], 'mine:', my_labels[i])
        num_disagree += 1

if num_disagree == 0:
    print('PASS - All labels match!')
else:
    print('FAIL -', num_disagree, 'labels don\'t match.')

