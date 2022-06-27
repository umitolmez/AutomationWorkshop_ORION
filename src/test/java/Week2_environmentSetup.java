import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Week2_environmentSetup {

    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        //navigate to URL
        driver.get("https://www.google.com/");

        //type Wiki
        WebElement searchBox =driver.findElement(By.name("q"));
        searchBox.sendKeys("Wiki");

        //search
        WebElement firstOption = driver.findElement(By.className("pcTkSc"));
        firstOption.click();
    }
}
