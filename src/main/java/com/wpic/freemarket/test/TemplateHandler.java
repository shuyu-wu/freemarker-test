package com.wpic.freemarket.test;

import com.google.gson.Gson;
import freemarker.template.Configuration;
import freemarker.template.Template;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;

import java.io.*;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by hamed on 1/5/17.
 */
public class TemplateHandler implements HttpHandler {

    private final String language;

    private final String country;

    public TemplateHandler(final String language, final String country) {
        this.language = language;
        this.country = country;
    }

    public void handleRequest(HttpServerExchange exchange) throws Exception {
        final String uri = exchange.getRequestURI();

        if (uri.endsWith(".ftl")) {
            final String path = uri.substring(1);

            final Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
            cfg.setDirectoryForTemplateLoading(new File("templates"));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setLocale(new Locale(language, country));

            final Template template = cfg.getTemplate(path);

            final File dataFile = new File("templates", path.replace(".ftl", ".json"));
            try (Reader r = new InputStreamReader(new FileInputStream(dataFile), "UTF-8")) {
                final Gson gson = new Gson();
                final HashMap m = gson.fromJson(r, HashMap.class);

                final StringWriter w = new StringWriter();
                template.process(m, w);

                exchange.setStatusCode(StatusCodes.OK).getResponseSender().send(w.toString());
            }

        }
    }

}
