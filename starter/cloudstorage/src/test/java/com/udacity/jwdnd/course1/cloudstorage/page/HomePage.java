package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class HomePage {
    @FindBy(css = "#logoutDiv button")
    public WebElement logoutBtn;
    @FindBy(css = "#nav-notes-tab")
    public WebElement noteTab;
    @FindBy(css = "#add-note-btn")
    public WebElement addNoteBtn;
    @FindBy(css = "#note-title")
    public WebElement noteTitle;
    @FindBy(css = "#note-description")
    public WebElement noteDescription;
    @FindBy(css = "#submit-note-btn")
    public WebElement submitNoteBtn;
    @FindBy(css = "#nav-notes .btn-success")
    public List<WebElement> noteEditBtns;
    @FindBy(css = "#nav-notes .btn-danger")
    public List<WebElement> noteDeleteBtns;
    @FindBy(name = "noteTitleTable")
    public List<WebElement> noteTitles;
    @FindBy(name = "noteDescriptionTable")
    public List<WebElement> noteDescriptions;

    @FindBy(css = "#nav-credentials-tab")
    public WebElement credentialTab;
    @FindBy(css = "#add-credential-btn")
    public WebElement addCredentialBtn;
    @FindBy(css = "#credential-url")
    public WebElement credentialUrl;
    @FindBy(css = "#credential-username")
    public WebElement credentialUsername;
    @FindBy(css = "#credential-password")
    public WebElement credentialPassword;
    @FindBy(css = "#submit-credential-btn")
    public WebElement submitCredentialBtn;

    @FindBy(css = "#credentialModal .btn-secondary")
    public WebElement closeCredentialModal;

    @FindBy(css = "#nav-credentials .btn-success")
    public List<WebElement> credentialEditBtns;
    @FindBy(css = "#nav-credentials .btn-danger")
    public List<WebElement> credentialDeleteBtns;
    @FindBy(name = "credentialUrl")
    public List<WebElement> credentialUrls;
    @FindBy(name = "credentialUsername")
    public List<WebElement> credentialUsernames;
    @FindBy(name = "credentialPassword")
    public List<WebElement> credentialPasswords;


    public HomePage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void logout(){
        this.logoutBtn.click();
    }

    public void inputNoteData(String title, String description){
        this.noteTitle.clear();
        this.noteDescription.clear();
        this.noteTitle.sendKeys(title);
        this.noteDescription.sendKeys(description);
        this.submitNoteBtn.click();
    }

    public void deleteNote(int index){
        this.noteDeleteBtns.get(index).click();
    }

    public void inputCredentialData(String url, String username, String password) {
        this.credentialUrl.clear();
        this.credentialUsername.clear();
        this.credentialPassword.clear();
        this.credentialUrl.sendKeys(url);
        this.credentialUsername.sendKeys(username);
        this.credentialPassword.sendKeys(password);
        this.submitCredentialBtn.click();
    }

    public void deleteCredential(int index){
        this.credentialDeleteBtns.get(index).click();
    }
}
