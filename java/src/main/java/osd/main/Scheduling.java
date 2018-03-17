package osd.main;

import dagger.Component;
import org.apache.commons.cli.ParseException;
import osd.input.placeholder.PlaceholderModule;
import osd.schedule.SchedulingAttempt;

import java.io.File;

@Component(modules={FlagModule.class, PlaceholderModule.class})
public interface Scheduling {

    SchedulingAttempt schedulingAttempt();

    static void main(final String[] args) throws ParseException {
        Scheduling scheduling = DaggerScheduling.builder()
                .flagModule(new FlagModule(args))
                .build();
        System.out.println(scheduling.schedulingAttempt().call());
    }

}
