import java.io.*;
import java.util.*;


public class Vijimalka {
	ArrayList<Juice> recips=new ArrayList<Juice >();
	LinkedHashSet<String> compt=new LinkedHashSet<String >();
	public void openFile(String filename) throws FileNotFoundException
	{
		Scanner sc=new Scanner(new FileInputStream("juice.txt"));
		recips=new ArrayList<Juice >();
		compt=new LinkedHashSet<String >();
		while(sc.hasNextLine())
		{
			StringTokenizer tok=new StringTokenizer(sc.nextLine()," ");
			Juice ju=new Juice();
			while(tok.hasMoreTokens())
			{
				String str;
				str=tok.nextToken();
				compt.add(str);
				ju.add(str);
			}
			if(!recips.contains(ju))
				recips.add(ju);
		}
		sc.close();
	}
	public void diffComptsOriginalOrder() throws IOException
	{
		OutputStreamWriter wr=new OutputStreamWriter(new FileOutputStream("juice1.txt"));
		for(String str:compt )
		{
			wr.write(str+" ");
		}
		wr.close();
	}
	public void diffComptsSortedOrder()
	{
		new Thread()
		{

			@Override
			public void run() {
				TreeSet<String> sortedcompts=new TreeSet<String >(new Comparator<String>()
				{

					@Override
					public int compare(String o1, String o2) {
						return o1.compareTo(o2);
					}
				});
				sortedcompts.addAll(compt);
				try(OutputStreamWriter wr=new OutputStreamWriter(new FileOutputStream("juice2.txt")))
				{
					for(String str:sortedcompts )
					{
						wr.write(str+" ");
					}
					wr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	public void minTimesWash() throws IOException
	{
		OutputStreamWriter wr=new OutputStreamWriter(new FileOutputStream("juice3.txt"));
		int min=recips.size();
		recips.sort(new Comparator<Juice>()
				{

					@Override
					public int compare(Juice o1, Juice o2) {
						return o1.compareTo(o2);
					}
			
				});
		while(nextPermutation())
		{
			int p=timesWash();
			if(min>p)
				min=p;
		}
		wr.write(String.valueOf(min));
		wr.close();
	}
	private int timesWash()
	{
		int num=0;
		Iterator<Juice > it=recips.iterator();
		Juice prev=it.next();
		while(it.hasNext())
		{
			Juice now=it.next();
			if(now.needWashAfter(prev))
				num++;
		}
		return num;
	}
	private boolean nextPermutation() {
		int i, j;
		for (i = recips.size()- 2; i >= 0; i--) {
			if (recips.get(i).compareTo( recips.get(i + 1))<0)
				break;
		}
		if (i < 0) {
		    return false;
		}
		for (j = recips.size() - 1; j > i; j--) {
		    if (recips.get(j).compareTo(recips.get(i))>0)
		 break;
		}
		swap(i++, j);
		for (j = recips.size() - 1; j > i; i++, j--) {
			swap( i, j);
		}
	 return true;
	 }
	private void swap(int x, int y) {
		Juice a=recips.get(x);
		recips.set(x,recips.get(y));
		recips.set(y,a);
	}

}
