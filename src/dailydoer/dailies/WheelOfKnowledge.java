package dailydoer.dailies;

import dailydoer.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

public class WheelOfKnowledge
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Spinning Wheel of Knowledge...");
        driver.get("http://www.neopets.com/medieval/knowledge.phtml");
        Actions wheelClicker = new Actions(driver);

        Utils.sleepMode(5000);

        try {
            wheelClicker.moveToElement(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/embed")), 540, 300).click().build().perform();
            Utils.sleepMode(20000);
            wheelClicker.moveToElement(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/embed")), 540, 300).click().build().perform();
            Utils.sleepMode(20000);
            wheelClicker.moveToElement(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/embed")), 540, 300).click().build().perform();
        }
        catch(Exception ex) {
            Utils.logMessage("Failed spinning Wheel of Knowledge. Something went wrong.");
            return false;
        }
        Utils.sleepMode(5000);
        Utils.logMessage("Finished spinning Wheel of Knowledge.");
        return true;
    }
}