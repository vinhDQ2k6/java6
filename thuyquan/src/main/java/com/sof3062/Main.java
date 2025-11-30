package com.sof3062;

import com.sof3062.config.AutomationConfig;
import com.sof3062.driver.DriverManager;
import com.sof3062.service.FormFillerService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;

public class Main {

  public static void main(String[] args) {
    int threadCount = AutomationConfig.THREAD_COUNT;
    int totalSubmissions = AutomationConfig.SUBMISSION_COUNT;
    int submissionsPerThread = totalSubmissions / threadCount;

    ExecutorService executor = Executors.newFixedThreadPool(threadCount);

    System.out.println(
      "Starting automation with " +
      threadCount +
      " threads. Total submissions: " +
      totalSubmissions
    );

    for (int i = 0; i < threadCount; i++) {
      int threadId = i + 1;
      // Distribute remainder to the first few threads if any
      int count =
        submissionsPerThread + (i < (totalSubmissions % threadCount) ? 1 : 0);

      executor.submit(() -> {
        System.out.println(
          "Thread " +
          threadId +
          " started. Will perform " +
          count +
          " submissions."
        );
        WebDriver driver = null;
        try {
          driver = DriverManager.createDriver();
          FormFillerService service = new FormFillerService(driver);
          service.executeAutomation(count);
        } catch (Exception e) {
          System.err.println(
            "Thread " + threadId + " encountered an error: " + e.getMessage()
          );
          e.printStackTrace();
        } finally {
          if (driver != null) {
            driver.quit();
          }
          System.out.println("Thread " + threadId + " finished.");
        }
      });
    }

    executor.shutdown();
    try {
      // Wait for all tasks to finish (or a very long time)
      if (!executor.awaitTermination(2, TimeUnit.HOURS)) {
        executor.shutdownNow();
      }
    } catch (InterruptedException e) {
      executor.shutdownNow();
    }
    System.out.println("All automation tasks completed.");
  }
}
