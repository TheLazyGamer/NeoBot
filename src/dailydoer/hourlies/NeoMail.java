package dailydoer.hourlies;

import dailydoer.Utils;
import org.openqa.selenium.WebDriver;

public class NeoMail
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Checking NeoMail...");
        driver.get("http://www.neopets.com/neomessages.phtml");

        if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/form/table/tbody/tr[3]/td[1]/input", driver)) {
            Utils.sendEmail("NeoMail Received");
        }
        else if (Utils.isElementPresentXP("//*[@id=\"header\"]/table/tbody/tr[1]/td[2]/a[2]/b", driver)) {
            Utils.sendEmail("Trade Offer Received");
        }
        Utils.logMessage("Finished checking NeoMail.");
        return true;
    }
}