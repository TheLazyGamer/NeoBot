package dailydoer.hourlies;

import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ColtzansShrine
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Starting runColtzansShrine");
        driver.get("http://www.neopets.com/desert/shrine.phtml");

        if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/form[1]/input[2]", driver)) {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/form[1]/input[2]")).click();
            Utils.logMessage("Successfully ending runColtzansShrine");
            return true;
        }
        Utils.logMessage("Failed ending runColtzansShrine");
        return false;
    }
}