package osd.database.input.record;

import javax.persistence.*;

@Entity
@Table(name = "scheduler_userconstraint")
public class UserConstraintRecord implements UserConsiderationRecord {

    @Id @GeneratedValue
    @Column(name="id")
    private int id;

    @Column(name="left_argument_type_id")
    private int leftTypeId;

    @Column(name="right_argument_type_id")
    private int rightTypeId;

    @Column(name="left_argument_id")
    private int leftId;

    @Column(name="right_argument_id")
    private int rightId;

    @Column(name="is_blacklist")
    private boolean isBlacklist;

    public boolean getIsBlacklist() {
        return isBlacklist;
    }

    public void setIsBlacklist(Object blacklist) {
        isBlacklist = Boolean.valueOf(blacklist.toString());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getLeftTypeId() {
        return leftTypeId;
    }

    public void setLeftTypeId(int leftTypeId) {
        this.leftTypeId = leftTypeId;
    }

    @Override
    public int getRightTypeId() {
        return rightTypeId;
    }

    public void setRightTypeId(int rightTypeId) {
        this.rightTypeId = rightTypeId;
    }

    @Override
    public int getLeftId() {
        return leftId;
    }

    public void setLeftId(int leftId) {
        this.leftId = leftId;
    }

    @Override
    public int getRightId() {
        return rightId;
    }

    public void setRightId(int rightId) {
        this.rightId = rightId;
    }

}
