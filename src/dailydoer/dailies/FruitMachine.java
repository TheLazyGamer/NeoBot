package dailydoer.dailies;

import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class FruitMachine
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Running Fruit Machine...");
        driver.get("http://www.neopets.com/desert/fruit/index.phtml");

        if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/input[3]", driver))
        {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/input[3]")).click();
            Utils.sleepMode(10000); //extra 10 seconds to let the wheel spin
            Utils.logMessage("Finished Fruit Machine.");
            return true;
        }
        Utils.logMessage("Failed Fruit Machine.");
        return false;
    }
}