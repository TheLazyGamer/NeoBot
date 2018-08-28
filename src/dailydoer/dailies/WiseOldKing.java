package dailydoer.dailies;

import dailydoer.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class WiseOldKing
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Giving King Hagan advice...");
        driver.get("http://www.neopets.com/medieval/wiseking.phtml");
        List<WebElement> selects = driver.findElements(By.xpath("//form//select"));

        if (selects.size() > 0)
        {
            for (WebElement select : selects) {
                List<WebElement> options = select.findElements(By.tagName("option"));
                options.get(2).click();
            }
            if (Utils.isElementPresentXP("//input[@value='Impress King Hagan with your wisdom!']", driver)) {
                driver.findElement(By.xpath("//input[@value='Impress King Hagan with your wisdom!']")).click();
            }
            Utils.logMessage("Finished giving advice.");
            return true;
        }
        Utils.logMessage("Failed King Hagan.");
        return false;

    }
}