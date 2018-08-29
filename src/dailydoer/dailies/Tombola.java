package dailydoer.dailies;

import dailydoer.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Tombola
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Running Tombola...");
        driver.get("http://www.neopets.com/island/tombola.phtml");

        if (Utils.isElementPresentXP("//form[@action='tombola2.phtml']/input[@type='submit']", driver)) {
            driver.findElement(By.xpath("//form[@action='tombola2.phtml']/input[@type='submit']")).click();
            Utils.logMessage("Finished Tombola.");
            return true;
        }
        Utils.logMessage("Couldn't play Tombola.");
        return false;
    }
}