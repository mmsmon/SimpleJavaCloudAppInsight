package com.swift.cloudapp;

import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.internal.config.ApplicationInsightsXmlConfiguration;
import com.microsoft.applicationinsights.log4j.v2.ApplicationInsightsAppender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class SimpleCloudApp {
    public static void main(String[] args) throws InterruptedException {
        TelemetryClient tc = new TelemetryClient();
        System.out.println(tc.getContext());

        Map<String, Double> m = new HashMap<>();
        m.put("ExampleMetric", 1.0);
        tc.trackEvent("ExampleEvent",null, m);
        tc.trackTrace("This is first message from Cloud Application as trace");

        Logger log = LogManager.getRootLogger();
        log.info("First Message from CloudApp 3");
        tc.flush();
        System.out.println("Hello");
        Thread.sleep(1000 * 60 * 6);
    }
}
