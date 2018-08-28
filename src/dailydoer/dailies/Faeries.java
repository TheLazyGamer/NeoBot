package dailydoer.dailies;

import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Faeries
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Starting runFaeries");
        //Uncomment this when the faerie festival rolls around (regularly September)
        //driver.get("http://www.neopets.com/faeriefestival/");

        //Check if we're already on a FQ and complete it before taking our daily one
        driver.get("http://www.neopets.com/market.phtml?type=wizard");

        if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/p[1]/a[1]", driver)) { //Faerie quest link
            Utils.logMessage("runFaeries ShopWizard FQ found");
            if (!Utils.faerieQuest(driver)) {
                return false;
            }
        }

        driver.get("http://www.neopets.com/quests.phtml");

        if (Utils.isElementPresentID("fq-fc-accept", driver)) {
            driver.findElement(By.id("fq-fc-accept")).click();
            Utils.faerieQuest(driver);
        }

        Utils.logMessage("Successfully ending runFaeries");
        return true;
    }
}