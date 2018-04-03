package osd.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "scheduler_course")
class CourseRecord implements Course {

	@Id @GeneratedValue
	@Column(name = "id")
	private int id;
	
	@Column(name = "program")
	private String program;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "ins_method")
	private String insMethod;
	
	@Column(name = "section_capacity")
	private int sectionCapacity;
	
	@Column(name = "style")
	private String style;
	
	@Column(name = "division_id")
	private int divisionId;

    @Override
    public Iterable<Section> getSections() {
        // You might want to return a java.util.ArrayList<Section>, for example.
        // TODO: implement this
        throw new UnsupportedOperationException("not yet implemented");
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.program + "-" + this.style;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInsMethod() {
		return insMethod;
	}

	public void setInsMethod(String insMethod) {
		this.insMethod = insMethod;
	}

	public int getSectionCapacity() {
		return sectionCapacity;
	}

	public void setSectionCapacity(Object sectionCapacity) {
		this.sectionCapacity = Integer.valueOf(sectionCapacity.toString());
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public int getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Object divisionId) {
		this.divisionId = Integer.valueOf(divisionId.toString());
	}

}
