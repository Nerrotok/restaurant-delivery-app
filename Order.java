
public class Order {
	
	// Order class attributes
	FoodItem[] foodOrdered;
	String orderNumber;
	String specialInstruction;
	
	// Order class constructor
	public Order(FoodItem[] foodOrdered, String orderNumber, String specialInstruction) {
		this.foodOrdered = foodOrdered;
		this.orderNumber = orderNumber;
		this.specialInstruction = specialInstruction;
		
	}
}
