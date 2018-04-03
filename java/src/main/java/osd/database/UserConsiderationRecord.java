package osd.database;

public abstract class UserConsiderationRecord<T> extends Record<T> {

    private int id;
    private int leftTypeId;
    private int rightTypeId;
    private int leftId;
    private int rightId;

    @Override
    String getName() {
        return "Constraint" + id;
    }

    @Override
    T create(final RecordAccession lookup) {
        final Object leftObject = lookup.get(leftTypeId, leftId);
        final Object rightObject = lookup.get(rightTypeId, rightId);
        return createImpl(leftObject, rightObject);
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLeftTypeId() {
        return leftTypeId;
    }

    public void setLeftTypeId(int leftTypeId) {
        this.leftTypeId = leftTypeId;
    }

    public int getRightTypeId() {
        return rightTypeId;
    }

    public void setRightTypeId(int rightTypeId) {
        this.rightTypeId = rightTypeId;
    }

    public int getLeftId() {
        return leftId;
    }

    public void setLeftId(int leftId) {
        this.leftId = leftId;
    }

    public int getRightId() {
        return rightId;
    }

    public void setRightId(int rightId) {
        this.rightId = rightId;
    }

    abstract T createImpl(final Object left, final Object right);

}
