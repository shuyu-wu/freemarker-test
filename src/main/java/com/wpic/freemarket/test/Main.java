package com.wpic.freemarket.test;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import org.apache.commons.cli.*;

import java.io.File;
import java.util.Locale;

import static io.undertow.Handlers.resource;

/**
 * Created by hamed on 1/5/17.
 */
public class Main {

    public static void main(final String[] args) throws ParseException {
        // create Options object
        final Options options = new Options();

        // add t option
        options.addOption("h", "help", false, "show help");
        options.addOption("p", "port", true, "HTTP Port (default 8080)");
        options.addOption("H", "host", true, "HTTP Port (default localhost)");
        options.addOption("l", "host", true, "Locale setting (default is your system default. ex: en_US)");

        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption('h')) {
            final HelpFormatter formater = new HelpFormatter();
            formater.printHelp("Main", options);
            System.out.println("");
            System.out.println("Put freemarker templates (*.ftl) and data models (*.json) in templates folders and test it with your browsers.");
            System.out.println("Example: put sample.ftl and sample.json and test it on http://localhost:8080/sample.ftl");
            System.exit(0);
        }

        final int port = Integer.parseInt(cmd.getOptionValue("p", "8080"));
        final String host = cmd.getOptionValue("h", "localhost");
        final String locate = cmd.getOptionValue("locate");

        final String language;
        final String country;
        if (locate != null) {
            final String[] parts = locate.split("_");
            if (parts.length != 2 || !parts[0].matches("[a-z]{2}") || !parts[1].matches("[A-Z]{2}")) {
                System.out.println("Illegal locale format: " + locate);
                return;
            } else {
                language = parts[0];
                country = parts[1];
            }
        } else {
            language = Locale.getDefault().getLanguage();
            country = Locale.getDefault().getCountry();
        }

        final PathHandler handler = Handlers.path();

        final File[] statics = new File("static").listFiles();
        for (File s:statics) {
            if (s.isDirectory()) {
                final FileResourceManager resourceManager = new FileResourceManager(s, 1000);
                final ResourceHandler resourceHandler = resource(resourceManager);
                handler.addPrefixPath(s.getName(), resourceHandler);
            }
        }

        handler.addPrefixPath("/", new TemplateHandler(language, country));

        final Undertow server = Undertow.builder()
                .addHttpListener(port, host)
                .setHandler(handler).build();
        server.start();
    }

}
