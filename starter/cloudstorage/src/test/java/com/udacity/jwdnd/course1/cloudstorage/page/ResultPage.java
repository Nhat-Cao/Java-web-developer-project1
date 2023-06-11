package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ResultPage {
    @FindBy(css = "#success-back")
    private WebElement successBack;
    @FindBy(css = "#error-back")
    private WebElement errorBack;

    public ResultPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void clickSuccessBack() {
        this.successBack.click();
    }

    public void clickErrorBack() {
        this.errorBack.click();
    }
}
