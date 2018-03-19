package osd.main;

import dagger.Component;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.apache.commons.cli.ParseException;
import osd.considerations.ConsiderationModule;
import osd.input.placeholder.PlaceholderModule;
import osd.schedule.ScheduleModule;
import osd.schedule.Scheduler;

import java.util.stream.Collectors;

@Component(modules={ScheduleModule.class, FlagModule.class, PlaceholderModule.class})
public interface Scheduling {

    Scheduler schedulingAttempt();

    static void main(final String[] args) throws ParseException {
        Scheduling scheduling = DaggerScheduling.builder()
                .flagModule(new FlagModule(args))
                .considerationModule(new ConsiderationModule(FastClasspathScanner::new))
                .build();
        scheduling.schedulingAttempt().getResults().allHunks().forEach(System.out::println);
    }

}
