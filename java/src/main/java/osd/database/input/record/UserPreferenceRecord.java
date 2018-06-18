package osd.database.input.record;

import javax.persistence.*;

@Entity
@Table(name = "scheduler_userpreference")
public class UserPreferenceRecord implements UserConsiderationRecord {

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

    @Column(name="score")
    private int score;

    @SuppressWarnings("unused")
    public int getScore() {
        return score;
    }

    @SuppressWarnings("unused")
    public void setScore(Object score) {
        this.score = Integer.valueOf(score.toString());
    }

    @SuppressWarnings("unused")
    public int getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getLeftTypeId() {
        return leftTypeId;
    }

    @SuppressWarnings("unused")
    public void setLeftTypeId(int leftTypeId) {
        this.leftTypeId = leftTypeId;
    }

    @Override
    public int getRightTypeId() {
        return rightTypeId;
    }

    @SuppressWarnings("unused")
    public void setRightTypeId(int rightTypeId) {
        this.rightTypeId = rightTypeId;
    }

    @Override
    public int getLeftId() {
        return leftId;
    }

    @SuppressWarnings("unused")
    public void setLeftId(int leftId) {
        this.leftId = leftId;
    }

    @Override
    public int getRightId() {
        return rightId;
    }

    @SuppressWarnings("unused")
    public void setRightId(int rightId) {
        this.rightId = rightId;
    }

    @SuppressWarnings("unused")
    public void setScore(int score) {
        this.score = score;
    }
}
