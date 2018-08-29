package dailydoer.dailies;

import dailydoer.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class GeraptikuTomb
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Running Geraptiku Tomb...");
        driver.get("http://www.neopets.com/worlds/geraptiku/tomb.phtml");

        if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div[2]/form[1]/input[2]", driver))
        {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div[2]/form[1]/input[2]")).click();

            if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div[2]/form[1]/input", driver))
            {
                driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div[2]/form[1]/input")).click();
                Utils.logMessage("Finished Geraptiku Tomb.");
                return true;
            }
            Utils.logMessage("Failed ending 1 runGeraptikuTomb");
            return false;
        }
        Utils.logMessage("Failed ending 2 runGeraptikuTomb");
        return false;
    }
}