package dailydoer.dailies;

import dailydoer.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class GrundoPlushie
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Visiting GrundoPlushie...");
        driver.get("http://www.neopets.com/faerieland/tdmbgpop.phtml");

        if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[3]/form/input[2]", driver)) {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[3]/form/input[2]")).click();
            Utils.logMessage("Finished GrundoPlushie.");
            return true;
        }
        Utils.logMessage("Failed GrundoPlushie. Something went wrong.");
        return false;
    }
}