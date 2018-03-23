package osd.database.placeholder;

import osd.considerations.UserConstraint;
import osd.database.UserConstraintRecord;

import java.util.Objects;

class UserConstraintPlaceholder extends UserConstraintRecord {

    private final PlaceholderConfig config;
    private Object left, right;
    private boolean isBlacklist;

    UserConstraintPlaceholder(String[] row, PlaceholderConfig config) {
        this.config = config;
        Placeholder.parse(this, row);
    }

    @Placeholder.FromCSV(0)
    void setLeft(final String name) {
        left = config.lookup(name);
    }

    @Placeholder.FromCSV(1)
    void setRight(final String name) {
        right = config.lookup(name);
    }

    @Placeholder.FromCSV(2)
    void setBlacklist(final String blacklist) {
        if (blacklist.equalsIgnoreCase("blacklist")) {
            isBlacklist = true;
        } else if (blacklist.equalsIgnoreCase("whitelist")) {
            isBlacklist = false;
        } else {
            throw new IllegalArgumentException("expected \"whitelist\" or \"blacklist\"");
        }
    }

    @Override
    public UserConstraint toUserConstraint() {
        return new UserConstraint(left, right, isBlacklist);
    }

}
