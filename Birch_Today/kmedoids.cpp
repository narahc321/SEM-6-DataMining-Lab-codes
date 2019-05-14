#include <bits/stdc++.h>
using namespace std;
double data[1000][21];
double mns[100][21];
vector<int> clu[100];
int med[100];
double dist(int a,int b)
{
	int i;
	double d =0.0;
	for(i=0;i<21;i++)
	{
		d+=((data[a][i]-data[b][i])*(data[a][i]-data[b][i]));
	}
	d=sqrt(d);
	return d;
}
int newi(int t)
{
	int i,j,c=0;
	double mn=1000000000000000.0;
	int rt;
	for(i=0;i<clu[t].size();i++)
	{
		double d=0.0;
		for(j=0;j<clu[t].size();j++)
		{
			d+=dist(clu[t][i],clu[t][j]);
		}
		if(d<mn)
		{
			mn=d;
			rt=i;
		}
	}
	return rt;
}
int main()
{
	freopen("Absenteeism_at_work.csv","r",stdin);
	freopen("output1.txt","w",stdout);
	int t=0;
	int i,j;
	med[0]=0;
	med[1]=370;
	string line;
	while(getline(cin,line))
	{
		//cout<<line<<endl;
		if(t==0)
		{
			t=1;
			continue;
		}
		int n=0;
		string tmp="";
		j=0;
		//cout<<line<<endl;
		for(i=0;i<line.size();i++)
		{
			if(line[i]==',')
			{
				//tmp=tmp+'\n';
				data[t-1][j]=stof(tmp);
				//cout<<tmp<<" ";
				tmp="";
				j++;
			}
			else
			{
				tmp=tmp+line[i];
			}
		}
		data[t-1][j]=stof(tmp);
		//cout<<tmp<<endl;
		t++;
		//cout<<endl;
		//break;
	}
	int k=2,h;
	for(i=0;i<740;i++)
	{
		int into=0;
		double dis=10000000000000000.0;
		for(j=0;j<k;j++)
		{
			if(dist(i,med[j])<dis)
			{
				dis=dist(i,med[j]);
				into=j;
			}
		}
		clu[into].push_back(i);
		//cout<<i<<" "<<into<<endl;
	}
	
	for(i=0;i<100;i++)
	{
		for(j=0;j<k;j++)
		{
			med[j]=newi(j);
		}
		for(j=0;j<100;j++)
		{
			clu[j].clear();
		}
		for(j=0;j<740;j++)
		{
			double dis=10000000000000000.0;
			int in;
			for(h=0;h<k;h++)
			{
				if(dist(j,med[h])<dis)
				{
					dis=dist(j,med[h]);
					in=h;
				}
			}
			clu[in].push_back(j);
		}
	}
	for(i=0;i<k;i++)
	{
		cout<<clu[i].size();
		cout<<endl;
		for(j=0;j<clu[i].size();j++)
		{
			cout<<clu[i][j]<<" ";
		}
		cout<<endl;
	}
	//cout<<med[0]<<endl;
	//cout<<dist(0,0)<<endl;
	//cout<<dist(0,1)<<endl;
	//cout<<dist(0,2)<<endl;
	return 0;
}