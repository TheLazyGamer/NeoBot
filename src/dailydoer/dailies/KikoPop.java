package dailydoer.dailies;

import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

public class KikoPop
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Starting runKikoPop");
        driver.get("http://www.neopets.com/worlds/kiko/kpop/");

        Utils.sleepMode(10000);

        if (Utils.isElementPresentXP("//*[@id=\"difficultyForm\"]/div[3]/div", driver)) {
            driver.findElement(By.xpath("//*[@id=\"difficultyForm\"]/div[3]/div")).click();
        }

        Utils.sleepMode(10000);

        Actions builder = new Actions(driver);
        try {
            builder.moveToElement(driver.findElement(By.name("kikopop")), 460, 300).click().build().perform();
        }
        catch(Exception ex) {

        }

        Utils.sleepMode(15000);

        Actions builder2 = new Actions(driver);
        try {
            builder2.moveToElement(driver.findElement(By.name("kikopop")), 540, 540).click().build().perform();
        }
        catch(Exception ex) {

        }

        Utils.sleepMode(10000);

        Utils.logMessage("Successfully ending runKikoPop");
        return true;
    }
}