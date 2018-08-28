package dailydoer.dailies;

import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

public class TrudysSurprise
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Running Trudy's Surprise...");
        driver.get("http://www.neopets.com/trudys_surprise.phtml");
        Actions builder = new Actions(driver);

        Utils.sleepMode(5000);

        try {
            builder.moveToElement(driver.findElement(By.xpath("//*[@id=\"tempcontent\"]")), 400, 500).doubleClick().build().perform();
            Utils.sleepMode(1000);
            builder.moveToElement(driver.findElement(By.xpath("//*[@id=\"tempcontent\"]")), 400, 500).doubleClick().build().perform();
            Utils.sleepMode(5000);
            builder.moveToElement(driver.findElement(By.xpath("//*[@id=\"tempcontent\"]")), 400, 500).click().build().perform();
        }
        catch(Exception ex) {
            Utils.logMessage("Failed Trudy's Surprise. Something went wrong.");
            return false;
        }
        Utils.sleepMode(15000);

        Utils.logMessage("Finished Trudy's Surprise.");
        return true;
    }
}