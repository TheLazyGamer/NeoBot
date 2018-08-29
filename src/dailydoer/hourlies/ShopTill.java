package dailydoer.hourlies;

import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ShopTill
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Checking Shop Till...");
        driver.get("http://www.neopets.com/market.phtml?type=till");

        if (Utils.isElementPresentXP("//input[@value='Withdraw']", driver))
        {
            String npString = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/p[1]/b")).getText();
            int npToWithdraw = Integer.parseInt(npString.replace(",", "").replace(" NP", ""));

            if (npToWithdraw > 0)
            {
                driver.findElement(By.name("amount")).clear();
                driver.findElement(By.name("amount")).sendKeys("" + npToWithdraw);
                driver.findElement(By.xpath("//input[@value='Withdraw']")).click();
            }
            Utils.logMessage("Finished checking Shop Till.");
            return true;
        }
        else {
            Utils.logMessage("Failed checking Shop Till. Something went wrong.");
            return false;
        }
    }
}