package dailydoer.hourlies;

import dailydoer.Utils;
import org.openqa.selenium.WebDriver;

public class Tarla
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Checking Tarla's Treasures...");
        driver.get("http://www.neopets.com/freebies/tarlastoolbar.phtml");

        if (Utils.isElementPresentXP("//*[@id=\"header\"]/table/tbody/tr[1]/td[1]/a/img", driver)) {
            Utils.logMessage("Finished checking Tarla's Treasures.");
            return true;
        }
        Utils.logMessage("Failed checking Tarla's Treasures.");
        return false;
    }
}