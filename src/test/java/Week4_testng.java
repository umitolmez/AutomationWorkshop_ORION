import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;

/*  Assert vs Verify; https://www.browserstack.com/guide/verify-and-assert-in-selenium
    Hard Assert vs Soft Assert(Verify); https://artoftesting.com/difference-between-assert-and-verify#:~:text=In%20the%20case%20of%20the,failure%20of%20an%20assertion%20statement.
 */

public class Week4_testng {
    public WebDriver driver;

    //credentials
    public String email=" ";
    public String password=" ";
    public String nick=" ";
    public String phoneNumber="9053...";

    @BeforeClass
    public void setUpDriver(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterClass
    public void report(){
        System.out.println("Report: not yet :(");
    }

    @BeforeMethod
    public void openBrowser() throws InterruptedException {
        driver.get("https://twitter.com/");
        driver.manage().window().maximize();
        Thread.sleep(1500);
    }

    @AfterMethod
    public void shutDown(){
        driver.close();
    }

    public void login() throws InterruptedException {
        //click logIn
        WebElement login =driver.findElement(By.xpath("//a[@href='/login']"));
        login.click();

        //type email
        Thread.sleep(2500);
        WebElement emailBox =driver.findElement(By.name("text"));
        emailBox.sendKeys(email);
        WebElement next =driver.findElement(By.xpath("//div[@role='button'][2]"));
        next.click();

        //if you spam logging
        Thread.sleep(1500);
        String textTitle =driver.findElement(By.xpath("//div/div/div/div/div/div[2]/div/div/span/span")).getText();
        if(textTitle.startsWith("There was unusual login activity")){
            WebElement phoneOrUsername = driver.findElement(By.name("text"));
            phoneOrUsername.sendKeys(nick);
            WebElement nextButton = driver.findElement(By.xpath("//div[@data-testid='ocfEnterTextNextButton']"));
            nextButton.click();
        }

        //type password
        Thread.sleep(2000);
        WebElement passwordBox = driver.findElement(By.xpath("//input[@type='password']"));
        passwordBox.sendKeys(password);
        WebElement loginForm = driver.findElement(By.xpath("//div[@data-testid='LoginForm_Login_Button']"));
        loginForm.click();

        //if you spam logging
        Thread.sleep(2000);
        String textTitle2 =driver.findElement(By.xpath("//div/div/div/div/div/div[2]/div/div/span/span")).getText();
        if(textTitle2.startsWith("Verify your identity by entering the phone")){
            WebElement phoneNumberBox = driver.findElement(By.name("text"));
            phoneNumberBox.sendKeys(phoneNumber);
            WebElement nextButton = driver.findElement(By.xpath("//div[@data-testid='ocfEnterTextNextButton']"));
            nextButton.click();
        }

        //verify that I logged in successfully
        Thread.sleep(3000);
        String expectedURL ="https://twitter.com/home";
        String actualURL = driver.getCurrentUrl();
        Assert.assertEquals(actualURL, expectedURL);

        String actualUsername = driver.findElement(By.xpath("//div[@dir='ltr']/span")).getText();
        Assert.assertEquals(actualUsername, "@"+nick);
    }

    @Test
    public void tweetAndDelete() throws InterruptedException {
        login();

        //send tweet
        WebElement tweet =driver.findElement(By.xpath("//div[@data-contents='true']"));
        tweet.sendKeys("hello ORION");
        WebElement tweetButton = driver.findElement(By.xpath("//div[@data-testid='tweetButtonInline']/div"));
        tweetButton.click();
        Thread.sleep(4000);

        //delete the tweet
        driver.findElement(By.xpath("//div[@data-testid='caret']")).click();
        driver.findElement(By.xpath("//div[@role='menuitem']")).click();
        driver.findElement(By.xpath("//div[@data-testid='confirmationSheetConfirm']")).click();

        //verify the tweet was deleted
        Thread.sleep(3000);
        String toastMessage = driver.findElement(By.xpath("//div[@data-testid='toast']")).getText();
        Assert.assertTrue(toastMessage.startsWith("Your Tweet was deleted"));
    }

    @Test
    public void retweet() throws InterruptedException {
        login();

        //retweet Nth tweet (between 1-9)
        List<WebElement> nthTweet= driver.findElements(By.xpath("//div[@data-testid='retweet']"));
        nthTweet.get(0).click();
        driver.findElement(By.xpath("//div[@data-testid='retweetConfirm']")).click();

        //verify the tweet was retweeted
        Assert.assertEquals(nthTweet.get(0).getAttribute("data-testid"),"unretweet");

        //to see the result
        Thread.sleep(2000);

        //BONUS - unretweet before close driver
    }

}
