package osd.database;

import osd.considerations.UserPreference;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "scheduler_userpreference")
public class UserPreferenceRecord extends UserConsiderationRecord<UserPreference> {

    private int score;

    @Override
    UserPreference createImpl(Object left, Object right) {
        return new UserPreference(left, right, score);
    }

    public int getScore() {
        return score;
    }

    public void setScore(Object score) {
        this.score = Integer.valueOf(score.toString());
    }

    // metamprogramming hack
    @Override
    UserPreference create(final RecordAccession accession) {
        return super.create(accession);
    }

}
