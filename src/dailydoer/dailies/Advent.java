package dailydoer.dailies;

import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Advent
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Running Advent Calendar...");
        driver.get("http://www.neopets.com/winter/adventcalendar.phtml");

        if (Utils.isElementPresentXP("//input[@value='Collect My Prize!!!']", driver))
        {
            driver.findElement(By.xpath("//input[@value='Collect My Prize!!!']")).click();
            Utils.logMessage("Finished Advent Calendar.");
            return true;
        }
        /*
		sleepMode(10000);
		driver.get("http://www.neopets.com/winter/adventcalendar.phtml");
		sleepMode(10000);
		if (isElementPresentXP("//*[@id=\"dom_overlay_container\"]", driver)) {
			driver.findElement(By.xpath("//*[@id=\"dom_overlay_container\"]")).click();
		}*/

        Utils.logMessage("Failed Advent Calendar.");
        return false;
    }
}