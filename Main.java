import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map.Entry;

class Main {
	public static void main(String args[]) {
		
		// initialise directories
		String directoryDrivers = "src\\textFiles\\driver-info.txt";
		String directoryInvoice = "src\\textFiles\\invoice.txt";
		
		// initialise foodItem objects
		FoodItem pepperoniPizza1 = new FoodItem("Pepperoni pizza", 78.00);
		FoodItem hawaiianPizza1 = new FoodItem("Hawaiian pizza", 82.00);
		FoodItem hawaiianPizza2 = new FoodItem("Hawaiian pizza", 82.00);
		
		// initialise orderedItems array
		FoodItem[] orderedItems = {pepperoniPizza1, hawaiianPizza1, hawaiianPizza2};
		
		// Initialise Order object
		Order orderObject = new Order(orderedItems, "1234", "Extra tomato base on the Pepperoni pizza");
		
		// Initialise scanner
		Scanner sc = new Scanner(System.in);
		
		// Program starts here
		System.out.println("Greetings customer");
		System.out.println("");
		
		// Get customer information using method
		String customerName = getName(sc, "customer");
		String customerContactNumber = getContactNumber(sc, "customer");
		String customerAddress = getCustomerAddress(sc);
		String customerSuburb = getCustomerSuburb(sc);
		String customerCity = getCity(sc, "customer");
		String customerEmailAddress = getCustomerEmailAddress(sc);
		
		// Initialise Customer object with constructor
		Customer customerObject = new Customer(customerName.strip(), customerContactNumber.strip(), 
				customerAddress.strip(), customerSuburb.strip(), 
				customerCity.strip(), customerEmailAddress.strip().toLowerCase());
		
		System.out.println("");
		
		// Get Restaurant information using methods
		String restaurantName = getName(sc, "restaurant");
		String restaurantContactNumber = getContactNumber(sc, "restaurant");
		String restaurantCity = getCity(sc, "restaurant");
		
		// Initialise restaurant object
		Restaurant restaurantObject = new Restaurant(restaurantName.strip(), 
				restaurantCity.strip(), restaurantContactNumber.strip());
		
		// Close scanner
		sc.close();
		
		// Get the drivers from driver.txt in String ArrayList
		ArrayList<String> arrayListToIterate = getLinesOfDrivers(directoryDrivers);
		
		
		// Create driver objects array from the arrayToIterate array
		Driver[] driverObjectArray = makeDriverObjectArray(arrayListToIterate);
		
		
		// Get ArrayList of available drivers for the customer
		ArrayList<Driver> availableDrivers = findDriversForRestaurant(restaurantObject, driverObjectArray);
		
		// Conditional to decide which invoice to create based on if there are drivers in the same city as the customer
		if (availableDrivers.size() < 1) {
			
			noDriversInvoiceGenerator(directoryInvoice);		
		} else {
			
			// Get arrayList for invoices
			ArrayList<String> foodItemArrayList =  makeFoodItemInvoiceArrayList(orderObject);
				
			// Get Driver with lowest load
			Driver driverLowestAvailable = findDriverLowestLoad(availableDrivers);
				
			// Make invoice for if there are drivers available
			yesDriversInvoiceGenerator(orderObject, restaurantObject, customerObject, driverLowestAvailable, directoryInvoice, foodItemArrayList);
			}
		
		// Alert user their invoice is written
		System.out.println("Your invoice has been written to: " + directoryInvoice);
	}
	
	
	
	// Method to get list of strings from driver.txt 
	public static ArrayList<String>  getLinesOfDrivers(String directory) {
		ArrayList<String> arrayToIterate = new ArrayList<String>();
		
		try {
			File driverFile = new File(directory);
			Scanner sc = new Scanner(driverFile);
			while (sc.hasNext()) {
				arrayToIterate.add(sc.nextLine());
			}
			
			sc.close();
					
		} catch (Exception e) {
			System.out.println("Error occurred, file not found.");
		}
		
		return arrayToIterate;
	}
	
	// Method to make driver object array
	public static Driver[] makeDriverObjectArray(ArrayList<String> arrayListToIterate) {
		Driver[] driverArray = new Driver[arrayListToIterate.size()];
		
		for (int i = 0; i < arrayListToIterate.size(); i++) {
			
			String driverString = arrayListToIterate.get(i);
			String[] driverStringParts = driverString.split(",");
			String driverName = driverStringParts[0].strip();
			String city = driverStringParts[1].strip(); 
			int itemsCarried = Integer.parseInt(driverStringParts[2].strip());
			
			Driver driverToAdd = new Driver(driverName, city, itemsCarried);
			driverArray[i] = driverToAdd;
		}

		return driverArray;
	}

	// Method to get name for customer or restaurant
	public static String getName(Scanner sc, String nameObjectType) {
		
		// Initialise variables
		boolean nameGood = false;
		String nameToCheck = null;
		
		// While loop to check that the name is at least 2 characters long
		while (!nameGood) {
			System.out.println("Enter " + nameObjectType + " name: ");
			nameToCheck = sc.nextLine();
			
			if (nameToCheck.strip().length() < 2) {
				System.out.println("Please enter at least 2 characters");
			} else {
				nameGood = true;
			}	
		}
		
		return nameToCheck;
	}
	
	// Method to get city for customer or restaurant
	public static String getCity(Scanner sc, String nameObjectType) {
		
		// Initialise variables
		boolean cityGood = false;
		boolean digitsCity = false;
		String city = null;
		
		// While loop until city name is valid
		while (!cityGood) {
			cityGood = true;
			digitsCity = false;
			
			System.out.println("Enter " + nameObjectType + " city: ");
			city = sc.nextLine();
			
			// Check that city is at least 5 characters long
			if (city.replaceAll("\\s+", "").length() < 5) {
				System.out.println("City name is too short.");
				cityGood = false;
			} 
			
			// Check that there are no digits in the String
			for (char c: city.toCharArray()) {
				if (Character.isDigit(c)) {
					digitsCity = true;
				}
			}
			
			if (digitsCity == true) {
				cityGood = false;
				System.out.println("There are no digits in the names of South African cities.");
				System.out.println("Please enter a city not containing digits");
			} 
			
		}
		
		return city;
	}
	
	// Method to get contact number for customer or restaurant	
	public static String getContactNumber(Scanner sc, String nameObjectType) {
		
		// Initialise variables
		boolean phoneNumberGood = false;
		String phoneNumber = null;
		
		// While loop until phone number is valid
		while (!phoneNumberGood) {
			phoneNumberGood = true;
		
			System.out.println("Enter " + nameObjectType + " phone number: ");
			phoneNumber = sc.nextLine();
			
			// Check that there are no digits in the String
			for (char c: phoneNumber.toCharArray()) {
				if (!Character.isDigit(c)) {		
					phoneNumberGood = false;
				} 
			}
			
			// Check that the phone number is 10 digits
			if (!phoneNumberGood) {
				System.out.println("Please enter a phone number, (only numbers).");
			} else if (phoneNumber.replaceAll("\\s+", "").length() != 10) {
				System.out.println("Please enter 10 numbers for the phone number.");
				phoneNumberGood = false;
			} 
		
		}
		
		return phoneNumber;
	}
	
	// Method to get the customer address
	public static String getCustomerAddress(Scanner sc) {
		
		// initialise variables
		boolean streetAddressGood = false;
		String customerStreetAddress = null;
		
		// While loop to make sure that street address is good
		while (!streetAddressGood) {
			boolean digitsThere = false;
			
			System.out.println("Enter customer street address: ");
			customerStreetAddress = sc.nextLine();
			
			// Check that there is at least 1 digit in the String
			for (char c: customerStreetAddress.toCharArray()) {
				if (Character.isDigit(c)) {
					digitsThere = true;			
				}
			}
				
			if (digitsThere == false) {
				System.out.println("Please enter a number in your address");
			// Check that the street address name is long enough
			} else if (customerStreetAddress.replaceAll("\\s+", "").length() < 4) {
				System.out.println("Please enter a full street address, (maybe include road or street)");
			} else {
				streetAddressGood = true;
			}
			
		}
		
		return customerStreetAddress;
	}
	
	// Method to get customer suburb
	public static String getCustomerSuburb(Scanner sc) {
		
		// Initialise variables
		boolean suburbGood = false;
		String customerSuburb = null;
		
		// While loop to ensure suburb is good
		while (!suburbGood) {
			suburbGood = true;
			boolean digitsSuburb = false;
			
			System.out.println("Enter customer suburb: ");
			customerSuburb = sc.nextLine();
			
			// Check that the suburb length is at least 5 characters (South African suburbs are at least 5)
			if (customerSuburb.replaceAll("\\s+", "").length() < 5) {
				System.out.println("That is not a valid suburb");
				suburbGood = false;
			} 
			
			// Check that there are no digits in the string
			for (char c: customerSuburb.toCharArray()) {
				if (Character.isDigit(c)) {
					digitsSuburb = true;
				}
			}
			
			if (digitsSuburb == true) {
				suburbGood = false;
				System.out.println("There are no digits in the names of South African suburbs.");
				System.out.println("Please enter a suburb not containing digits");
			} 
		}
		
		return customerSuburb;
	}
	
	// Method to get customer email address
	public static String getCustomerEmailAddress(Scanner sc) {
		
		// Initialise variables
		boolean emailGood = false;
		String customerEmailAddress = null;
		
		// While loop to validate email address
		while (!emailGood) {
			System.out.println("Enter your email address: ");
			customerEmailAddress = sc.nextLine();
			
			// Check for the @ symbol
			if (customerEmailAddress.indexOf("@") == -1) {
				System.out.println("Please enter a valid email address.");
				System.out.println("Format: identifier@emailhost.com");
			} else {
				String[] emailHalves = customerEmailAddress.split("@",2);
				
				// Check that there are characters besides @ and check that there is .
				if (emailHalves[0].equals("")) {
					System.out.println("Please enter a valid email address.");
					System.out.println("Format: identifier@emailhost.com");
				} else if (emailHalves[1].indexOf(".") == -1 || emailHalves[1].length() <= 1) {
					System.out.println("Please enter a valid email address.");
					System.out.println("Format: identifier@emailhost.com");
				} else {
					emailGood = true;
				}
			}
		}
		return customerEmailAddress;
	}
	
	// Method to find drivers who are in the same city as the restaurant
	// Method which finds drivers in the same city as the restaurant
	// Method to find drivers available for the customer
	public static ArrayList<Driver> findDriversForRestaurant(Restaurant restaurantObject, Driver[] driverArray) {
		// initialise available driversArrayList
		ArrayList<Driver> availableDrivers = new ArrayList<Driver>();
		
		// Check if Customer.city among driverArray
		for (int i = 0; i < driverArray.length; i++) {
			if (driverArray[i].city.equalsIgnoreCase(restaurantObject.city)) {
				availableDrivers.add(driverArray[i]);
			}
		}
		return availableDrivers;
	}

	//Method to find the driver with the lowest load from an ArrayList
	// Method which finds the driver with the lowest load in an array
	// Method to find the driver with the lowest load of deliveries
	public static Driver findDriverLowestLoad(ArrayList<Driver> availableDrivers) {
		
		// initialise Driver variable
		Driver driverToDeliver = availableDrivers.get(0);
		
		// For loop to find driver with lowest load
		for (int i = 1; i < availableDrivers.size(); i++) {
			if (availableDrivers.get(i).itemsCarried < driverToDeliver.itemsCarried) {
				driverToDeliver = availableDrivers.get(i);
			}
		}
		
		return driverToDeliver;
	}
	
	// Method creates an ArrayList items to add to the invoice
	// Creates String arrayList to add to the invoice
	// Method to create arrayList which will be used in the invoice
	public static ArrayList<String> makeFoodItemInvoiceArrayList(Order orderObject) {
		
		// initialise HashMap and String ArrayList
		ArrayList<String> foodItemsArrayList = new ArrayList<String>();
		HashMap<String, Integer> orderedItemsNumber = new HashMap<String, Integer>();
		HashMap<String, Double> orderedItemsPrice = new HashMap<String, Double>();
			
		// prepare HashMaps for invoice
		for (int i = 0; i < orderObject.foodOrdered.length; i++) {
			
			String foodName = orderObject.foodOrdered[i].name;
			double foodPrice = orderObject.foodOrdered[i].price;
			
			// Check if key exists and increments the value if it does
			if (orderedItemsNumber.containsKey(foodName)) {
				int numToIncrement = orderedItemsNumber.get(foodName);
				orderedItemsNumber.put(foodName, numToIncrement + 1);
			} else {
				orderedItemsNumber.put(foodName, 1);
			}
			
			if (!orderedItemsPrice.containsKey(foodName)) {
				orderedItemsPrice.put(foodName, foodPrice);
			}
		}
		
		// Create and add strings to arrayList
		for (Entry<String, Integer> entry : orderedItemsNumber.entrySet()) {
			String stringToAdd = String.format("%s X %s (%s)", entry.getValue(), entry.getKey(), orderedItemsPrice.get(entry.getKey()));
			foodItemsArrayList.add(stringToAdd);
		}
		// return arrayList
		return foodItemsArrayList;
	}

	// Method to generate an invoice if drivers are available to deliver
	// Invoice generator Method for if there are drivers in the area
	// Method to create an invoice if the drivers can deliver
	public static void yesDriversInvoiceGenerator(Order orderObject, Restaurant restaurantObject, Customer customerObject, Driver driverObject, String directory, ArrayList<String> foodItemsArrayList) {
		
		// Initialise variable
		double sumTotal = 0.00;
		
		// get total cost for invoice
		for (int i = 0; i < orderObject.foodOrdered.length; i++) {
			sumTotal = sumTotal + orderObject.foodOrdered[i].price;
		}
		
		
		// Write information to the file
		try {
			Formatter fileToWrite = new Formatter(directory);
			fileToWrite.format("%s %s %s", "Order number", orderObject.orderNumber, "\r\n");
			fileToWrite.format("%s %s %s", "Customer:", customerObject.name, "\r\n");
			fileToWrite.format("%s %s %s", "Email:", customerObject.emailAddress, "\r\n");
			fileToWrite.format("%s %s %s", "Phone number:", customerObject.contactNumber, "\r\n");
			fileToWrite.format("%s %s %s", "Location:", customerObject.city, "\r\n");
			fileToWrite.format("%s", "\r\n");
			fileToWrite.format("%s %s %s %s%s","You have ordered the following from", restaurantObject.name, "in", restaurantObject.city, ": \r\n");
			fileToWrite.format("%s", "\r\n");
			
			// For loop for the ordered items
			foodItemsArrayList.forEach((s) -> fileToWrite.format("%s %s", s, "\r\n")); 
			
			fileToWrite.format("%s", "\r\n");
			fileToWrite.format("%s %s %s", "Special instructions:", orderObject.specialInstruction, "\r\n");
			fileToWrite.format("%s", "\r\n");
			fileToWrite.format("%s %s %s", "Total:", sumTotal, "\r\n");
			fileToWrite.format("%s", "\r\n");
			fileToWrite.format("%s %s", driverObject.name, "is nearest to the restaurant and so they will be delivering your order to you at: \r\n");
			fileToWrite.format("%s", "\r\n");
			fileToWrite.format("%s %s", customerObject.streetAddress, "\r\n");
			fileToWrite.format("%s %s", customerObject.suburb, "\r\n");
			fileToWrite.format("%s", "\r\n");
			fileToWrite.format("%s %s%s", "If you need to contact the restaurant, their number is", restaurantObject.contactNumber, ".\r\n");
			
			// close file
			fileToWrite.close();
		} catch (Exception e) {
			System.out.println("An error has occurred, could not find directory for invoice.");
		}
	}
	
	// Method to created an invoice for no drivers being able to deliver
	// Method to generate a message if no drivers are available to deliver
	// Invoice generator Method for if there are no drivers in the area
	public static void noDriversInvoiceGenerator(String directory) {
		
		// Write information to the file
		try {
			Formatter fileToWrite = new Formatter(directory);
			fileToWrite.format("%s", "Sorry! Our drivers are too far away from you to be able to deliver to your location.");
			
			fileToWrite.close();
		} catch (Exception e) {
			System.out.println("An error has occurred, could not find directory for invoice.");
		}
	}
}