package ru.otus.service;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class TemplateProcessorImpl implements TemplateProcessor {
    private final Configuration configuration;

    public TemplateProcessorImpl(String templatesDir) {
        this.configuration = new Configuration(Configuration.VERSION_2_3_30);
        configuration.setClassForTemplateLoading(getClass(), templatesDir);
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
    }

    @Override
    public String getPage(String filename, Map<String, Object> data) throws IOException {
        try (var writer = new StringWriter()) {
            var template = configuration.getTemplate(filename);
            template.process(data, writer);
            return writer.toString();
        } catch (TemplateException e) {
            throw new IOException(e);
        }
    }
}
