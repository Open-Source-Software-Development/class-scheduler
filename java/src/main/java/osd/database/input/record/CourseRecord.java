package osd.database.input.record;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "scheduler_course")
public class CourseRecord {

	@Id @GeneratedValue
	@Column(name = "id")
	private int id;
	
	@Column(name = "program")
	private String program;
	
	@Column(name = "title")
	private String title;

	@Column(name = "base_section_count")
    private int baseSectionCount;
	
	@Column(name = "ins_method")
	private String insMethod;
	
	@Column(name = "section_capacity")
	private int sectionCapacity;
	
	@Column(name = "style")
	private String style;
	
	@Column(name = "division_id")
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
	public String getName() {
		return this.program + "-" + this.style;
	}

    @SuppressWarnings("unused")
	public String getProgram() {
		return program;
	}

    @SuppressWarnings("unused")
	public void setProgram(String program) {
		this.program = program;
	}

    @SuppressWarnings("unused")
	public String getTitle() {
		return title;
	}

    @SuppressWarnings("unused")
	public void setTitle(String title) {
		this.title = title;
	}

    @SuppressWarnings("unused")
	public String getInsMethod() {
		return insMethod;
	}

    @SuppressWarnings("unused")
	public void setInsMethod(String insMethod) {
		this.insMethod = insMethod;
	}

    @SuppressWarnings("unused")
	public int getSectionCapacity() {
		return sectionCapacity;
	}

    @SuppressWarnings("unused")
	public void setSectionCapacity(Object sectionCapacity) {
		this.sectionCapacity = Integer.valueOf(sectionCapacity.toString());
	}

    @SuppressWarnings("unused")
	public String getStyle() {
		return style;
	}

    @SuppressWarnings("unused")
	public void setStyle(String style) {
		this.style = style;
	}

    @SuppressWarnings("unused")
	public int getDivisionId() {
		return divisionId;
	}

    @SuppressWarnings("unused")
	public void setDivisionId(Object divisionId) {
		this.divisionId = Integer.valueOf(divisionId.toString());
	}

    @SuppressWarnings("unused")
    public int getBaseSectionCount() {
        return baseSectionCount;
    }

    @SuppressWarnings("unused")
    public void setBaseSectionCount(Object baseSectionCount) {
        this.baseSectionCount = Integer.valueOf(baseSectionCount.toString());
    }

}
