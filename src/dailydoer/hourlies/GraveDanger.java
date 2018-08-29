package dailydoer.hourlies;

import dailydoer.User;
import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;

public class GraveDanger
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Starting runGraveDanger");
        driver.get("http://www.neopets.com/halloween/gravedanger/");

        if (Utils.isElementPresentID("gdRemaining", driver)) {
            Utils.logMessage("Successfully ending runGraveDanger");
            return true;
        }

        //Received reward so clicking next adventure
        try {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/form[1]/input[2]")).click();
        }
        catch(ElementNotVisibleException ex) {

        }

        //Click petpet to run
        driver.findElement(By.xpath("//*[@id=\"gdSelection\"]/div[" + User.GRAVE_PETPET + "]")).click();

        //Click Choose a petpet(starts adventure)
        driver.findElement(By.xpath("//*[@id=\"gdSelection\"]/button")).click();
        Utils.handleAlert(driver);

        Utils.sleepMode(5000);

        Utils.logMessage("Failed ending runGraveDanger");
        return false;

    }
}