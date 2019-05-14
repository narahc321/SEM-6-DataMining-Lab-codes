#include <bits/stdc++.h>
using namespace std;

double a[100][100];
int n;

void take_input()
{
	freopen("data.csv", "r", stdin);
	string line;
	int x=0, y=0;
	while(getline(cin, line))
	{
		string tmp=""; y=0;
		for(int i=0; i<line.size(); i++)
		{
			if(line[i]!=',')
			{
				tmp+=line[i];
			}
			else{
				a[x][y]=stof(tmp);
				tmp="";
				y++;
			}
		}
		a[x][y]=stof(tmp);
		tmp="";
		x++;
	}
	n=x;
}

int main()
{
	take_input();

	map<int, string> m;
	vector<pair<string, string>> v;

	for(int i=0; i<n; i++)
	{
		char x='A'+i;
		m[i]=x;
	}

	int z=1;
	while(z<n)
	{
		double minn=a[0][1];
		int n1=-1, n2=-1;
		for(int i=0; i<n; i++)
		{
			for(int j=i+1; j<n; j++)
			{
				if(a[i][j]<=minn)
				{
					n1=i; n2=j;
					minn=a[i][j];
				}
			}
		}

		for(int j=0; j<n; j++)
		{
			if(j!=n1)
			{
				a[n1][j] = (a[n1][j] + a[n2][j])/2.0;
				a[j][n1] = a[n1][j];
			}
		}

		for(int j=0; j<n; j++)
		{
			if(j!=n2)
			{
				a[n2][j]=50.0;
				a[j][n2]=50.0;
			}
		}

		cout<<"ITR - "<<z<<" -> "<<m[n1]<<"--"<<m[n2]<<endl;
		v.push_back({m[n1], m[n2]});
		m[n1]+=m[n2]; m[n2]=m[n1];
		z++;
	}

	for(int i=0; i<n; i++)
	{
		char x='A'+i;
		cout<<x<<" ";
	}printf("\n");

	for(int i=0; i<v.size(); i++)
	{
		pair<string, string> p;
		p=v[i];
		char s1=p.first[0];
		char s2=p.second[0];
		int x=s1-'A';
		int y=s2-'A';

		for(int i=0; i<x; i++) printf("  ");
		printf("| ");
		for(int i=x+1; i<y; i++) printf("  ");
		printf("| ");
		printf("\n");

		for(int i=0; i<x; i++) printf("  ");
		for(int i=x; i<=y; i++) printf("--");
		printf("\n");
	}
}
