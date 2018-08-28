package dailydoer.hourlies;

import dailydoer.User;
import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HealingSprings
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Visiting the Healing Springs...");
        driver.get("http://www.neopets.com/quickref.phtml");

        String source = driver.getPageSource();

        boolean needToHeal = false;

        if (source.contains(User.PETNAME + " is suffering from")) {
            Utils.logMessage(User.PETNAME + " is sick");
            needToHeal = true;
        }

        if (Utils.isElementPresentXP("//*[@id=\"" + User.PETNAME + "_details\"]/table/tbody/tr[2]/td/div[1]/table/tbody/tr[6]/td/font/b", driver)) {
            String hpStats = driver.findElement(By.xpath("//*[@id=\"" + User.PETNAME + "_details\"]/table/tbody/tr[2]/td/div[1]/table/tbody/tr[6]/td/font/b")).getText();
            int currentHP = Integer.parseInt(hpStats.substring(0, hpStats.indexOf(" ")));
            int totalHP = Integer.parseInt(hpStats.substring(hpStats.indexOf("/") + 2, hpStats.length()));
            String hunger = driver.findElement(By.xpath("//*[@id=\"" + User.PETNAME + "_details\"]/table/tbody/tr[2]/td/div[1]/table/tbody/tr[8]/td/b")).getText().trim().toLowerCase();

            if (currentHP < totalHP /*|| hunger.equals("hungry") || hunger.equals("very hungry") ||
					hunger.equals("famished") || hunger.equals("starving") || hunger.equals("dying")*/) {
                needToHeal = true;
            }

        }
        else {
            Utils.logMessage("Failed ending 1 runHealingSprings");
            return false;
        }

        if (needToHeal) {
            driver.get("http://www.neopets.com/faerieland/springs.phtml");
            if (Utils.isElementPresentXP("//input[@value='Heal my Pets']", driver)) {
                driver.findElement(By.xpath("//input[@value='Heal my Pets']")).click();
            }
            else {
                Utils.logMessage("Failed ending 2 runHealingSprings");
                return false;
            }
        }

        Utils.logMessage("Successfully ending runHealingSprings");
        return true;
    }
}