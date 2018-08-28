package dailydoer.dailies;

import dailydoer.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class Meteor
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Running Meteor...");
        driver.get("http://www.neopets.com/moon/meteor.phtml");
        if (Utils.isElementPresentXP("//input[@value='Take a chance']", driver)) {
            driver.findElement(By.xpath("//input[@value='Take a chance']")).click();
            new Select(driver.findElement(By.name("pickstep"))).selectByVisibleText("Poke the meteor with a stick.");
            driver.findElement(By.name("meteorsubmit")).click();
            Utils.sleepMode(5000); //Might need extra sleep
            Utils.logMessage("Finished Meteor.");
            return true;
        }
        Utils.logMessage("Failed ending Meteor.");
        return false;
    }
}