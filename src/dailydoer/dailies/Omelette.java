package dailydoer.dailies;

import dailydoer.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Omelette
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Running Omelette...");
        driver.get("http://www.neopets.com/prehistoric/omelette.phtml");

        if (Utils.isElementPresentXP("//form[@action='omelette.phtml']/input[@type='submit']", driver))
        {
            driver.findElement(By.xpath("//form[@action='omelette.phtml']/input[@type='submit']")).click();
            Utils.logMessage("Grabbed some omelette.");
            return true;
        }
        Utils.logMessage("Fresh out of omelette. Will try again later.");
        return false;
    }
}