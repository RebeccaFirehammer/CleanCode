package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class RecordProcessor {
	
	private static String [] firstName;
	private static String [] lastName;
	private static int [] age;
	private static String [] employeeType;
	private static double [] pay;
	private StringBuffer stringBuffer = new StringBuffer();
	private Scanner scanner;
		
	public void readInFile(String fileInput){
		try {
			scanner = new Scanner(new File(fileInput));
		} catch (FileNotFoundException exception) {
			System.err.println(exception.getMessage());
		}
	}
	public void assignValues(){
		int inputLength = 0;
		
		while(scanner.hasNextLine()) {
			String lineInput = scanner.nextLine();
			if(lineInput.length() > 0)
				inputLength++;
		}

		firstName = new String[inputLength];
		lastName = new String[inputLength];
		age = new int[inputLength];
		employeeType = new String[inputLength];
		pay = new double[inputLength];

		scanner.close();
	}
	public String processFile(String fileInput){
		String output = "";
		readInFile(fileInput);
		assignValues();
		return output;
	}
	
	public String oldProcessFile(String fileInput) {
//		StringBuffer stringBuffer = new StringBuffer();
//		Scanner scanner = null;
//		try {
//			scanner = new Scanner(new File(fileInput));
//		} catch (FileNotFoundException e) {
//			System.err.println(e.getMessage());
//			return null;
//		}
		
//		int c = 0;
//		while(scanner.hasNextLine()) {
//			String l = scanner.nextLine();
//			if(l.length() > 0)
//				c++;
//		}
//
//		firstName = new String[c];
//		lastName = new String[c];
//		age = new int[c];
//		employeeType = new String[c];
//		pay = new double[c];
//
//		scanner.close();
		try {
			scanner = new Scanner(new File(fileInput));
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			return null;
		}

		int count = 0;
		while(scanner.hasNextLine()) {
			String l = scanner.nextLine();
			if(l.length() > 0) {
				
				String [] words = l.split(",");

				int c2 = 0; 
				for(;c2 < lastName.length; c2++) {
					if(lastName[c2] == null)
						break;
					
					if(lastName[c2].compareTo(words[1]) > 0) {
						for(int i = count; i > c2; i--) {
							firstName[i] = firstName[i - 1];
							lastName[i] = lastName[i - 1];
							age[i] = age[i - 1];
							employeeType[i] = employeeType[i - 1];
							pay[i] = pay[i - 1];
						}
						break;
					}
				}
				
				firstName[c2] = words[0];
				lastName[c2] = words[1];
				employeeType[c2] = words[3];

				try {
					age[c2] = Integer.parseInt(words[2]);
					pay[c2] = Double.parseDouble(words[4]);
				} catch(Exception e) {
					System.err.println(e.getMessage());
					scanner.close();
					return null;
				}
				
				count++;
			}
		}
		
		if(count == 0) {
			System.err.println("No records found in data file");
			scanner.close();
			return null;
		}
		
		//print the rows
		stringBuffer.append(String.format("# of people imported: %d\n", firstName.length));
		
		stringBuffer.append(String.format("\n%-30s %s  %-12s %12s\n", "Person Name", "Age", "Emp. Type", "Pay"));
		for(int i = 0; i < 30; i++)
			stringBuffer.append(String.format("-"));
		stringBuffer.append(String.format(" ---  "));
		for(int i = 0; i < 12; i++)
			stringBuffer.append(String.format("-"));
		stringBuffer.append(String.format(" "));
		for(int i = 0; i < 12; i++)
			stringBuffer.append(String.format("-"));
		stringBuffer.append(String.format("\n"));
		
		for(int i = 0; i < firstName.length; i++) {
			stringBuffer.append(String.format("%-30s %-3d  %-12s $%12.2f\n", firstName[i] + " " + lastName[i], age[i]
				, employeeType[i], pay[i]));
		}
		
		int sum1 = 0;
		float avg1 = 0f;
		int c2 = 0;
		double sum2 = 0;
		double avg2 = 0;
		int c3 = 0;
		double sum3 = 0;
		double avg3 = 0;
		int c4 = 0;
		double sum4 = 0;
		double avg4 = 0;
		for(int i = 0; i < firstName.length; i++) {
			sum1 += age[i];
			if(employeeType[i].equals("Commission")) {
				sum2 += pay[i];
				c2++;
			} else if(employeeType[i].equals("Hourly")) {
				sum3 += pay[i];
				c3++;
			} else if(employeeType[i].equals("Salary")) {
				sum4 += pay[i];
				c4++;
			}
		}
		avg1 = (float) sum1 / firstName.length;
		stringBuffer.append(String.format("\nAverage age:         %12.1f\n", avg1));
		avg2 = sum2 / c2;
		stringBuffer.append(String.format("Average commission:  $%12.2f\n", avg2));
		avg3 = sum3 / c3;
		stringBuffer.append(String.format("Average hourly wage: $%12.2f\n", avg3));
		avg4 = sum4 / c4;
		stringBuffer.append(String.format("Average salary:      $%12.2f\n", avg4));
		
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		int c1 = 0;
		for(int i = 0; i < firstName.length; i++) {
			if(hm.containsKey(firstName[i])) {
				hm.put(firstName[i], hm.get(firstName[i]) + 1);
				c1++;
			} else {
				hm.put(firstName[i], 1);
			}
		}

		stringBuffer.append(String.format("\nFirst names with more than one person sharing it:\n"));
		if(c1 > 0) {
			Set<String> set = hm.keySet();
			for(String str : set) {
				if(hm.get(str) > 1) {
					stringBuffer.append(String.format("%s, # people with this name: %d\n", str, hm.get(str)));
				}
			}
		} else { 
			stringBuffer.append(String.format("All first names are unique"));
		}

		HashMap<String, Integer> hm2 = new HashMap<String, Integer>();
		int c21 = 0;
		for(int i = 0; i < lastName.length; i++) {
			if(hm2.containsKey(lastName[i])) {
				hm2.put(lastName[i], hm2.get(lastName[i]) + 1);
				c21++;
			} else {
				hm2.put(lastName[i], 1);
			}
		}

		stringBuffer.append(String.format("\nLast names with more than one person sharing it:\n"));
		if(c21 > 0) {
			Set<String> set = hm2.keySet();
			for(String str : set) {
				if(hm2.get(str) > 1) {
					stringBuffer.append(String.format("%s, # people with this name: %d\n", str, hm2.get(str)));
				}
			}
		} else { 
			stringBuffer.append(String.format("All last names are unique"));
		}
		
		//close the file
		scanner.close();
		
		return stringBuffer.toString();
	}
}