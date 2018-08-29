package dailydoer.hourlies;

import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SymolHole
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Starting runSymolHole");
        driver.get("http://www.neopets.com/medieval/symolhole.phtml");
        if (Utils.isElementPresentXP("//*[@id='content']/table/tbody/tr/td[2]/center/form/input[3]", driver)) {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/center/form/input[3]")).click();
            Utils.logMessage("Successfully ending runSymolHole");
            return true;
        }
        Utils.logMessage("Failed ending runSymolHole");
        return false;
    }
}