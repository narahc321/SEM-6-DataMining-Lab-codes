#include <bits/stdc++.h>
using namespace std;
#define M 2

vector<pair<double, double>> a;
int n;

void takeInput()
{
	freopen("data2.csv", "r", stdin);
	string line;
	int x=0;
	while(getline(cin, line))
	{
		string tmp="";
		double d1=0.0, d2=0.0;
		for(int i=0; i<line.size(); i++)
		{
			if(line[i]!=',') tmp += line[i];
			else{
				d1=stof(tmp);
				tmp="";
			}
		}
		d2=stof(tmp);
		a.push_back({d1, d2});
		x++;
	}
	n=x;
}

struct Node {

	int type;
	int size;
	double cf[M][3][2];
	Node *child[M];
	Node *par;

	vector<pair<double, double>> lf;
};

Node *createLeaf(pair<double, double> x, Node *p)
{
	Node *tmp = new Node;
	tmp->type=0;
	tmp->size=1;
	for(int i=0; i<M; i++) tmp->child[i]=NULL;

	tmp->lf.push_back(x);

	tmp->par=p;
	return tmp;
}

Node *createNode(pair<double, double> x)
{
	Node *tmp = new Node;
	tmp->type=1;
	tmp->size=1;
	for(int i=0; i<M; i++) tmp->child[i]=NULL;

	tmp->cf[0][0][0]=1.0;

	tmp->cf[0][1][0]=x.first;
	tmp->cf[0][1][1]=x.second;

	tmp->cf[0][2][0]=x.first*x.first;
	tmp->cf[0][2][1]=x.second*x.second;

	tmp->child[0] = createLeaf(x, tmp);
	return tmp;
}

double dist(pair<double, double> x, Node *tmp, int j)
{
	double n1 = tmp->cf[j][j][0];

	double ls1 = tmp->cf[j][1][0];
	double ls2 = tmp->cf[j][1][1];

	double x1 = ls1/n1;
	double x2 = ls2/n1;

	double d = sqrt((x.first - x1)*(x.first - x1) + (x.second - x2)*(x.second - x2));
	return d;
}

Node *duplicateNode(Node *p)
{
	Node * tmp = new Node;
	tmp->type=p->type;
	tmp->size=p->size;
	for(int i=0; i<M; i++) tmp->child[i]=NULL;

	for(int i=0; i<p->size; i++)
	{
		tmp->cf[i][0][0] = p->cf[i][0][0];
		tmp->cf[i][1][0] = p->cf[i][1][0];
		tmp->cf[i][1][1] = p->cf[i][1][1];
		tmp->cf[i][2][0] = p->cf[i][2][0];
		tmp->cf[i][2][1] = p->cf[i][2][1];

		tmp->child[i] = p->child[i];
	}

	return tmp;
}

void modifyNode(Node *p)
{
	for(int i=1; i<p->size; i++)
	{
		p->cf[0][0][0] += p->cf[i][0][0];
		p->cf[0][1][0] += p->cf[i][1][0];
		p->cf[0][1][1] += p->cf[i][1][1];
		p->cf[0][2][0] += p->cf[i][2][0];
		p->cf[0][2][1] += p->cf[i][2][1];
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
	double ls2 = 0.0;
	double ss1 = 0.0;
	double ss2 = 0.0;

	for(int i=0; i<p->size; i++)
	{
		n1 += p->cf[i][0][0];
		ls1 += p->cf[i][1][0];
		ls2 += p->cf[i][1][1];
		ss1 += p->cf[i][2][0];
		ss2 += p->cf[i][2][1];
	}

	pp->cf[in][0][0] = n1;
	pp->cf[in][1][0] = ls1;
	pp->cf[in][1][1] = ls2;
	pp->cf[in][2][0] = ss1;
	pp->cf[in][2][1] = ss1;

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
				cout<<"CF -> ("<<p->cf[i][0][0]<<") ("<<p->cf[i][1][0]<<","<<p->cf[i][1][1]<<") ("
				<<p->cf[i][2][0]<<","<<p->cf[i][2][1]<<")"<<endl;
			}
			printf("\n");
		}
		else{
			printf("Leaf - %d\n", k2); k2++;
			printf("LF -> ");
			for(int i=0; i<p->size; i++)
			{
				cout<<" ("<<p->lf[i].first<<", "<<p->lf[i].second<<") ";
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

	double t=2.55;

	Node *root;
	root = createNode(a[0]);
	root->par = NULL;

	traverse(root);

	for(int i=1; i<n; i++)
	{

		pair<double, double> x=a[i];
		Node *tmp, *tmp2;
		tmp=root;

		int in;

		while(tmp->type==1)
		{
			double minn=99999999.0;
			for(int j=0; j<tmp->size; j++)
			{
				double d = dist(x, tmp, j);

				if(d < minn)
				{
					in = j;
					minn = d;
				}
			}
			tmp2=tmp;
			tmp=tmp->child[in];
		}


		double n1 = tmp2->cf[in][0][0] + 1.0;

		double ls1 = tmp2->cf[in][1][0] + x.first;
		double ls2 = tmp2->cf[in][1][1] + x.second;

		double ss1 = tmp2->cf[in][2][0] + x.first*x.first;
		double ss2 = tmp2->cf[in][2][1] + x.second*x.second;

		double r1 = sqrt(abs(((ss1) - ((ls1*ls1)/n1))/n1));
		double r2 = sqrt(abs(((ss2) - ((ls2*ls2)/n1))/n1));

		double r = r1 + r2;

		cout<<"r = "<<r<<endl;

		if(r<=t)
		{
			tmp->lf.push_back(x);
			tmp->size += 1;

			tmp2->cf[in][0][1] = n1;

			tmp2->cf[in][1][0] = ls1;
			tmp2->cf[in][1][1] = ls2;

			tmp2->cf[in][2][0] = ss1;
			tmp2->cf[in][2][1] = ss2;

			//updateCf();

		} else {

			if(tmp2->size < M)
			{

				int idx=tmp2->size;

				tmp2->cf[idx][0][0] = 1.0;

				tmp2->cf[idx][1][0] = x.first;
				tmp2->cf[idx][1][1] = x.second;

				tmp2->cf[idx][2][0] = x.first*x.first;
				tmp2->cf[idx][2][1] = x.second*x.second;

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

				tmp2->cf[1][0][0] = tmp4->cf[0][0][0];
				tmp2->cf[1][1][0] = tmp4->cf[0][1][0];
				tmp2->cf[1][1][1] = tmp4->cf[0][1][1];
				tmp2->cf[1][2][0] = tmp4->cf[0][2][0];
				tmp2->cf[1][2][1] = tmp4->cf[0][2][1];

				tmp2->size = 2;

				//updateCf();
			}
		}

		updateCf(tmp2, tmp2->par);

		traverse(root);

	}

}