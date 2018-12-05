import java.awt.image.BufferedImage;
import java.util.*;
import java.io.*;
import java.awt.*;
import javax.imageio.*;

class HistoGram
{
	int img[][][];
	File fout;
	double p[][];
	int trans[][]=new int[3][256];
	HistoGram(int img[][][],File fout)
	{
		this.img=img;
		this.fout=fout;
		p=new double[3][256];
	}
	void cdf()
	{
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<img[i].length;j++)
			{
				for(int k=0;k<img[i][j].length;k++)
				{
					p[i][img[i][j][k]]+=1;
				}
			}
			for(int j=1;j<256;j++)
				p[i][j]+=p[i][j-1];
			for(int j=0;j<256;j++)
				p[i][j]=(p[i][j]-1)/(p[i][255]-1);
		}
	}
	void transform()
	{
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<256;j++)
				trans[i][j]=(int)Math.round(p[i][j]*255);
		}
	}
	void write()
	{
		BufferedImage buff=new BufferedImage(img[0].length,img[0][0].length,BufferedImage.TYPE_INT_RGB);
		for(int i=0;i<img[0].length;i++)
		{
			for(int j=0;j<img[0][i].length;j++)
			{
				int val=trans[0][img[0][i][j]]<<16|trans[1][img[1][i][j]]<<8|trans[2][img[2][i][j]];
				buff.setRGB(i,j,val);
			}
		}
		try
		{
			ImageIO.write(buff,"png",fout);
		}catch(Exception e)
		{
			System.out.println(e);
		}
	}
}
class solution {
   	public static void main(String []args)
   	{
   		int h=0,w=0;
   		try{
	   		Scanner sc=new Scanner(System.in);
	   		String file1=sc.next(),file2=sc.next();
	   		File f1=new File(file1),f2=new File(file2);
	   		BufferedImage fin=ImageIO.read(f1);
	   		int arr[][][]=new int[3][fin.getHeight()][fin.getWidth()];
	   		h=fin.getHeight();w=fin.getWidth();
	   		for(int i=0;i<fin.getHeight();i++)
	   		{
	   			for(int j=0;j<fin.getWidth();j++)
	   			{
	   				Color c=new Color(fin.getRGB(j,i));
	   				arr[0][i][j]=c.getRed();
	   				arr[1][i][j]=c.getGreen();
	   				arr[2][i][j]=c.getBlue();
	   				//System.out.println(i+" "+j+" "+arr[0][i][j]+"  "+arr[1][i][j]+" "+arr[2][i][j]);
	   			}
	   		}
	   		HistoGram hs=new HistoGram(arr,f2);
	   		hs.cdf();
	   		System.out.println("cdf completed");
	   		hs.transform();
	   		System.out.println("transformation completed");
	   		hs.write();
	   		System.out.println("image created");
	   	}catch(Exception e)
	   	{
	   		System.out.println(h+" "+w);
	   		e.printStackTrace();
	   	}
   	}
}
