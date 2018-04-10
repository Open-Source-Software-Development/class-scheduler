package osd.database.input.record;

import osd.database.Identified;

public interface UserConsiderationRecord extends Identified {

    int getLeftTypeId();

    int getRightTypeId();

    int getLeftId();

    int getRightId();

}
