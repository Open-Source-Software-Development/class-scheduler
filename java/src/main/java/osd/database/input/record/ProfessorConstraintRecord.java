package osd.database.input.record;

import javax.persistence.*;

@Entity
@Table(name = "scheduler_professorconstraint")
public class ProfessorConstraintRecord {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "professor_id")
    private int professorId;

    @Column(name = "block_id")
    private int blockId;

    @Column(name = "value")
    private int value;

    @SuppressWarnings("unused")
    public int getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(int id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public int getProfessorId() {
        return professorId;
    }

    @SuppressWarnings("unused")
    public void setProfessorId(int professorId) {
        this.professorId = professorId;
    }

    @SuppressWarnings("unused")
    public int getBlockId() {
        return blockId;
    }

    @SuppressWarnings("unused")
    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    @SuppressWarnings("unused")
    public int getValue() {
        return value;
    }

    @SuppressWarnings("unused")
    public void setValue(int value) {
        this.value = value;
    }
}

