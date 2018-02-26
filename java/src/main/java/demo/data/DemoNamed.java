package demo.data;

import osd.input.Named;

interface DemoNamed extends Named {

    @Override
    default String getName() {
        return toString();
    }

}
