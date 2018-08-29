package dailydoer.dailies;

import dailydoer.User;
import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MoodImprove
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Starting runMoodImprove");

        while (true)
        {
            driver.get("http://www.neopets.com/worlds/roo/merrygoround.phtml");
            String mood = "";

            if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[1]/div[1]/table/tbody/tr[4]/td/table/tbody/tr[3]/td[2]/b", driver)) {
                mood = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[1]/div[1]/table/tbody/tr[4]/td/table/tbody/tr[3]/td[2]/b")).getText().trim();
            }
            else if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[1]/div[1]/table/tbody/tr[5]/td/table/tbody/tr[3]/td[2]/b", driver)) {
                mood = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[1]/div[1]/table/tbody/tr[5]/td/table/tbody/tr[3]/td[2]/b")).getText().trim();
            }
            else {
                return false;
            }
            if (!mood.equals("delighted!")) {
                driver.findElement(By.xpath("//input[@value='" + User.PETNAME + "']")).click();
                driver.findElement(By.xpath("//input[@value='Go on the Ride!']")).click();
                Utils.sleepMode(5000);
            }
            else {
                break;
            }
        }
        Utils.logMessage("Successfully ending runMoodImprove");
        return true;
    }
}