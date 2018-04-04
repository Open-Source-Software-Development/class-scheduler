package osd.database;

import javax.persistence.*;

@Entity
@Table(name="scheduler_professor")
class ProfessorRecord extends Record<Professor> {

	@Id @GeneratedValue
    @Column(name="id")
	private int id;

	@Column(name="first")
	private String firstName;

	@Column(name="last")
	private String lastName;

	@Column(name="division_id")
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
