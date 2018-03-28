package hibernateTest2;

public class Professor {
	private String firstName;
	private String lastName;
	private	String division;
	private int id;
	
	public Professor() {}
	public Professor(String fName, String lName, String div){
		this.firstName = fName;
		this.lastName = lName;
		this.division = div;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
