import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class TimeDiff {
	static String dayslist[] = new String[]{"Mon","Tues","Wed","Thurs","Fri","Sat","Sun"};
	
	public static void main(String[] args) throws FileNotFoundException  {
		parseData(args[0]);
	}
	
	public static void parseData(String filename) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(filename));
		ArrayList<String> data = new ArrayList<String>();
		while(sc.hasNext()) 
			data.add(sc.nextLine());
		sc.close();
		
		boolean diffArr[][][] = new boolean[data.size()][7][24];
		int i = 0;
		for(String times:data) {
			//System.out.println(times);
			String[] days = times.split(", ");
			//System.out.println(i);
			for(String day_str:days) {
				String[] day_info = day_str.split("[.] ");
				int day = getDayIndex(day_info[0]);
				//System.out.println(day_info[0]+"--"+day);
				String range[] = day_info[1].split(" - ");
				//System.out.println(day_info[1]);
				int hour1 = getHourIndexes(day_info[1])[0], hour2 = getHourIndexes(day_info[1])[1];
				//System.out.println(range[0]+"-"+range[1]+" == "+hour1+"-"+hour2);
				for(int j = hour1; j < hour2; j++)
					diffArr[i][day][j-1] = true;
			}
			//System.out.println("\n");
			i++;
		}
		
		boolean andedArr[][] = new boolean[7][24];
		for(int r = 0; r < andedArr.length; r++)
			for(int c = 0; c < andedArr[r].length; c++)
				andedArr[r][c] = true;
		
		for(i = 0; i < diffArr.length; i++) {
			//printTimes(diffArr[i]);
			//System.out.println("\n");
			for(int r = 0; r < diffArr[i].length; r++)
				for(int c = 0; c < diffArr[i][r].length; c++)
					andedArr[r][c] &= diffArr[i][r][c];
		}
		
		System.out.println("Intersection of hours available:\n(first column is 1 AM, last is midnight)");
		printTimes(andedArr);
	}
	
	public static void printTimes(boolean[][] matrix) {
		System.out.print("\t");
		for(int i = 0; i < matrix[0].length; i++)
			System.out.print((i+1)%10);
		System.out.println();
		for(int r = 0; r < matrix.length; r++) {
			System.out.print(dayslist[r]+":\t");
			for(int c = 0; c < matrix[0].length; c++) {
				int num = (matrix[r][c]) ? 1 : 0;
				System.out.print(num);
			}
			System.out.println();
		}
		System.out.print("\t");
		for(int i = 0; i < matrix[0].length; i++)
			System.out.print((i+1)%10);
		System.out.println();
	}
	
	public static int[] getHourIndexes(String range) {
		String[] tokens = range.split(" ");
		String hours[] = range.split(" - ");
		int index1 = getHourIndex(hours[0]),
			index2 = getHourIndex(hours[1]);
		if (tokens.length < 5) {
			String suffix = tokens[3];
			if (suffix.equals("PM")) 
				index1+=12;
			else if (suffix.equals("AM") && index1 == 12)
				index1 = 24;
		}
		return new int[]{index1, index2};
	}
	
	public static int getHourIndex(String hour) {
		String tokens[] = hour.split(" ");
		int num = 0;
		if (hour.split(":").length == 1)
			num = Integer.parseInt(tokens[0]);
		else
			num = Integer.parseInt(hour.split(":")[0]);
		if (tokens.length > 1) {
			String suffix = tokens[1];
			if (suffix.equals("PM")) 
				num+=12;
			else if (suffix.equals("AM") && num == 12)
				num = 24;
		}
		return num;
	}
	
	public static int getDayIndex(String day) {
		for(int i = 0; i < dayslist.length; i++)
			if (day.equals(dayslist[i]))
				return i;
		return -1;
	}
}
