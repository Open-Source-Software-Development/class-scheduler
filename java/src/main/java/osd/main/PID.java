package osd.main;

import javax.inject.Inject;

class PID {

    @Inject
    PID() {}

    int pid() {
        // Taken from https://stackoverflow.com/a/12066696/8560257
        try {
            final java.lang.management.RuntimeMXBean runtime =
                    java.lang.management.ManagementFactory.getRuntimeMXBean();
            final java.lang.reflect.Field jvm = runtime.getClass().getDeclaredField("jvm");
            jvm.setAccessible(true);
            final sun.management.VMManagement management =
                    (sun.management.VMManagement) jvm.get(runtime);
            final java.lang.reflect.Method pid_method =
                    management.getClass().getDeclaredMethod("getProcessId");
            pid_method.setAccessible(true);
            return (Integer)pid_method.invoke(management);
        } catch (final ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}
