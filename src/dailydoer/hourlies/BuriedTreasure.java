package dailydoer.hourlies;

import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

public class BuriedTreasure
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Playing Buried Treasure...");
        driver.get("http://www.neopets.com/pirates/buriedtreasure/index.phtml");
        Actions mapClicker = new Actions(driver);

        if (Utils.isElementPresentXP("//input[@value='Click to Play!  Only 300 NP a game!']", driver))
        {
            driver.findElement(By.xpath("//input[@value='Click to Play!  Only 300 NP a game!']")).click();
            Utils.sleepMode(2000);

            if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center[1]/a/img", driver))
            {
                Utils.logMessage("Found treasure map.");
                mapClicker.moveToElement(driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center[1]/a/img")), 68, 74).click().build().perform();
                Utils.logMessage("Finished playing Buried Treasure.");
                return true;
            }

            Utils.logMessage("Can't play Buried Treasure yet. Wait a little longer.");
            return false;
        }
        Utils.logMessage("Couldn't play Buried Treasure. Something went wrong.");
        return false;
    }
}