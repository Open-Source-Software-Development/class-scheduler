package osd.database.input.record;

import javax.persistence.*;

@Entity
@Table(name = "scheduler_professorconstraint")
public class ProfessorConstraintRecord implements Record {

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

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProfessorId() {
        return professorId;
    }

    public void setProfessorId(int professorId) {
        this.professorId = professorId;
    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

