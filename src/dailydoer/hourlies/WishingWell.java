package dailydoer.hourlies;

import dailydoer.User;
import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class WishingWell
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Starting runWishingWell");
        driver.get("http://www.neopets.com/wishing.phtml");

        for (int x = 0; x < 7; x++) {
            driver.findElement(By.name("donation")).clear();
            driver.findElement(By.name("donation")).sendKeys("21");
            driver.findElement(By.name("wish")).clear();
            driver.findElement(By.name("wish")).sendKeys(User.WISHING_WELL_ITEM);
            driver.findElement(By.xpath("//input[@value='Make a Wish']")).click();
        }
        Utils.logMessage("Successfully ending runWishingWell");
        return true;
    }
}