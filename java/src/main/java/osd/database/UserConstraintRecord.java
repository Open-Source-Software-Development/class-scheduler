package osd.database;

import osd.considerations.UserConstraint;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "scheduler_userconstraint")
public class UserConstraintRecord extends UserConsiderationRecord<UserConstraint> {

    private boolean isBlacklist;

    @Override
    UserConstraint createImpl(final Object leftObject, final Object rightObject) {
        return new UserConstraint(leftObject, rightObject, isBlacklist);
    }

    public boolean getIsBlacklist() {
        return isBlacklist;
    }

    public void setIsBlacklist(boolean blacklist) {
        isBlacklist = blacklist;
    }

    // metaprogramming hack
    @Override
    UserConstraint create(final RecordAccession accession) {
        return super.create(accession);
    }
}
