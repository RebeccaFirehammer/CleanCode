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
	private static StringBuffer stringBuffer = new StringBuffer();
	private static Scanner scanner;
	
	private static int totalEmployeeAge = 0;
	private static int totalCommissionEmployees = 0;
	private static double totalCommissionPay = 0;
	private static int totalHourlyEmployees = 0;
	private static double totalHourlyPay = 0;
	private static int totalSalaryEmployees = 0;
	private static double totalSalaryPay = 0;
		
	public static void readInFile(String fileInput){
		try {
			scanner = new Scanner(new File(fileInput));
		} catch (FileNotFoundException exception) {
			System.err.println(exception.getMessage());
		}
	}
	public static int readFileValues(){
		int inputLength = 0;
		
		while(scanner.hasNextLine()) {
			String lineInput = scanner.nextLine();
			if(lineInput.length() > 0)
				inputLength++;		
		}
		scanner.close();
		return inputLength;
	}
	public static void initializeEmployeeValues(){
		int inputLength = readFileValues();
		firstName = new String[inputLength];
		lastName = new String[inputLength];
		age = new int[inputLength];
		employeeType = new String[inputLength];
		pay = new double[inputLength];
	}
	
	public static double getAverageEmployeeAge() {
		return totalEmployeeAge / firstName.length;
	}
	
	public static double getAverageCommissionPay() {
		return totalCommissionPay / totalCommissionEmployees;
	}
	
	public static double getAverageHourlyPay() {
		return totalHourlyPay / totalHourlyEmployees;
	}
	
	public static double getAverageSalaryPay() {
		return totalSalaryPay / totalSalaryEmployees;
	}
	
	public static void getEmployeeStats() {
		for(int i = 0; i < firstName.length; i++) {
			totalEmployeeAge += age[i];
			if(employeeType[i].equals("Commission")) {
				totalCommissionPay += pay[i];
				totalCommissionEmployees++;
			} else if(employeeType[i].equals("Hourly")) {
				totalHourlyPay += pay[i];
				totalHourlyEmployees++;
			} else if(employeeType[i].equals("Salary")) {
				totalSalaryPay += pay[i];
				totalSalaryEmployees++;
			}
		}
	}
	
	public static void printEmployeeStats() {
		stringBuffer.append(String.format("\nAverage age:         %12.1f\n", getAverageEmployeeAge()));
		stringBuffer.append(String.format("Average commission:  $%12.2f\n", getAverageCommissionPay()));
		stringBuffer.append(String.format("Average hourly wage: $%12.2f\n", getAverageHourlyPay()));
		stringBuffer.append(String.format("Average salary:      $%12.2f\n", getAverageSalaryPay()));
	}
	
	public static void alphabetizeEmployeesByLastname(int recordCount, int employeeNum){
		for(int i = recordCount; i > employeeNum; i--) {
			firstName[i] = firstName[i - 1];
			lastName[i] = lastName[i - 1];
			age[i] = age[i - 1];
			employeeType[i] = employeeType[i - 1];
			pay[i] = pay[i - 1];
		}
	}
	public static void assignEmployeeValues(int employeeNum, String [] employeeValue){
		firstName[employeeNum] = employeeValue[0];
		lastName[employeeNum] = employeeValue[1];
		age[employeeNum] = Integer.parseInt(employeeValue[2]);
		employeeType[employeeNum] = employeeValue[3];
		pay[employeeNum] = Double.parseDouble(employeeValue[4]);
	}
	public static void siftLastnames(int recordCount, String [] employeeValue ){
		int employeeNum;
				
		for(employeeNum = 0; employeeNum < lastName.length; employeeNum++) {
			if(lastName[employeeNum] == null)
				break;
			if(lastName[employeeNum].compareTo(employeeValue[1]) > 0) {
				alphabetizeEmployeesByLastname(recordCount, employeeNum);
				break;
			}
		}	
		assignEmployeeValues(employeeNum, employeeValue);		
	}
	public static void printRows(){
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
	}
	
	public static HashMap<String, Integer> createMap(String [] names){
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		for(int i = 0; i < names.length; i++) {
			if(hm.containsKey(names[i])) {
				hm.put(names[i], hm.get(names[i]) + 1);
			} else {
				hm.put(names[i], 1);
			}
		}
		return hm;
	}
	
	public static void compareMapToArray(HashMap<String, Integer> hm, String [] names) {
		if(names.length > hm.size()) {
			Set<String> set = hm.keySet();
			for(String str : set) {
				if(hm.get(str) > 1) {
					stringBuffer.append(String.format("%s, # people with this name: %d\n", str, hm.get(str)));
				}
			}
		} else { 
			stringBuffer.append(String.format("All first names are unique"));
		}
	}
	
	public static String processFile(String fileInput) {
		readInFile(fileInput);
		initializeEmployeeValues();
		
		readInFile(fileInput);
		try{
			int recordCount = 0;
			while(scanner.hasNextLine()) {
				String lineInput = scanner.nextLine();
				if(lineInput.length() > 0) {
					String [] employeeValue = lineInput.split(",");
					siftLastnames(recordCount, employeeValue);
					
					recordCount++;
				}
			}
			if(recordCount == 0) {
				System.err.println("No records found in data file");
				scanner.close();
				return null;
			}
		}catch(Exception e){
			System.err.println(e.getMessage());
			scanner.close();
			return null;
		}
		
		printRows();
		getEmployeeStats();
		printEmployeeStats();
		
		HashMap<String, Integer> hm = createMap(firstName);

		stringBuffer.append(String.format("\nFirst names with more than one person sharing it:\n"));
		compareMapToArray(hm, firstName);

		HashMap<String, Integer> hm2 = createMap(lastName);

		stringBuffer.append(String.format("\nLast names with more than one person sharing it:\n"));
		compareMapToArray(hm2, lastName);
		
		//close the file
		scanner.close();
		
		return stringBuffer.toString();
	}
}
