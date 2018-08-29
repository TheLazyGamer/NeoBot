package dailydoer.dailies;

import dailydoer.User;
import dailydoer.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Bank
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Running Bank...");
        driver.get("http://www.neopets.com/bank.phtml");

        if (Utils.isElementPresentXP("//form[@onsubmit='return one_submit();']/input[@type='submit']", driver))
        {
            driver.findElement(By.xpath("//form[@onsubmit='return one_submit();']/input[@type='submit']")).click();
        }

        int neopoints = Integer.parseInt(driver.findElement(By.id("npanchor")).getText().replace(",", ""));

        if (neopoints < User.NEOPOINTS_TO_WITHDRAW)
        {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table[1]/tbody/tr/td[2]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/input[1]")).clear();
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table[1]/tbody/tr/td[2]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/input[1]")).sendKeys("" + (100000 - neopoints));
            driver.findElement(By.xpath("//input[@value='Withdraw']")).click();
            Utils.handleAlert(driver);
        }

        Utils.logMessage("Ending Bank run.");
        return true;
    }
}