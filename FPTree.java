import java.util.*;
import java.io.*;
class Tree
{
	String name;
	int count;
	ArrayList<Tree> edges;
	Tree(String name)
	{
		this.name=name;
		count=1;
		edges=new ArrayList<Tree>();
	}
}
class Pair implements Comparable<Pair>
{
	String name;
	int count;
	Pair(String name,int count)
	{
		this.name=name;
		this.count=count;
	}
	public int compareTo(Pair a)
	{
		if(count>a.count)
			return -1;
		else if(count<a.count)
			return 1;
		else
			return name.compareTo(a.name);
			
	}
	public String toString()
	{
		return name+"("+count+")";
	}
}
class FPTree
{
	ArrayList<HashSet<String>> data=new ArrayList<>();
	ArrayList<Pair> Fpat=new ArrayList<>();
	int sup;
	FPTree(Scanner sc,int sup)
	{
		this.sup=sup;
		HashMap<String,Integer> hmp=new HashMap<>();
		while(sc.hasNext())
		{
			String str[]=sc.next().split(",");
			HashSet<String> al=new HashSet<>();
			for(int i=0;i<str.length;i++)
			{
				al.add(str[i]);
			}
			for(String str2:al)
			{
				if(hmp.containsKey(str2))
					hmp.put(str2,hmp.get(str2)+1);
				else
					hmp.put(str2,1);
			}
			data.add(al);
		}
		for(String str:hmp.keySet())
		{
			if(hmp.get(str)<sup)
				continue;
			Fpat.add(new Pair(str,hmp.get(str)));
		}
		Collections.sort(Fpat);
		System.out.println(Fpat);
	}
	void build(Tree node,LinkedList<String> ll)
	{
		//System.out.println(ll+" "+node.count+" "+node.name);
		if(ll.size()==0)
			return ;
		int flg=0;
		for(int i=0;i<node.edges.size();i++)
		{
			if(node.edges.get(i).name.equals(ll.getFirst()))
			{
				ll.removeFirst();
				Tree n=node.edges.get(i);
				n.count+=1;
				node.edges.set(i,n);
				build(node.edges.get(i),ll);
				flg=1;
				break;
			}
		}
		if(flg==0)
		{
			Tree ng=new Tree(ll.getFirst());
			node.edges.add(ng);
			ll.removeFirst();
			build(ng,ll);
		}
	}
	Tree buildFP()
	{
		Tree ng=new Tree("NULL");
		for(int i=0;i<data.size();i++)
		{
			LinkedList<String> ll=new LinkedList<>();
			for(int j=0;j<Fpat.size();j++)
			{
				if(data.get(i).contains(Fpat.get(j).name))
					ll.addLast(Fpat.get(j).name);
			}
			build(ng,ll);
		}
		return ng;
	}
}
class solution
{
	static void printFP(Tree ng,String str)
	{
		str+="->"+ng.name;
		if(!ng.name.equals("NULL"))
			str+="("+ng.count+")";
		for(int i=0;i<ng.edges.size();i++)
			printFP(ng.edges.get(i),str);
		if(ng.edges.size()==0)
			System.out.println(str);
	}
	public static void main(String []args)
	{
		Scanner sc=new Scanner(System.in);
		String f=sc.next();
		int sup=sc.nextInt();
		try
		{
			File file=new File(f);
			FPTree fp=new FPTree(new Scanner(file),sup);
			Tree ng=fp.buildFP();
			printFP(ng,"");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
