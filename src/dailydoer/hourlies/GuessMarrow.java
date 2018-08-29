package dailydoer.hourlies;

import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class GuessMarrow
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Playing Guess the Marrow...");
        driver.get("http://www.neopets.com/medieval/guessmarrow.phtml");

        if (Utils.isElementPresentXP("//*[@id=\"content\"]/div[1]/table/tbody/tr/td[2]/center[2]/form/input[1]", driver))
        {
            driver.findElement(By.xpath("//*[@id=\"content\"]/div[1]/table/tbody/tr/td[2]/center[2]/form/input[1]")).clear();
            driver.findElement(By.xpath("//*[@id=\"content\"]/div[1]/table/tbody/tr/td[2]/center[2]/form/input[1]")).sendKeys("427");
            driver.findElement(By.xpath("//input[@value='Guess!']")).click();
            Utils.logMessage("Finished Guess the Marrow.");
            return true;
        }
        Utils.logMessage("Couldn't finish Guess the Marrow. Hasn't started yet.");
        return false;
    }
}