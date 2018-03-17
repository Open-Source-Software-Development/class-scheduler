package osd.main;

import org.apache.commons.cli.*;

public class Flags {

    private final String databaseHost, databaseName, databaseUser, databasePass;

    private static final Options options = new Options();
    static {
        options.addRequiredOption("h", "dbHost", true, "database host URI");
        options.addRequiredOption("d", "dbName", true, "database name");
        options.addRequiredOption("u", "dbUser", true, "database username");
        options.addRequiredOption("p", "dbPass", true, "database password");
    }

    Flags(final String[] args) throws ParseException {
        final CommandLine commandLine = new DefaultParser().parse(options, args);
        this.databaseHost = commandLine.getOptionValue("dbHost");
        this.databaseName = commandLine.getOptionValue("dbName");
        this.databaseUser = commandLine.getOptionValue("dbUser");
        this.databasePass = commandLine.getOptionValue("dbPass");
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public String getDatabasePass() {
        return databasePass;
    }

}
