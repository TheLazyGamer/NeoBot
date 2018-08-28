package dailydoer.dailies;

import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class GrumpyOldKing
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Telling King Skarl a joke...");

        for (int x = 0; x < 2; x++)
        {
            driver.get("http://www.neopets.com/medieval/grumpyking.phtml");
            List<WebElement> selects2 = driver.findElements(By.xpath("//form//select"));

            //Selections for Jester avatar (Peophin olives)
            int[] jester = {3, 8, 6, 1, 39, 118, 1, 32, 1, 143, 2, 2, 2, 2, 2, 2, 2, 2};
            int jesterIndex = 0;

            if (selects2.size() > 0)
            {
                for (WebElement select : selects2)
                {
                    List<WebElement> options = select.findElements(By.tagName("option"));
                    options.get(jester[jesterIndex]).click();
                    jesterIndex++;
                }

                Utils.logMessage("Joke " + x);

                if (Utils.isElementPresentXP("//input[@value='Tell the King your joke!']", driver)) {
                    driver.findElement(By.xpath("//input[@value='Tell the King your joke!']")).click();
                }

                Utils.logMessage("Joke " + x);
                Utils.sleepMode(5000);
            }
            if (x == 1) {
                Utils.logMessage("Told King Skarl a joke.");
                return true;
            }
        }
        Utils.logMessage("Failed telling a joke. Something went wrong.");
        return false;
    }
}