package osd.database;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "scheduler_block")
class BlockRecord implements Block {

    // TODO: add column fields, getters, and setters here
    // see https://www.tutorialspoint.com/hibernate/hibernate_annotations.htm

    @Override
    public String getName() {
        // TODO: implement this
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public Block getNext() {
        // TODO: implement this
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public Block getPrevious() {
        // TODO: implement this
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public Block getPairedWith() {
        // TODO: implement this
        throw new UnsupportedOperationException("not yet implemented");
    }

}
