package osd.database;

class ProfessorRecord extends Record<Professor> {

	private int id;
	private String firstName;
	private String lastName;
	private int divisionId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Object divisionId) {
		this.divisionId = Integer.valueOf(divisionId.toString());
	}

	@Override
	Professor create(final RecordAccession lookup) {
	    return new Professor(id, getName());
	}

	private String getName() {
		return firstName + " " + lastName;
	}

}
