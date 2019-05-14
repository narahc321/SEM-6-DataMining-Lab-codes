#include <bits/stdc++.h>
#include <string>
using namespace std;
#define M 2

vector<double> a;
int n;

void takeInput()
{
	freopen("birchdata1.csv", "r", stdin);
	string line;
	int x=0;
	while(getline(cin, line))
	{
		a.push_back(stof(line));
		x++;
	}
	n=x;
}

struct Node {

	int type;
	int size;
	double cf[M][3];
	Node *child[M];
	Node *par;

	vector<double> lf;
};

Node *createLeaf(double x, Node *p)
{
	Node *tmp = new Node;
	tmp->type=0;
	tmp->size=1;
	for(int i=0; i<M; i++) tmp->child[i]=NULL;

	tmp->lf.push_back(x);

	tmp->par=p;
	return tmp;
}

Node *createNode(double x)
{
	Node *tmp = new Node;
	tmp->type=1;
	tmp->size=1;
	for(int i=0; i<M; i++) tmp->child[i]=NULL;

	tmp->cf[0][0]=1.0;
	tmp->cf[0][1]=x;
	tmp->cf[0][2]=x*x;

	tmp->child[0] = createLeaf(x, tmp);
	return tmp;
}

Node *duplicateNode(Node *p)
{
	Node * tmp = new Node;
	tmp->type=p->type;
	tmp->size=p->size;
	for(int i=0; i<M; i++) tmp->child[i]=NULL;

	for(int i=0; i<p->size; i++)
	{
		tmp->cf[i][0] = p->cf[i][0];
		tmp->cf[i][1] = p->cf[i][1];
		tmp->cf[i][2] = p->cf[i][2];

		tmp->child[i] = p->child[i];
	}

	return tmp;
}

void modifyNode(Node *p)
{
	for(int i=1; i<p->size; i++)
	{
		p->cf[0][0] += p->cf[i][0];
		p->cf[0][1] += p->cf[i][1];
		p->cf[0][2] += p->cf[i][2];
	}

	p->size = 2;

	for(int i=0; i<M; i++) p->child[i]=NULL;
}

void updateCf(Node *p, Node *pp)
{
	if(pp==NULL) return;

	int in;
	for(int i=0; i<pp->size; i++)
	{
		if(pp->child[i]==p)
		{
			in=i; break;
		}
	}

	double n1 = 0.0;
	double ls1 = 0.0;
	double ss1 = 0.0;

	for(int i=0; i<p->size; i++)
	{
		n1 += p->cf[i][0];
		ls1 += p->cf[i][1];
		ss1 += p->cf[i][2];
	}

	pp->cf[in][0] = n1;
	pp->cf[in][1] = ls1;
	pp->cf[in][2] = ss1;

	updateCf(pp, pp->par);
}

void traverse(Node *pp)
{
	queue<Node*> q;
	q.push(pp);

	int k1=0, k2=0;

	while(!q.empty())
	{
		Node *p=q.front(); q.pop();
		if(p->type==1)
		{
			printf("Node - %d\n", k1); k1++;
			for(int i=0; i<p->size; i++)
			{
				cout<<"CF -> "<<p->cf[i][0]<<" "<<p->cf[i][1]<<" "<<p->cf[i][2]<<endl;
			}
			printf("\n");
		}
		else{
			printf("Leaf - %d\n", k2); k2++;
			printf("LF -> ");
			for(int i=0; i<p->size; i++)
			{
				cout<<p->lf[i]<<" ";
			}printf("\n\n");
		}
		for(int i=0; i<p->size; i++)
		{
			if(p->child[i]) q.push(p->child[i]);
		}
	}
	printf("\n\n");
}

int main()
{	
	takeInput();

	double t=0.15;

	Node *root;
	root = createNode(a[0]);
	root->par = NULL;

	traverse(root);

	for(int i=1; i<n; i++)
	{

		double x=a[i];
		Node *tmp, *tmp2;
		tmp=root;

		int in;

		while(tmp->type==1)
		{
			double minn=99999999.0;
			for(int j=0; j<tmp->size; j++)
			{
				double n1 = tmp->cf[j][0];
				double ls1 = tmp->cf[j][1];
				if(abs(x-(ls1/n1)) < minn)
				{
					in = j;
					minn = abs(x-(ls1/n1));
				}
			}
			tmp2=tmp;
			tmp=tmp->child[in];
		}


		double n1 = tmp2->cf[in][0] + 1;
		double ls1 = tmp2->cf[in][1] + x;
		double ss1 = tmp2->cf[in][2] + x*x;

		double r = sqrt(((ss1) - ((ls1*ls1)/n1))/n1);
		//cout<<"r = "<<r<<endl;

		if(r<=t)
		{
			tmp->lf.push_back(x);
			tmp->size += 1;

			tmp2->cf[in][0] = n1;
			tmp2->cf[in][1] = ls1;
			tmp2->cf[in][2] = ss1;

			//updateCf();

		} else {

			if(tmp2->size < M)
			{

				int idx=tmp2->size;

				tmp2->cf[idx][0] = 1.0;
				tmp2->cf[idx][1] = x;
				tmp2->cf[idx][2] = x*x;

				tmp2->child[idx] = createLeaf(x, tmp2);

				tmp2->size += 1;

				//updateCf();
			} else {

				Node *tmp3 = duplicateNode(tmp2);
				Node *tmp4 = createNode(x);

				modifyNode(tmp2);

				tmp2->child[0] = tmp3;
				tmp2->child[1] = tmp4;

				tmp3->par=tmp2;
				tmp4->par=tmp2;

				tmp2->cf[1][0] = tmp4->cf[0][0];
				tmp2->cf[1][1] = tmp4->cf[0][1];
				tmp2->cf[1][2] = tmp4->cf[0][2];

				tmp2->size = 2;

				//updateCf();
			}
		}

		updateCf(tmp2, tmp2->par);

		traverse(root);
	}
}