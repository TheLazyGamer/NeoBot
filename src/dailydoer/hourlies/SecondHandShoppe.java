package dailydoer.hourlies;

import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SecondHandShoppe
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Running Second Hand Shoppe...");
        driver.get("http://www.neopets.com/thriftshoppe/index.phtml");

        for (int x = 2; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[" + x + "]/td[1]/a/div[1]/img", driver); x++)
        {
            for (int y = 1; Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[" + x + "]/td[" + y + "]/a/div[1]/img", driver); y++)
            {
                String itemImg = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[" + x + "]/td[" + y + "]/a/div[1]/img")).getAttribute("src").trim();
                Utils.logMessage("runSecondHandShoppe itemImg: " + itemImg);

                if (!itemImg.equals("http://images.neopets.com/items/med_booby_5.gif") &&
                    !itemImg.equals("http://images.neopets.com/items/vor_mossy_rock.gif") &&
                    !itemImg.equals("http://images.neopets.com/items/newbie_potatosack.gif") &&
                    !itemImg.equals("http://images.neopets.com/items/bkg_oceanrockbackground.gif"))
                {
                    driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[" + x + "]/td[" + y + "]/a/div[1]/img")).click();
                    break;
                }
            }
        }
        Utils.logMessage("Finished Second Hand Shoppe.");
        return true;
    }
}