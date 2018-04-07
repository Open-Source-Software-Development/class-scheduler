package osd.util.pair;

class ImmutablePairImpl<L, R> extends AbstractPair<L, R> implements ImmutablePair<L, R> {

    private final L left;
    private final R right;

    ImmutablePairImpl(final L left, final R right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public L left() {
        return left;
    }

    @Override
    public R right() {
        return right;
    }

}
