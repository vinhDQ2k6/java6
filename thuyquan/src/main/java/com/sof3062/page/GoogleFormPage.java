package com.sof3062.page;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GoogleFormPage {

  private final WebDriver driver;
  private final WebDriverWait wait;
  private final Random random;

  private final By questionContainer = By.cssSelector("div[role='listitem']");
  private final By radioOption = By.cssSelector("div[role='radio']");
  private final By checkboxOption = By.cssSelector("div[role='checkbox']");

  public GoogleFormPage(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    this.random = new Random();
  }

  public void open(String url) {
    driver.get(url);
  }

  public void fillForm() {
    wait.until(
      ExpectedConditions.visibilityOfElementLocated(questionContainer)
    );
    List<WebElement> questions = driver.findElements(questionContainer);

    for (WebElement question : questions) {
      scrollToElement(question);
      if (isRadioQuestion(question)) {
        handleRadioQuestion(question);
      } else if (isCheckboxQuestion(question)) {
        handleCheckboxQuestion(question);
      }
      sleep(5);
    }
  }

  private boolean isRadioQuestion(WebElement question) {
    return !question.findElements(radioOption).isEmpty();
  }

  private boolean isCheckboxQuestion(WebElement question) {
    return !question.findElements(checkboxOption).isEmpty();
  }

  private void handleRadioQuestion(WebElement question) {
    List<WebElement> options = getClickableOptions(question, radioOption);
    if (!options.isEmpty()) {
      clickElement(options.get(random.nextInt(options.size())));
    }
  }

  private void handleCheckboxQuestion(WebElement question) {
    List<WebElement> options = getClickableOptions(question, checkboxOption);
    for (WebElement option : options) {
      // 20-50% chance to select each option
      double probability = 0.2 + (0.3 * random.nextDouble());
      if (random.nextDouble() < probability) {
        if (!isChecked(option)) {
          clickElement(option);
        }
      }
    }
  }

  private List<WebElement> getClickableOptions(
    WebElement question,
    By locator
  ) {
    return question
      .findElements(locator)
      .stream()
      .filter(opt -> opt.isDisplayed() && isEnabled(opt))
      .collect(Collectors.toList());
  }

  private boolean isEnabled(WebElement element) {
    String ariaDisabled = element.getAttribute("aria-disabled");
    return ariaDisabled == null || !"true".equals(ariaDisabled);
  }

  private boolean isChecked(WebElement element) {
    String ariaChecked = element.getAttribute("aria-checked");
    return "true".equals(ariaChecked);
  }

  public void submit() {
    WebElement submitBtn = findSubmitButton();
    if (submitBtn != null) {
      scrollToElement(submitBtn);
      clickElement(submitBtn);
    } else {
      throw new RuntimeException("Submit button not found");
    }
  }

  private WebElement findSubmitButton() {
    String[] texts = { "Gửi", "Submit", "Tiếp", "Next" };
    for (String text : texts) {
      try {
        for (WebElement btn : driver.findElements(
          By.xpath(
            "//div[@role='button']//span[contains(text(), '" + text + "')]"
          )
        )) {
          if (btn.isDisplayed()) return btn;
        }
      } catch (Exception ignored) {}
    }

    List<WebElement> buttons = driver.findElements(
      By.cssSelector("div[role='button']")
    );
    for (int i = buttons.size() - 1; i >= 0; i--) {
      WebElement btn = buttons.get(i);
      if (btn.isDisplayed()) {
        String t = btn.getText();
        if (!t.contains("Xóa") && !t.contains("Clear")) return btn;
      }
    }
    return null;
  }

  private void scrollToElement(WebElement element) {
    ((JavascriptExecutor) driver).executeScript(
        "arguments[0].scrollIntoView({block: 'center'});",
        element
      );
  }

  private void clickElement(WebElement element) {
    try {
      element.click();
    } catch (Exception e) {
      ((JavascriptExecutor) driver).executeScript(
          "arguments[0].click();",
          element
        );
    }
  }

  private void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException ignored) {}
  }
}
