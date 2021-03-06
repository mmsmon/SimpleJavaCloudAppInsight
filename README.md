# SimpleJavaCloudAppInsight
Simple Java Cloud Application with Azure Application Insight

Example to configure Java application to Microsoft Azure Application Insight

## Create a Azure Application Insight Resource

Go to Azure portal and create an application insight resource. Make sure to copy the instrumentation key from the overview page.

## Create a Maven project

```xml
<groupId>com.example.cloudapp</groupId>
<artifactId>CloudJavaApplication</artifactId>
<version>1.0-SNAPSHOT</version>
```
## Add pom.xml dependency 

```xml
<dependency>
    <groupId>com.microsoft.azure</groupId>
    <artifactId>applicationinsights-core</artifactId>
    <version>2.5.1</version>
</dependency>
```


## Add ApplicationInsights.xml file in resources directory

```xml
<?xml version="1.0" encoding="utf-8"?>
<ApplicationInsights xmlns="http://schemas.microsoft.com/ApplicationInsights/2013/Settings" schemaVersion="2014-05-30">

    <!-- The key from the portal: -->
    <InstrumentationKey>[Instrumentation Key from Application Insight Resource page]</InstrumentationKey>

    <Channel type="com.microsoft.applicationinsights.channel.concrete.inprocess.InProcessTelemetryChannel">
        <DeveloperMode>false</DeveloperMode>
        <FlushIntervalInSeconds>5</FlushIntervalInSeconds><!-- must be between [1, 500]. values outside the bound will be rounded to nearest bound -->
        <MaxInstantRetry>3</MaxInstantRetry><!-- must be between [0, 10] -->
        <MaxTelemetryBufferCapacity>500</MaxTelemetryBufferCapacity><!-- units=number of telemetry items; must be between [1, 1000] -->
        <MaxTransmissionStorageFilesCapacityInMB>10</MaxTransmissionStorageFilesCapacityInMB><!-- must be between [1, 1000] -->
        <Throttling>true</Throttling>
    </Channel>

    <ContextInitializers>
        <Add type="DeviceInfoContextInitializer" />
        <Add type="SdkVersionContextInitializer" />
    </ContextInitializers>
    <RoleName>SimpleCloudApp</RoleName>
    <PerformanceCounters collectionFrequencyInSec="60">
        <!--
        Built-in performance counters include:
            * Everything in the <Jvm /> section
            * These system performance counters (collected on both Linux and Windows):
                - category=Process, name=Private Bytes, instance="__SELF__"
                - category=Process, name=% Processor Time, instance="__SELF__"
                - category=Processor, name=% Processor Time, instance="_Total"
                - category=Process, name=IO Data Bytes/sec, instance="__SELF__"
                - category=Memory, name=Available Bytes, instance=""
        -->
        <UseBuiltIn>true</UseBuiltIn>
        <!--
        This controls the JvmPerformanceCountersModule.
        It can be disabled, or each individual component can be disabled.
        This is optional; the example below shows the defaults.
        -->
        <Jvm enabled="true">
            <JvmPC name="GC" enabled="true" />
            <JvmPC name="MemoryUsage" enabled="true" />
            <JvmPC name="ThreadDeadLockDetector" enabled="true" />
        </Jvm>

        <!--
        Below, you'll find some example JMX metrics.
        There are two supported JMX metric attribute types: REGULAR (the default) and COMPOSITE.
            COMPOSITE attributes are of the form 'X.y'.
        -->
        <Jmx>
            <Add displayName="Thread Count"
                 objectName="java.lang:type=Threading"
                 attribute="ThreadCount" />
            <Add displayName="Used Heap Memory"
                 objectName="java.lang:type=Memory"
                 attribute="HeapMemoryUsage.used"
                 type="COMPOSITE" />
        </Jmx>
    </PerformanceCounters>
</ApplicationInsights>

```

## Add log4j file in resources directory

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<Configuration packages="com.microsoft.applicationinsights.log4j.v2">
    <Properties>
        <Property name="LOG_PATTERN">
            %d{yyyy-MM-dd HH:mm:ss.SSS} %5p ${hostName} --- [%15.15t] %-40.40c{1.} : %m%n%ex
        </Property>
    </Properties>
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
        </Console>
        <ApplicationInsightsAppender name="simpleCloudApp" instrumentationKey="[Instrumentation Key from Application Insight Resource page]">
        </ApplicationInsightsAppender>
    </Appenders>
    <Loggers>
        <Root level="trace">
            <AppenderRef ref="Console"  />
            <AppenderRef ref="simpleCloudApp"  />
        </Root>
    </Loggers>
</Configuration>
```

## Create a jar file

mvn package 

## Execute the program 

java -jar target/SimpleJavaCloudAppInsight-1.0-SNAPSHOT.jar


