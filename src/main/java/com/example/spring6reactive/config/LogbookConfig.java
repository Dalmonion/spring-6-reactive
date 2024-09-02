package com.example.spring6reactive.config;

import org.zalando.logbook.HttpLogFormatter;
import org.zalando.logbook.Sink;
import org.zalando.logbook.json.JsonHttpLogFormatter;
import org.zalando.logbook.logstash.LogstashLogbackSink;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogbookConfig {

    @Bean
    public Sink LogbookLogStash() {
        HttpLogFormatter formatter = new JsonHttpLogFormatter();
        LogstashLogbackSink sink = new LogstashLogbackSink(formatter);
        return sink;
    }
}
