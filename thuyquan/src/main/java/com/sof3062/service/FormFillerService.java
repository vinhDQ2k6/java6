package com.sof3062.service;

import com.sof3062.config.AutomationConfig;
import com.sof3062.page.GoogleFormPage;
import org.openqa.selenium.WebDriver;

public class FormFillerService {

  private final GoogleFormPage formPage;

  public FormFillerService(WebDriver driver) {
    this.formPage = new GoogleFormPage(driver);
  }

  public void executeAutomation(int count) {
    for (int i = 0; i < count; i++) {
      System.out.println("Starting submission " + (i + 1) + " of " + count);
      try {
        formPage.open(AutomationConfig.FORM_URL);
        formPage.fillForm();
        formPage.submit();
        System.out.println("Submission " + (i + 1) + " completed.");

        // Wait a bit before next submission
        Thread.sleep(100);
      } catch (Exception e) {
        System.err.println(
          "Error during submission " + (i + 1) + ": " + e.getMessage()
        );
        e.printStackTrace();
      }
    }
  }
}
