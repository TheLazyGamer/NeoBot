package dailydoer.dailies;

import dailydoer.Utils;

import org.openqa.selenium.WebDriver;

public class IslandMystic
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Running Island Mystic...");
        driver.get("http://www.neopets.com/island/mystichut.phtml");

        if (Utils.isElementPresentXP("//*[@id=\"header\"]/table/tbody/tr[1]/td[1]/a/img", driver))
        {
            Utils.logMessage("Finished Island Mystic.");
            return true;
        }
        Utils.logMessage("Failed Island Mystic. Something went wrong.");
        return false;
    }
}