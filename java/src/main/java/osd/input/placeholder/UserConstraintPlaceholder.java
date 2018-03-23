package osd.input.placeholder;

import osd.considerations.UserConstraint;

class UserConstraintPlaceholder extends UserConsiderationPlaceholder<UserConstraint> {

    private boolean isBlacklist;

    UserConstraintPlaceholder(String[] row, PlaceholderConfig config) {
        super(row, config);
    }

    @FromCSV(2)
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
    public UserConstraint get() {
        return new UserConstraint(left, right, isBlacklist);
    }
}
