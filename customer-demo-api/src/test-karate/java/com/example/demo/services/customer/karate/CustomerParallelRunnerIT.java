package com.example.demo.services.customer.karate;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import lombok.extern.slf4j.Slf4j;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
//@CucumberOptions(features = "classpath:com/example/demo/services/customer/karate/features/customers_orchestrator.feature")
class CustomerParallelRunnerIT {

    private static final int THREAD_COUNT = 5;
    private static final String REPORT_DIRECTORY = "target/karate-reports";


    @BeforeAll
    static void beforeAllTests() {
        log.info("Before all karate tests");
    }

    @AfterAll
    static void afterAllTests() {
        log.info("After all karate tests");
    }

    private static String[] getFeatureFilePath() {
        final String packageClassPath = getPackageClassPath();

        final String[] featureFilePath =
                new String[]{
                        /*packageClassPath + "/features/customers_orchestrator.feature",*/
                        packageClassPath + "/features/customers_create.feature",
                        packageClassPath + "/features/customers_read.feature",
                        packageClassPath + "/features/customers_update.feature",
                        packageClassPath + "/features/customers_delete.feature"
                };

        log.info("Feature file path: {}", featureFilePath.length);

        return featureFilePath;
    }

    private static String getPackageClassPath() {
        final String absoluteResourcePath = CustomerParallelRunnerIT.class.getResource("").getPath();
        log.info("Absolute resource path: " + absoluteResourcePath);

        final String relativeResourcePath = CustomerParallelRunnerIT.class.getClassLoader().getResource("").getPath();
        log.info("Relative resource path: " + relativeResourcePath);

        final String packageClassPath = "classpath:" + absoluteResourcePath.substring(relativeResourcePath.length());

        log.info("Package class path: {}", packageClassPath);

        return packageClassPath;
    }

    private static void generateReport(final String karateReportDirectoryPath) {
        Collection<File> jsonFiles = FileUtils.listFiles(new File(karateReportDirectoryPath), new String[]{"json"}, true);
        List<String> jsonPaths = new ArrayList(jsonFiles.size());
        jsonFiles.forEach(file -> jsonPaths.add(file.getAbsolutePath()));
        Configuration config = new Configuration(new File(REPORT_DIRECTORY), "karate");
        ReportBuilder reportBuilder = new ReportBuilder(jsonPaths, config);
        reportBuilder.generateReports();
    }

    @Test
    void testParallel() {
        Results results = Runner
                .path(getFeatureFilePath())
                /*.tags("@Success,@Error,~@ignore")*/
                .tags("~@ignore")
                .reportDir(REPORT_DIRECTORY)
                .parallel(THREAD_COUNT);

        generateReport(results.getReportDir());

        assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }

    /*@Test
    void testSuccessScenariosParallel() {
        Results results = Runner
                .path(getPackageClassPath())
                .tags("@Success")
                .parallel(THREAD_COUNT);

        generateReport(results.getReportDir());

        assertEquals(0, results.getFailCount(), results.getErrorMessages());
        assertEquals(0, results.getFailCount(), results.getFailureReason().getMessage());
    }

    @Test
    void testErrorScenariosParallel() {
        Results results = Runner
                .path(getPackageClassPath())
                .tags("@Error")
                .parallel(THREAD_COUNT);

        generateReport(results.getReportDir());

        assertEquals(0, results.getFailCount(), results.getErrorMessages());
        assertEquals(0, results.getFailCount(), results.getFailureReason().getMessage());
    }*/

}
