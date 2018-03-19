package osd.main;

import dagger.Component;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.apache.commons.cli.ParseException;
import osd.considerations.BaseConsiderationModule;
import osd.input.placeholder.PlaceholderModule;
import osd.schedule.ScheduleModule;
import osd.schedule.Scheduler;

@Component(modules={ScheduleModule.class, FlagModule.class, PlaceholderModule.class})
public interface Scheduling {

    Scheduler schedulingAttempt();

    static void main(final String[] args) throws ParseException {
        Scheduling scheduling = DaggerScheduling.builder()
                .flagModule(new FlagModule(args))
                .baseConsiderationModule(new BaseConsiderationModule(FastClasspathScanner::new))
                .build();
        scheduling.schedulingAttempt().getResults().getHunks().forEach(System.out::println);
    }

}
