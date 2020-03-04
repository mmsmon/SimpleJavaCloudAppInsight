package com.example.cloudapp;

import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.internal.config.ApplicationInsightsXmlConfiguration;
import com.microsoft.applicationinsights.log4j.v2.ApplicationInsightsAppender;
import com.microsoft.applicationinsights.telemetry.Duration;
import com.microsoft.applicationinsights.telemetry.RemoteDependencyTelemetry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SimpleCloudApp {
    public static double getRandomDoubleBetweenRange(double min, double max){
        double x = (Math.random()*((max-min)+1))+min;
        return x;
    }

    public static long getRandomIntBetweenRange(long min, long max){
        double x = (Math.random()*((max-min)+1))+min;
        long i = Math.round(x);
        return i;
    }

    public static void main(String[] args) throws InterruptedException {

        int loop = 100;
        TelemetryClient tc = new TelemetryClient();

        for (int i=0; i < loop; i++) {

            Map<String, Double> m = new HashMap<>();
            m.put("ExampleMetric1", getRandomDoubleBetweenRange(1.0, 99.0));
            m.put("ExampleMetric2", getRandomDoubleBetweenRange(1.0, 99.0));
            m.put("ExampleMetric3", getRandomDoubleBetweenRange(1.0, 99.0));
            m.put("ExampleMetric4", getRandomDoubleBetweenRange(1.0, 99.0));
            tc.trackEvent("ExampleEvent", null, m);

            tc.trackTrace("This is first message from Cloud Application as trace");

            long start = System.currentTimeMillis();
            int rem = i % 5;

            try {
                Thread.sleep(getRandomIntBetweenRange(1,200));
                if (rem == 0)
                    throw new Exception("This is exception on value - " + Integer.toString(i));
            } catch (InterruptedException e) {
                tc.trackException(e);

                long end = System.currentTimeMillis();
                //track a custom dependency
                RemoteDependencyTelemetry remoteDependencyTelemetry =
                        new RemoteDependencyTelemetry(Integer.toString(rem) + "--storage", "blob:get", new Duration(end - start), false);
                remoteDependencyTelemetry.setType("blob");

                tc.trackDependency(remoteDependencyTelemetry);
            } catch (Exception e) {
                tc.trackException(e);

                long end = System.currentTimeMillis();
                //track a custom dependency
                RemoteDependencyTelemetry remoteDependencyTelemetry =
                        new RemoteDependencyTelemetry(Integer.toString(rem) + "--storage", "blob:get", new Duration(end - start), false);
                remoteDependencyTelemetry.setType("blob");

                tc.trackDependency(remoteDependencyTelemetry);
            } finally {
                long end = System.currentTimeMillis();
                //track a custom dependency
                RemoteDependencyTelemetry remoteDependencyTelemetry =
                        new RemoteDependencyTelemetry(Integer.toString(rem) + "--storage", "blob:get", new Duration(end - start), true);
                remoteDependencyTelemetry.setType("blob");

                tc.trackDependency(remoteDependencyTelemetry);
            }

            Logger log = LogManager.getRootLogger();
            log.info("Log Message from CloudApp");

            // Sleeping so that it can send JMX performance  metrics
            Thread.sleep(2000);
        }
    }
}
