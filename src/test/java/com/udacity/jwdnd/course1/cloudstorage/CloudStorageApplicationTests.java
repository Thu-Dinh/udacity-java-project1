package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

  @LocalServerPort
  private int port;

  private WebDriver driver;

  @BeforeAll
  static void beforeAll() {
    WebDriverManager.chromedriver().setup();
  }

  @BeforeEach
  public void beforeEach() {
    this.driver = new ChromeDriver();
  }

  @AfterEach
  public void afterEach() {
    if (this.driver != null) {
      driver.quit();
    }
  }

  @Test
  public void getLoginPage() {
    driver.get("http://localhost:" + this.port + "/login");
    Assertions.assertEquals("Login", driver.getTitle());
  }

  /**
   * PLEASE DO NOT DELETE THIS method.
   * Helper method for Udacity-supplied sanity checks.
   **/
  private void doMockSignUp(String firstName, String lastName, String userName, String password) {
    // Create a dummy account for logging in later.

    // Visit the sign-up page.
    WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
    driver.get("http://localhost:" + this.port + "/signup");
    webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

    // Fill out credentials
    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
    WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
    inputFirstName.click();
    inputFirstName.sendKeys(firstName);

    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
    WebElement inputLastName = driver.findElement(By.id("inputLastName"));
    inputLastName.click();
    inputLastName.sendKeys(lastName);

    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
    WebElement inputUsername = driver.findElement(By.id("inputUsername"));
    inputUsername.click();
    inputUsername.sendKeys(userName);

    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
    WebElement inputPassword = driver.findElement(By.id("inputPassword"));
    inputPassword.click();
    inputPassword.sendKeys(password);

    // Attempt to sign up.
    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
    WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
    buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
//		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
  }


  /**
   * PLEASE DO NOT DELETE THIS method.
   * Helper method for Udacity-supplied sanity checks.
   **/
  private void doLogIn(String userName, String password) {
    // Log in to our dummy account.
    driver.get("http://localhost:" + this.port + "/login");
    WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
    WebElement loginUserName = driver.findElement(By.id("inputUsername"));
    loginUserName.click();
    loginUserName.sendKeys(userName);

    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
    WebElement loginPassword = driver.findElement(By.id("inputPassword"));
    loginPassword.click();
    loginPassword.sendKeys(password);

    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
    WebElement loginButton = driver.findElement(By.id("login-button"));
    loginButton.click();

    webDriverWait.until(ExpectedConditions.titleContains("Home"));

  }

  /**
   * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
   * rest of your code.
   * This test is provided by Udacity to perform some basic sanity testing of
   * your code to ensure that it meets certain rubric criteria.
   * <p>
   * If this test is failing, please ensure that you are handling redirecting users
   * back to the login page after a succesful sign up.
   * Read more about the requirement in the rubric:
   * https://review.udacity.com/#!/rubrics/2724/view
   */
  @Test
  public void testRedirection() {
    // Create a test account
    doMockSignUp("Redirection", "Test", "RT", "123");

    // Check if we have been redirected to the log in page.
    Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
  }

  /**
   * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
   * rest of your code.
   * This test is provided by Udacity to perform some basic sanity testing of
   * your code to ensure that it meets certain rubric criteria.
   * <p>
   * If this test is failing, please ensure that you are handling bad URLs
   * gracefully, for example with a custom error page.
   * <p>
   * Read more about custom error pages at:
   * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
   */
  @Test
  public void testBadUrl() {
    // Create a test account
    doMockSignUp("URL", "Test", "UT", "123");
    doLogIn("UT", "123");

    // Try to access a random made-up URL.
    driver.get("http://localhost:" + this.port + "/some-random-page");
    Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
  }


  /**
   * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
   * rest of your code.
   * This test is provided by Udacity to perform some basic sanity testing of
   * your code to ensure that it meets certain rubric criteria.
   * <p>
   * If this test is failing, please ensure that you are handling uploading large files (>1MB),
   * gracefully in your code.
   * <p>
   * Read more about file size limits here:
   * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
   */
  @Test
  public void testLargeUpload() {
    // Create a test account
    doMockSignUp("Large File", "Test", "LFT", "123");
    doLogIn("LFT", "123");

    // Try to upload an arbitrary large file
    WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
    String fileName = "upload5m.zip";

    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
    WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
    fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

    WebElement uploadButton = driver.findElement(By.id("uploadButton"));
    uploadButton.click();
    try {
      webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
    } catch (org.openqa.selenium.TimeoutException e) {
      System.out.println("Large File upload failed");
    }
    Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 ??? Forbidden"));

  }


  /**
   * Tests for User Signup, Login, and Unauthorized Access Restrictions
   */
  @Test
  public void getSignupPage() {
    driver.get("http://localhost:" + this.port + "/signup");
    Assertions.assertEquals("Sign Up", driver.getTitle());
  }

  @Test
  public void unauthorizedUserRedirectLoginPage() {
    driver.get("http://localhost:" + this.port + "/files");
    Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
  }

  @Test
  public void testLoginAndLogoutFlow() {
    // Create a test account
    doMockSignUp("Flow", "Test", "FT", "123");
    doLogIn("FT", "123");

    Assertions.assertEquals("Home", driver.getTitle());

    WebElement logoutButton = driver.findElement(By.id("logoutBtn"));
    logoutButton.click();
    Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    Assertions.assertEquals("Login", driver.getTitle());
  }


  /**
   * Tests for Note Creation, Viewing, Editing, and Deletion
   */
  @Test
  public void testAddNote() {
    doMockSignUp("Note", "Test", "ANT", "123");
    doLogIn("ANT", "123");

    // add note
    doAddNote("note title", "note description");

    List<WebElement> noteTitles = driver.findElements(By.className("noteTitle"));
    Assertions.assertTrue(noteTitles.stream().map(e -> e.getText()).anyMatch(t -> t.equals("note title")));
    List<WebElement> noteDescriptions = driver.findElements(By.className("noteDescription"));
    Assertions.assertTrue(noteDescriptions.stream().map(e -> e.getText()).anyMatch(t -> t.equals("note description")));


  }

  @Test
  public void testEditNote() {
    doMockSignUp("Note", "Test", "ENT", "123");
    doLogIn("ENT", "123");

    // add note
    doAddNote("note title 1", "note description 1");
    doAddNote("note title 2", "note description 2");

    // edit note
    doEditNote(1, "note title update", "note description update");

    List<WebElement> noteTitles = driver.findElements(By.className("noteTitle"));
    Assertions.assertEquals("note title update", noteTitles.get(1).getText());
    List<WebElement> noteDescriptions = driver.findElements(By.className("noteDescription"));
    Assertions.assertEquals("note description update", noteDescriptions.get(1).getText());

  }

  @Test
  public void testDeleteNote() {
    doMockSignUp("Note", "Test", "DNT", "123");
    doLogIn("DNT", "123");

    // add note
    doAddNote("note title 1", "note description 1");

    List<WebElement> deleteBtn = driver.findElements(By.className("note-delete"));
    deleteBtn.get(0).click();

    List<WebElement> noteTitles = driver.findElements(By.className("noteTitle"));
    Assertions.assertEquals(0, noteTitles.size());
    List<WebElement> noteDescriptions = driver.findElements(By.className("noteDescription"));
    Assertions.assertEquals(0, noteDescriptions.size());

  }

  private void doAddNote(String title, String description) {
    WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
    WebElement noteTab = driver.findElement(By.id("nav-notes-tab"));
    noteTab.click();

    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-note")));
    WebElement addNoteBtn = driver.findElement(By.id("add-note"));
    addNoteBtn.click();

    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
    WebElement noteTitle = driver.findElement(By.id("note-title"));
    noteTitle.click();
    noteTitle.sendKeys(title);

    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
    WebElement noteDescription = driver.findElement(By.id("note-description"));
    noteDescription.click();
    noteDescription.sendKeys(description);

    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-submit")));
    WebElement noteSubmitBtn = driver.findElement(By.id("note-submit"));
    noteSubmitBtn.click();
  }

  private void doEditNote(int index, String title, String description) {
    WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
    List<WebElement> editBtn = driver.findElements(By.className("note-edit"));
    editBtn.get(index).click();

    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
    WebElement noteTitle = driver.findElement(By.id("note-title"));
    noteTitle.click();
    noteTitle.clear();
    noteTitle.sendKeys(title);

    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
    WebElement noteDescription = driver.findElement(By.id("note-description"));
    noteDescription.click();
    noteDescription.clear();
    noteDescription.sendKeys(description);

    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-submit")));
    WebElement noteSubmitBtn = driver.findElement(By.id("note-submit"));
    noteSubmitBtn.click();
  }


  /**
   * Tests for Credential Creation, Viewing, Editing, and Deletion
   */
  @Test
  public void testAddCredential() {
    doMockSignUp("Credential", "Test", "ACT", "123");
    doLogIn("ACT", "123");

    // add credential
    doAddCredential("https://google.com", "abcd", "1234");

    List<WebElement> creUrls = driver.findElements(By.className("cre-url"));
    Assertions.assertTrue(creUrls.stream().map(e -> e.getText()).anyMatch(t -> t.equals("https://google.com")));
    List<WebElement> creUsernames = driver.findElements(By.className("cre-username"));
    Assertions.assertTrue(creUsernames.stream().map(e -> e.getText()).anyMatch(t -> t.equals("abcd")));
  }

  @Test
  public void testEditCredential() {
    doMockSignUp("Credential", "Test", "ECT", "123");
    doLogIn("ECT", "123");

    // add credential
    doAddCredential("https://google.com", "abcd", "1234");
    doAddCredential("https://udacity.com", "udacity", "1234");

    // edit
    doEditCredential(0, "https://fsoft.com.vn", "abc", "123456");

    List<WebElement> creUrls = driver.findElements(By.className("cre-url"));
    Assertions.assertEquals("https://fsoft.com.vn", creUrls.get(0).getText());
    List<WebElement> creUsernames = driver.findElements(By.className("cre-username"));
    Assertions.assertEquals("abc", creUsernames.get(0).getText());

  }

  @Test
  public void testDeleteCredential() {
    doMockSignUp("Credential", "Test", "DCT", "123");
    doLogIn("DCT", "123");

    // add credential
    doAddCredential("https://google.com", "abcd", "1234");

    List<WebElement> deleteBtn = driver.findElements(By.className("cre-delete"));
    deleteBtn.get(0).click();
    List<WebElement> creUrls = driver.findElements(By.className("cre-url"));
    Assertions.assertEquals(0, creUrls.size());
    List<WebElement> creUsernames = driver.findElements(By.className("cre-username"));
    Assertions.assertEquals(0, creUsernames.size());

  }

  private void doAddCredential(String url, String username, String password) {
    WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
    WebElement creTab = driver.findElement(By.id("nav-credentials-tab"));
    creTab.click();

    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-credential")));
    WebElement addNoteBtn = driver.findElement(By.id("add-credential"));
    addNoteBtn.click();

    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
    WebElement creUrl = driver.findElement(By.id("credential-url"));
    creUrl.click();
    creUrl.sendKeys(url);

    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
    WebElement creUsername = driver.findElement(By.id("credential-username"));
    creUsername.click();
    creUsername.sendKeys(username);

    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
    WebElement crePasword = driver.findElement(By.id("credential-password"));
    crePasword.click();
    crePasword.sendKeys(password);

    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-submit")));
    WebElement noteSubmitBtn = driver.findElement(By.id("credential-submit"));
    noteSubmitBtn.click();
  }

  private void doEditCredential(int index, String url, String username, String password) {

    List<WebElement> editBtn = driver.findElements(By.className("cre-edit"));
    editBtn.get(index).click();

    WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
    WebElement creUrl = driver.findElement(By.id("credential-url"));
    creUrl.click();
    creUrl.clear();
    creUrl.sendKeys(url);

    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
    WebElement creUsername = driver.findElement(By.id("credential-username"));
    creUsername.click();
    creUsername.clear();
    creUsername.sendKeys(username);

    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
    WebElement crePasword = driver.findElement(By.id("credential-password"));
    crePasword.click();
    crePasword.clear();
    crePasword.sendKeys(password);

    webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-submit")));
    WebElement noteSubmitBtn = driver.findElement(By.id("credential-submit"));
    noteSubmitBtn.click();
  }


}
