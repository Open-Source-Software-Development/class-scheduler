package osd.database.input.record;

import javax.persistence.*;

@Entity
@Table(name="scheduler_professor")
public class ProfessorRecord {

	@Id @GeneratedValue
    @Column(name="id")
	private int id;

	@Column(name="first")
	private String firstName;

	@Column(name="last")
	private String lastName;

	@Column(name="division_id")
	private int divisionId;

	@SuppressWarnings("unused")
	public int getId() {
		return id;
	}

	@SuppressWarnings("unused")
	public void setId(int id) {
		this.id = id;
	}

	@SuppressWarnings("unused")
	public String getFirstName() {
		return firstName;
	}

	@SuppressWarnings("unused")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@SuppressWarnings("unused")
	public String getLastName() {
		return lastName;
	}

	@SuppressWarnings("unused")
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@SuppressWarnings("unused")
	public int getDivisionId() {
		return divisionId;
	}

	@SuppressWarnings("unused")
	public void setDivisionId(Object divisionId) {
		this.divisionId = Integer.valueOf(divisionId.toString());
	}

}
