package dailydoer.dailies;

import dailydoer.Utils;
import org.openqa.selenium.WebDriver;

public class AppleBobbing
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Running Apple Bobbing...");
        driver.get("http://www.neopets.com/halloween/applebobbing.phtml?bobbing=1");

        if (Utils.isElementPresentXP("//*[@id=\"header\"]/table/tbody/tr[1]/td[1]/a/img", driver))
        {
            Utils.logMessage("Finished Apple Bobbing.");
            return true;
        }
        Utils.logMessage("Failed Apple Bobbing. Something went wrong.");
        return false;
    }
}