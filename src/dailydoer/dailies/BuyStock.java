package dailydoer.dailies;

import dailydoer.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class BuyStock
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Buying stocks...");
        driver.get("http://www.neopets.com/stockmarket.phtml?type=list&search=%&bargain=true");
        String stockSource = driver.getPageSource();

        if (stockSource.contains("/marquee"))
        {
            stockSource = stockSource.substring(stockSource.indexOf("marquee"), stockSource.indexOf("/marquee"));
            String[] stockSourceArr = stockSource.split("><b");

            for (int x = 1; x < stockSourceArr.length; x++)
            {
                String ticker = stockSourceArr[x].substring(1, stockSourceArr[x].indexOf("<"));
                String tickerPrice = ticker.substring(ticker.indexOf(" ")+1, ticker.lastIndexOf(" "));
                String tickerName = ticker.substring(0, ticker.indexOf(" "));

                if (tickerPrice.equals("15"))
                {
                    Utils.logMessage("Found a stock for 15 NP.");
                    driver.get("http://www.neopets.com/stockmarket.phtml?type=buy&ticker=" + tickerName);

                    if (Utils.isElementPresentName("amount_shares", driver))
                    {
                        driver.findElement(By.name("amount_shares")).clear();
                        driver.findElement(By.name("amount_shares")).sendKeys("1000");
                        driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[3]/td/input")).click();
                        Utils.sleepMode(5000); //Allow time for purchase to process
                        Utils.logMessage("Bought some " + tickerName + " stocks.");
                        return true;
                    }
                    else {
                        Utils.logMessage("Couldn't buy stock. Something went wrong.");
                        return false;
                    }
                }
            }
            Utils.logMessage("No stock worth 15 ea. yet.");
            return false;
        }
        Utils.logMessage("Couldn't find ticker list. Something went wrong. Site update?");
        return false;
    }
}