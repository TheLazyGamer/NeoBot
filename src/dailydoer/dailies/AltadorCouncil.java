package dailydoer.dailies;

import dailydoer.User;
import dailydoer.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AltadorCouncil
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Running Altador Council...");
        driver.get("http://www.neopets.com/altador/council.phtml?prhv=" + User.ALTADOR_URL);

        if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/input[3]", driver))
        {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/input[3]")).click();
            Utils.logMessage("Finished Altador Council.");
            return true;
        }

        Utils.logMessage("Failed Altador Council. Something went wrong.");
        return false;
    }
}