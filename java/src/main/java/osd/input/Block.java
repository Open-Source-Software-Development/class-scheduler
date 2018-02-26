package osd.input;

public interface Block extends Named {

    Block getNext();

    Block getPrevious();

    Block getPairedWith();

}
