import java.util.Iterator;
import java.util.TreeSet;


public class Juice implements Comparable<Juice> {
	private TreeSet<String> compts=new TreeSet<String>();
	public boolean needWashAfter(Juice j)
	{
		return !compts.containsAll(j.compts);
	}
	public void add(String comp)
	{
		if(!compts.contains(comp))
			compts.add(comp);
	}
	@Override
	public int compareTo(Juice o) {
		Iterator<String> it1=compts.iterator(),it2=o.compts.iterator();
		while(it1.hasNext() && it2.hasNext())
		{
			int k=it1.next().compareTo(it2.next());
			if(k==0)
				continue;
			return k;
		}
		if(!it1.hasNext() && !it2.hasNext())
			return 0;
		return it1.hasNext()?-1:1;
	}
	public boolean equals(Object j)
	{
		try{
		return compts.equals(((Juice)j).compts);
		} catch(Exception e){};
		return false;
	}
}
