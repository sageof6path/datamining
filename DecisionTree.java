import java.util.*;
import java.io.*;
class solution
{
	static ArrayList<List<String>> readData(String fileName)
	{
		ArrayList<List<String>> data=new ArrayList<>();
		try{
			File f=new File(fileName);
			Scanner sc=new Scanner(f);
			while(sc.hasNext())
			{
				String str=sc.nextLine();
				data.add(Arrays.asList(str.split(",")));
			}
		}catch(Exception e)
		{
			System.out.println(e);
		}
		return data;
	}
	static HashSet<String> getCategories(ArrayList<List<String>> data,int column)
	{
		HashSet<String> hs=new HashSet<>();
		for(int i=1;i<data.size();i++)
		{
			hs.add(data.get(i).get(column));
		}
		return hs;
	}
	static double targetEntropy(List<String> data,HashSet<String> targetCategories)
	{
		HashMap<String,Integer> hmp=new HashMap<>();
		for(String tmp:targetCategories)
			hmp.put(tmp,0);
		for(int i=0;i<data.size();i++)
		{
			hmp.put(data.get(i),hmp.get(data.get(i))+1);
		}
		double entropy=0.0;
		for(String key:hmp.keySet())
		{
			entropy+=info(hmp.get(key),data.size());
		}
		return -1*entropy;
	}
	static double Entropy(List<List<String>> data,HashSet<String> targetCategories,int column,int targetColumn)
	{
		int arr[]=new int[data.size()+1];
		double entropy=0.0;
		for(int i=0;i<data.get(0).size();i++)
		{
			if(arr[i]!=0)
				continue;
			List<String> al=new ArrayList<>();
			for(int j=i;j<data.size();j++)
			{
				if(data.get(i).get(column).equals(data.get(j).get(column)))
				{
					al.add(data.get(j).get(targetColumn));
					arr[j]=1;
				}
			}
			entropy+=targetEntropy(al,targetCategories)*(double)al.size()/(double)data.size();
		}
		return entropy;
	}
	static double info(double x,double y)
	{
		return (x/y)*Math.log(x/y)/Math.log(2);
	}
	static ArrayList buildDecisionTree(ArrayList<List<String>> data,HashSet<String> targetCategories,int targetColumn)
	{
		List<String> targetCol=new ArrayList<>();
		for(List<String> l:data.subList(1,data.size()))
			targetCol.add(l.get(targetColumn));
		double targetEntro=targetEntropy(targetCol,targetCategories);
		System.out.println(targetEntro);
		for(int i=0;i<data.get(0).size();i++)
		{
			System.out.print(data.get(0).get(i)+" -> ");
			System.out.println(targetEntro - Entropy(data.subList(1,data.size()),targetCategories,i,targetColumn));
		}
		return null;
	}
	public static void main(String []args)
	{
			Scanner sc=new Scanner(System.in);
			System.out.println("enter the file name:-");
			String fileName=sc.next();
			ArrayList<List<String>> data=readData(fileName);
			List<String> header=data.get(0);
			System.out.println("enter target class name");
			String target=sc.next();
			int targetColumn=header.indexOf(target);
			HashSet<String> targetCategories=getCategories(data,targetColumn);
			buildDecisionTree(data,targetCategories,targetColumn);
	}
}
