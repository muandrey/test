import java.io.IOException;

public class Solution {
	public static void main(String[] args) throws IOException {
		Vijimalka v=new Vijimalka();
		v.openFile("juice.in");
		v.diffComptsOriginalOrder();
		v.diffComptsSortedOrder();
		v.minTimesWash();
	}
}
