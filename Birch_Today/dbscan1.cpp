#include <bits/stdc++.h>
using namespace std;

vector<pair<double, double>> data;
int n;

void take_input()
{
	freopen("dbscandata1.csv", "r", stdin);
	int x=0;
	string line;
	while(getline(cin, line))
	{
		string tmp="";
		double d1=0.0, d2=0.0;
		for(int i=0; i<line.size(); i++)
		{
			if(line[i]!=',') tmp+=line[i];
			else{
				d1=stof(tmp);
				tmp="";
			}
		}
		d2=stof(tmp);
		data.push_back({d1, d2});
		x++;
	}
	n=x;
}

double dist(int x, int y)
{
	double d = (data[x].first - data[y].first)*(data[x].first - data[y].first) + 
	(data[x].second - data[y].second)*(data[x].second - data[y].second);
	return sqrt(d);

}

vector<int> checkMinPts(int x, double eps)
{
	vector<int> tmp;
	for(int i=0; i<n; i++)
	{
		if(dist(x, i)<=eps)
		{
			tmp.push_back(i);
		}
	}
	return tmp;
}

int main()
{
	take_input();

	int vis[n+1];
	memset(vis, 0, sizeof(vis));

	int inclus[n+1];
	memset(inclus, 0, sizeof(inclus));

	double eps=sqrt(10);
	int minPts=2;

	vector<int> clus[10];
	int z=0;

	vector<int> noice;

	for(int i=0; i<n; i++)
	{
		if(vis[i]==0)
		{
			vis[i]=1;
			vector<int> tmp = checkMinPts(i, eps);

			if(tmp.size()>=minPts) // If e-neighbourhood of P has minPts
			{
				clus[z].push_back(i);
				inclus[i]=1;

				for(int j=0; j<tmp.size(); j++) // All points P' in e-neighbourood of P
				{
					int p=tmp[j];
					if(vis[p]==0)
					{
						vis[p]=1;
						vector<int> tmp2 = checkMinPts(p, eps);

						if(tmp2.size()>=minPts) // If e-neighbourhood of P' has minPts
						{
							for(int k=0; k<tmp2.size(); k++)
							{
								tmp.push_back(tmp2[k]);
							}
						}
					}
					if(inclus[p] == 0)
					{
						clus[z].push_back(p);
						inclus[p]=1;
					}
				}

				z++;
			} else {
				
				noice.push_back(i);
			}
		}
	}

	for(int i=0; i<z; i++)
	{
		printf("Clus - %d\n", i);
		for(int j=0; j<clus[i].size(); j++)
		{
			cout<<clus[i][j]<<" ";
		}printf("\n\n");
	}
}