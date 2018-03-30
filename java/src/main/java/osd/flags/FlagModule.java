package osd.flags;

import dagger.Module;
import dagger.Provides;
import org.apache.commons.cli.ParseException;

@Module
public class FlagModule {

    private final Flags flags;

    public FlagModule(final String[] args) throws ParseException {
        this.flags = new Flags(args);
    }

    @Provides
    Flags providesFlags() {
        return flags;
    }

}
