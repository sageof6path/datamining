import java.util.*;
import java.io.*;
class Node
{
	String name;
	ArrayList<Node> edges;
	Node(String n)
	{
		name=n;
		edges=new ArrayList<Node>();
	} 
}
class DecisionTree
{
	HashMap<String,ArrayList<String>> data=new HashMap<>();
	HashMap<String,HashSet<String>> catg=new HashMap<>();
	ArrayList<String> attr=new ArrayList<>();
	String op;
	DecisionTree(Scanner sc,int x)
	{
		int flg=0;
		while(sc.hasNext())
		{
			String arr[]=sc.nextLine().split(",");
			if(flg==0)
			{
				flg=1;
				for(int i=0;i<arr.length;i++)
				{
					attr.add(arr[i]);
					data.put(arr[i],new ArrayList<String>());
					catg.put(arr[i],new HashSet<String>());
				}
			}
			else
			{
				for(int i=0;i<arr.length;i++)
				{
					String col=attr.get(i);
					ArrayList<String> tmp=data.get(col);
					tmp.add(arr[i]);
					HashSet<String> hs=catg.get(col);
					hs.add(arr[i]);
					data.put(col,tmp);
					catg.put(col,hs);
				}
			}
		}
		op=attr.get(x);
	}
	DecisionTree(HashMap<String,ArrayList<String>> d,HashMap<String,HashSet<String>> hs,ArrayList<String> al,String op1)
	{
		data=d;
		catg=hs;
		attr=al;
		op=op1;
	}
	boolean check()
	{
		boolean ans=true;
		for(int i=1;i<data.get(op).size();i++)
		{
			if(data.get(op).get(i).equals(data.get(op).get(i-1)))
				continue;
			ans=false;
			break;
		}
		return ans;
	}
	static double entropy(ArrayList<Double> args)
	{
		double sum=0;
		for(double d:args)
			sum+=d;
		double entro=0;
		for(double d:args)
		{
			if(d==0)
				continue;
			entro+=-1*(d/sum)*Math.log(d/sum)/Math.log(2);
		}
		return entro;
	}
	double infoGain(String atr)
	{
		HashMap<String,HashMap<String,Integer>> hmp=new HashMap<>();
		for(String str:catg.get(atr))
		{
			HashMap<String,Integer> tmp=new HashMap<>();
			for(String str2:catg.get(op))
				tmp.put(str2,0);
			hmp.put(str,tmp);
		}
		for(int i=0;i<data.get(atr).size();i++)
		{
			HashMap<String,Integer> tmp=hmp.get(data.get(atr).get(i));
			tmp.put(data.get(op).get(i),1+tmp.get(data.get(op).get(i)));
			hmp.put(data.get(atr).get(i),tmp);
		}
		double sum=0;
		for(String str:hmp.keySet())
		{
			ArrayList<Double> all=new ArrayList<>();
			double x=0,y=0;
			for(String str2:hmp.get(str).keySet())
			{
				all.add((double)hmp.get(str).get(str2));
				x+=(double)hmp.get(str).get(str2);
			}
			y=entropy(all);
			sum+=(x/data.get(op).size())*y;
		}
		//System.out.println(atr+" "+hmp);
		return sum;
	}
	Node build(HashSet<String> used,int n,int lvl)
	{
		if(check())
		{
			//System.out.println(data.get(op).get(0)+" "+lvl);
			return new Node(data.get(op).get(0));
		}
		if(used.size()==n)
		{
			//System.out.println("yes/no "+lvl);
			return new Node("yes/no");
		}
		String answer="";
		double info=2;
		//System.out.println(used);
		for(int i=0;i<attr.size();i++)
		{
			if(used.contains(attr.get(i)))
				continue;
			double tmp=infoGain(attr.get(i));
			//System.out.println(tmp+" "+attr.get(i));
			if(tmp<=info)
			{
				info=tmp;
				answer=attr.get(i);
			}
		}
		Node ng=new Node(answer);
		used.add(answer);
		//System.out.println(answer+" "+lvl);
		for(String str:catg.get(answer))
		{
			HashMap<String,ArrayList<String>> hmp=new HashMap<>();
			for(String str2:attr)
			{
				ArrayList<String> al=new ArrayList<>();
				for(int i=0;i<data.get(str2).size();i++)
				{
					if(data.get(answer).get(i).equals(str))
					{
						al.add(data.get(str2).get(i));
					}
				}
				if(al.size()==0)
					break;
				hmp.put(str2,al);
			}
			if(hmp.keySet().size()==0)
				continue;
			//System.out.println(str+" -> "+lvl);
			DecisionTree dt=new DecisionTree(hmp,catg,attr,op);
			Node ng1=new Node(str);
			ng1.edges.add(dt.build(used,n,lvl+1));
			ng.edges.add(ng1);
		}
		return ng;
	}
}
class solution
{
	static void printTree(Node ng,String str)
	{
		String str1=str+"->"+ng.name;
		for(int i=0;i<ng.edges.size();i++)
		{
			printTree(ng.edges.get(i),str1);
		}
		if(ng.edges.size()==0)
			System.out.println(str1);
	}
	public static void main(String []args)
	{
		Scanner sc=new Scanner(System.in);
		String file=sc.next();
		int col=sc.nextInt();
		try
		{
			File f=new File(file);
			Scanner sc1=new Scanner(f);
			DecisionTree dt=new DecisionTree(sc1,col);
			HashSet<String> al=new HashSet<>();
			al.add(dt.op);
			Node tree=dt.build(al,dt.attr.size()-1,1);
			printTree(tree,"");
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
