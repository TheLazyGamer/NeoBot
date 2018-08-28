package dailydoer.hourlies;

import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SellStock
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Selling some stock...");
        driver.get("http://www.neopets.com/stockmarket.phtml?type=portfolio");

        boolean sellingStocks = false;

        for (int x = 3; Utils.isElementPresentXP("//*[@id=\"postForm\"]/table[1]/tbody/tr[" + x + "]/td[4]", driver); x+=2) {
            try
            {
                int currentPrice = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"postForm\"]/table[1]/tbody/tr[" + x + "]/td[4]")).getText().trim());

                if (currentPrice >= 60)
                {
                    sellingStocks = true;
                    String stockID = driver.findElement(By.xpath("//*[@id=\"postForm\"]/table[1]/tbody/tr[" + x + "]/td[1]/img[1]")).getAttribute("id").trim();
                    stockID = stockID.substring(0, stockID.indexOf("d"));
                    Utils.logMessage("Selling stock: " + stockID + " at price: " + currentPrice);
                    driver.findElement(By.xpath("//*[@id=\"postForm\"]/table[1]/tbody/tr[" + x + "]/td[1]/img[1]")).click();

                    for (int y = 2; Utils.isElementPresentXP("//*[@id=\"" + stockID + "\"]/td/table/tbody/tr[" + y + "]/td[1]", driver); y++)
                    {
                        String shareAmount = driver.findElement(By.xpath("//*[@id=\"" + stockID + "\"]/td/table/tbody/tr[" + y + "]/td[1]")).getText().trim().replace(",", "");
                        Utils.logMessage("Selling shareAmount: " + shareAmount);

                        if (Utils.isElementPresentXP("//*[@id=\"" + stockID + "\"]/td/table/tbody/tr[" + y + "]/td[7]/input", driver))
                        {
                            driver.findElement(By.xpath("//*[@id=\"" + stockID + "\"]/td/table/tbody/tr[" + y + "]/td[7]/input")).clear();
                            driver.findElement(By.xpath("//*[@id=\"" + stockID + "\"]/td/table/tbody/tr[" + y + "]/td[7]/input")).sendKeys(shareAmount);
                        }
                        else {
                            driver.findElement(By.xpath("//*[@id=\"" + stockID + "\"]/td/table/tbody/tr[" + y + "]/td[6]/input")).clear();
                            driver.findElement(By.xpath("//*[@id=\"" + stockID + "\"]/td/table/tbody/tr[" + y + "]/td[6]/input")).sendKeys(shareAmount);
                        }
                    }
                }
            }
            catch(NumberFormatException ex) {
                //This is a normal error. Doesn't need handling.
            }
        }

        if (sellingStocks) {
            driver.findElement(By.xpath("//input[@value='Sell Shares']")).click();
        }

        Utils.logMessage("Done checking stock.");
        return true;
    }
}