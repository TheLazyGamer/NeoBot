package dailydoer.hourlies;

import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

public class WheelOfMisfortune
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Spinning the Wheel of Misfortune...");
        driver.get("http://www.neopets.com/halloween/wheel/index.phtml");

        Actions wheelClicker = new Actions(driver);
        Utils.sleepMode(2000);

        try {
            wheelClicker.moveToElement(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[1]/embed")), 540, 300).click().build().perform();
            Utils.sleepMode(20000);
            wheelClicker.moveToElement(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[1]/embed")), 540, 300).click().build().perform();
        }
        catch(Exception ex) {
            Utils.logMessage("Couldn't spin the Wheel of Misfortune. Something went wrong.");
            return false;
        }

        Utils.sleepMode(4000);

        Utils.logMessage("Finished the Wheel of Misfortune.");
        return true;
    }
}