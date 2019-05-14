#include <bits/stdc++.h>
using namespace std;

double data[1000][100];
double mns[100][100];
vector<int> clu[100];
int n, m;

double dist(int a, int b)
{
	double d=0.0;
	for(int j=0; j<m; j++)
	{
		d+=((data[a][j]-mns[b][j])*(data[a][j]-mns[b][j]));
	}
	d=sqrt(d);
	return d;
}

void take_input()
{
	freopen("Absenteeism_at_work.csv", "r", stdin);
	string line;
	int x=0, y=0;
	m=0;
	int t=0;
	while(getline(cin, line))
	{
		if(t==0) {t=1; continue;}
		//cout<<line<<endl;
		string tmp=""; y=0;
		for(int i=0; i<line.size(); i++)
		{
			if(line[i]!=',') tmp+=line[i];
			else{
				data[x][y]=stof(tmp);
				tmp=""; y++;
			}
		}
		m=max(y+1, m);
		data[x][y]=stof(tmp);
		x++;
	}
	n=x;
}

int main()
{
	take_input();
	
	int k=3;
	for(int i=0; i<n; i++)
	{
		clu[i%k].push_back(i);
	}

	//for(int i=0; i<k; i++) cout<<clu[i].size()<<endl;

	for(int i=0; i<k; i++)
	{
		double sm[m+1];
		for(int j=0; j<m; j++) sm[j]=0.0;
		for(int j=0; j<clu[i].size(); j++)
		{
			int p=clu[i][j];
			for(int h=0; h<m; h++)
			{
				sm[h]+=data[p][h];
			}
		}
		for(int j=0; j<m; j++)
		{
			mns[i][j]=sm[j]/clu[i].size();
		}
	}

	int z=1;
	while(z<100)
	{
		for(int i=0; i<k; i++) clu[i].clear();
		for(int i=0; i<n; i++)
		{
			doupip install --upgrade setuptoolsble dis=10000000000000000.0;
			int in;
			for(int j=0; j<k; j++)
			{
				double d=dist(i, j);
				//cout<<d<<endl;
				if(d<dis)
				{
					in=j;
					dis=d;
				}
			}
			clu[in].push_back(i);
		}
		//printf("%d\n\n", z);
		//for(int i=0; i<k; i++) cout<<clu[i].size()<<endl;

		for(int i=0; i<k; i++)
		{
			double sm[m+1];
			for(int j=0; j<m; j++) sm[j]=0.0;
			for(int j=0; j<clu[i].size(); j++)
			{
				int p=clu[i][j];
				for(int h=0; h<m; h++)
				{
					sm[h]+=data[p][h];
				}
			}

			for(int j=0; j<m; j++)
			{
				mns[i][j]=sm[j]/clu[i].size();
			}
		}

		z++;
	}

	freopen("output_n1", "w", stdout);
	for(int i=0; i<k; i++)
	{
		cout<<clu[i].size()<<endl;
		for(int j=0; j<clu[i].size(); j++)
		{
			cout<<clu[i][j]<<" ";
		}
		printf("\n");
	}
}