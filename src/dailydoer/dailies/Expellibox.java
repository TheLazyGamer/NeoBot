package dailydoer.dailies;

import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

public class Expellibox
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Starting runExpellibox");
        driver.get("http://ncmall.neopets.com/mall/shop.phtml?page=giveaway");

        Utils.sleepMode(10000);

        Actions builder = new Actions(driver);
        try {
            builder.moveToElement(driver.findElement(By.xpath("//*[@id=\"main_div\"]/div/div[2]")), 350, 430).doubleClick().build().perform();
            Utils.sleepMode(1000);
            builder.moveToElement(driver.findElement(By.xpath("//*[@id=\"main_div\"]/div/div[2]")), 350, 430).click().build().perform();
        }
        catch(Exception ex) {

        }
        Utils.sleepMode(20000);

        Utils.logMessage("Successfully ending runExpellibox");
        return true;
    }
}