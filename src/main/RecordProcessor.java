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
	private static StringBuffer stringBufferOutput = new StringBuffer();
	private static Scanner scanner;
	
	private static int totalEmployeeAge = 0;
	private static int totalCommissionEmployees = 0;
	private static double totalCommissionPay = 0;
	private static int totalHourlyEmployees = 0;
	private static double totalHourlyPay = 0;
	private static int totalSalaryEmployees = 0;
	private static double totalSalaryPay = 0;
		
	private static void readInFile(String fileInput){
		try {
			scanner = new Scanner(new File(fileInput));
		} catch (FileNotFoundException exception) {
			System.err.println(exception.getMessage());
		}
	}
	private static int readFileValues(){
		int inputLength = 0;
		
		while(scanner.hasNextLine()) {
			String lineInput = scanner.nextLine();
			if(lineInput.length() > 0)
				inputLength++;		
		}
		scanner.close();
		return inputLength;
	}
	private static void initializeEmployeeValues(){
		int inputLength = readFileValues();
		firstName = new String[inputLength];
		lastName = new String[inputLength];
		age = new int[inputLength];
		employeeType = new String[inputLength];
		pay = new double[inputLength];
	}
	
	private static void getEmployeeStats() {
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
	
	private static void printEmployeeStats() {
		stringBufferOutput.append(String.format("\nAverage age:         %12.1f\n", getAverageEmployeeAge()));
		stringBufferOutput.append(String.format("Average commission:  $%12.2f\n", getAverageCommissionPay()));
		stringBufferOutput.append(String.format("Average hourly wage: $%12.2f\n", getAverageHourlyPay()));
		stringBufferOutput.append(String.format("Average salary:      $%12.2f\n", getAverageSalaryPay()));
	}
	
	private static double getAverageEmployeeAge() {
		return totalEmployeeAge / firstName.length;
	}
	
	private static double getAverageCommissionPay() {
		return totalCommissionPay / totalCommissionEmployees;
	}
	
	private static double getAverageHourlyPay() {
		return totalHourlyPay / totalHourlyEmployees;
	}
	
	private static double getAverageSalaryPay() {
		return totalSalaryPay / totalSalaryEmployees;
	}
	
	private static void assignEmployeeValues(int employeeNum, String [] employeeValue){
		firstName[employeeNum] = employeeValue[0];
		lastName[employeeNum] = employeeValue[1];
		age[employeeNum] = Integer.parseInt(employeeValue[2]);
		employeeType[employeeNum] = employeeValue[3];
		pay[employeeNum] = Double.parseDouble(employeeValue[4]);
	}
	private static void siftLastNames(int recordCount, String [] employeeValue ){
		int employeeNum;
				
		for(employeeNum = 0; employeeNum < lastName.length; employeeNum++) {
			if(lastName[employeeNum] == null)
				break;
			if(lastName[employeeNum].compareTo(employeeValue[1]) > 0) {
				alphabetizeEmployeesByLastName(recordCount, employeeNum);
				break;
			}
		}	
		assignEmployeeValues(employeeNum, employeeValue);		
	}
	
	private static void alphabetizeEmployeesByLastName(int recordCount, int employeeNum){
		for(int i = recordCount; i > employeeNum; i--) {
			firstName[i] = firstName[i - 1];
			lastName[i] = lastName[i - 1];
			age[i] = age[i - 1];
			employeeType[i] = employeeType[i - 1];
			pay[i] = pay[i - 1];
		}
	}
	
	private static void printRows(){
		stringBufferOutput.append(String.format("# of people imported: %d\n", firstName.length));
		stringBufferOutput.append(String.format("\n%-30s %s  %-12s %12s\n", "Person Name", "Age", "Emp. Type", "Pay"));
		for(int i = 0; i < 30; i++)
			stringBufferOutput.append(String.format("-"));
		stringBufferOutput.append(String.format(" ---  "));
		for(int i = 0; i < 12; i++)
			stringBufferOutput.append(String.format("-"));
		stringBufferOutput.append(String.format(" "));
		for(int i = 0; i < 12; i++)
			stringBufferOutput.append(String.format("-"));
		stringBufferOutput.append(String.format("\n"));	
		for(int i = 0; i < firstName.length; i++) {
			stringBufferOutput.append(String.format("%-30s %-3d  %-12s $%12.2f\n", firstName[i] + " " + lastName[i], age[i]
			, employeeType[i], pay[i]));
		}
	}
	
	private static void printMatchingNames(){
		HashMap<String, Integer> firstNameHashMap = createMap(firstName);
		HashMap<String, Integer> lastNameHashMap = createMap(lastName);

		stringBufferOutput.append(String.format("\nFirst names with more than one person sharing it:\n"));
		compareMapToArray(firstNameHashMap, firstName);

		stringBufferOutput.append(String.format("\nLast names with more than one person sharing it:\n"));
		compareMapToArray(lastNameHashMap, lastName);
	}
	
	private static HashMap<String, Integer> createMap(String [] names){
		HashMap<String, Integer> nameHashMap = new HashMap<String, Integer>();
		for(int i = 0; i < names.length; i++) {
			if(nameHashMap.containsKey(names[i])) {
				nameHashMap.put(names[i], nameHashMap.get(names[i]) + 1);
			} else {
				nameHashMap.put(names[i], 1);
			}
		}
		return nameHashMap;
	}
	
	private static void compareMapToArray(HashMap<String, Integer> nameHashMap, String [] nameArray) {
		if(nameArray.length > nameHashMap.size()) {
			Set<String> uniqueNames = nameHashMap.keySet();
			for(String name : uniqueNames) {
				if(nameHashMap.get(name) > 1) {
					stringBufferOutput.append(String.format("%s, # people with this name: %d\n", name, nameHashMap.get(name)));
				}
			}
		} else { 
			stringBufferOutput.append(String.format("All first names are unique"));
		}
	}
	
	private static String readEmployeeValues(){
		int recordCount = 0;
		while(scanner.hasNextLine()) {
			String lineInput = scanner.nextLine();
			if(lineInput.length() > 0) {
				String [] employeeValue = lineInput.split(",");
				siftLastNames(recordCount, employeeValue);
				recordCount++;
			}
		}
		if(recordCount == 0) {
			System.err.println("No records found in data file");
			scanner.close();
			return null;
		}
		return "";
	}
	/**
	 * processFile takes an input file containing employee records of
	 * format: lastName, firstName, age, employeeType, hourlyWage/salary
	 * and writes calculated details and organized information to an output string buffer:
	 * @param fileInput
	 * @return stringBufferOutput
	 */
	public static String processFile(String fileInput) {
		readInFile(fileInput);
		initializeEmployeeValues();
		readInFile(fileInput);
		try{
			if(readEmployeeValues() == null){
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
		printMatchingNames();
		
		//close the file
		scanner.close();
		
		return stringBufferOutput.toString();
	}
}
