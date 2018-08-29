package dailydoer.dailies;

import dailydoer.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Jelly
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Running Jelly...");
        driver.get("http://www.neopets.com/jelly/jelly.phtml");

        if (Utils.isElementPresentXP("//form[@action='jelly.phtml']/input[@type='submit']", driver))
        {
            driver.findElement(By.xpath("//form[@action='jelly.phtml']/input[@type='submit']")).click();
            Utils.logMessage("Grabbed some jelly.");
            return true;
        }
        Utils.logMessage("Fresh out of jelly. Will try again later.");
        return false;
    }
}