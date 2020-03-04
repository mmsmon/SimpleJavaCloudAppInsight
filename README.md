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


# 
