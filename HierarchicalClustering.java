import java.util.*;
class cluster
{
	ArrayList<Integer> ele=new ArrayList<>();
	public String toString()
	{
		return ele.toString();
	}
}
class HC
{
	double distMat[][];
	ArrayList<ArrayList<cluster>> cl=new ArrayList<>();
	HC(double arr[][])
	{
		distMat=arr;
		ArrayList<cluster> tmp=new ArrayList<>();
		for(int i=0;i<arr.length;i++)
		{
			cluster x=new cluster();
			x.ele.add(i);
			tmp.add(x);
		}
		cl.add(tmp);
	}
	double distance(cluster c1,cluster c2)
	{
		double d=100000000;
		for(int i=0;i<c1.ele.size();i++)
		{
			for(int j=0;j<c2.ele.size();j++)
			{
				d=Math.min(distMat[c1.ele.get(i)][c2.ele.get(j)],d);
			}
		}
		return d;
	} 
	void createClusters()
	{
		ArrayList<cluster> al=cl.get(cl.size()-1);
		while(al.size()>1)
		{
			double d=100000000;
			for(int i=0;i<al.size();i++)
			{
				for(int j=i+1;j<al.size();j++)
				{
					d=Math.min(distance(al.get(i),al.get(j)),d);
				}
			}
			for(int i=0;i<al.size();i++)
			{
				int flg=0;
				for(int j=i+1;j<al.size();j++)
				{
					if(distance(al.get(i),al.get(j))==d)
					{
						flg=1;
						ArrayList<cluster> al1=new ArrayList<>();
						for(int i1=0;i1<al.size();i1++)
							if(i1!=i&&i1!=j)
								al1.add(al.get(i1));
						cluster c=new cluster();
						c.ele.addAll(al.get(i).ele);
						c.ele.addAll(al.get(j).ele);
						al1.add(c);
						cl.add(al1);
						break;
					}
				}
				if(flg==1)
					break;
			}
			al=cl.get(cl.size()-1);
		}
	}
	void print()
	{
		for(int i=0;i<cl.size();i++)
		{
			System.out.println(cl.get(i));
		}
	}
}
class solution
{
	public static void main(String []args)
	{
		Scanner sc=new Scanner(System.in);
		int n=sc.nextInt(),m=sc.nextInt();
		double arr[][]=new double[n][m];
		for(int i=0;i<n;i++)
			for(int j=0;j<m;j++)
				arr[i][j]=sc.nextDouble();
		HC ng=new HC(arr);
		ng.createClusters();
		ng.print();
	}
}
