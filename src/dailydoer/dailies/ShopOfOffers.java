package dailydoer.dailies;

import dailydoer.Utils;

import org.openqa.selenium.WebDriver;

public class ShopOfOffers
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Running Shop of Offers...");
        driver.get("http://www.neopets.com/shop_of_offers.phtml?slorg_payout=yes");

        if (Utils.isElementPresentXP("//*[@id=\"header\"]/table/tbody/tr[1]/td[1]/a/img", driver)) {
            Utils.logMessage("Finished Shop of Offers.");
            return true;
        }
        Utils.logMessage("Failed Shop of Offers. Something went wrong.");
        return false;
    }
}