package demo;

import dagger.Component;
import schedule.SchedulingAttempt;

@Component(modules = DemoModule.class)
public interface Demo {

    SchedulingAttempt attempt();

    static void main(final String[] args) {
        System.out.println(DaggerDemo.create().attempt().call());
    }

}
