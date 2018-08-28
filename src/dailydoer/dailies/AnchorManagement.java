package dailydoer.dailies;

import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AnchorManagement
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Running Anchor Management...");
        driver.get("http://www.neopets.com/pirates/anchormanagement.phtml");

        if (Utils.isElementPresentXP("//*[@id=\"btn-fire\"]", driver))
        {
            driver.findElement(By.xpath("//*[@id=\"btn-fire\"]/a/div")).click();
            Utils.logMessage("Finished Anchor Management.");
            return true;
        }

        Utils.logMessage("Failed Anchor Management. Something went wrong.");
        return false;
    }
}