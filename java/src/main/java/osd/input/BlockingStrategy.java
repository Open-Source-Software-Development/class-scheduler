package osd.input;

import java.util.function.Function;
import java.util.stream.Stream;

enum BlockingStrategy implements Function<Block, Stream<Block>> {

    /**
     * A block and its paired block.
     */
    PAIR(b -> Stream.of(b, b.getPairedWith())),

    /**
     * A block and its next block.
     */
    SEQUENTIAL(b -> Stream.of(b, b.getNext())),

    ;

    private final Function<Block, Stream<Block>> function;

    BlockingStrategy(final Function<Block, Stream<Block>> function) {
        this.function = function;
    }

    @Override
    public Stream<Block> apply(final Block block) {
        return function.apply(block);
    }
}
