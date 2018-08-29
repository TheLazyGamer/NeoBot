package dailydoer.dailies;

import dailydoer.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LunarPuzzle
{
    public static boolean run(WebDriver driver)
    {
        /*
         * These are the different sources for solving (currently using Sunnyneo)
         * http://www.sunnyneo.com/lunartemple.php
         * http://www.jellyneo.net/?go=lunartemple
         * http://thedailyneopets.com/articles/solving-lunar-temple-puzzle/
         * http://neopets-cheats.com/neopets-lunar-temple-puzzle/
         */

        Utils.logMessage("Running Lunar Puzzle...");
        driver.get("http://www.neopets.com/shenkuu/lunar/?show=puzzle");

        Utils.sleepMode(5000);

        String lunarSource = driver.getPageSource();
        lunarSource = lunarSource.substring(lunarSource.indexOf("angleKreludor"), lunarSource.length());
        lunarSource = lunarSource.substring(lunarSource.indexOf("=") + 1, lunarSource.indexOf("&"));

        double angleKreludor = Double.parseDouble(lunarSource);

        //This division and rounding is for JN's answer
        //double answer = angleKreludor / 22.5;
        //answer = Math.round(answer);
        if (angleKreludor >= 0.0 && angleKreludor <= 11.24) {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[2]/td[1]/input")).click();
        }
        else if (angleKreludor >= 11.25 && angleKreludor <= 33.74) {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[2]/td[2]/input")).click();
        }
        else if (angleKreludor >= 33.75 && angleKreludor <= 56.24) {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[2]/td[3]/input")).click();
        }
        else if (angleKreludor >= 56.25 && angleKreludor <= 78.74) {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[2]/td[4]/input")).click();
        }
        else if (angleKreludor >= 78.75 && angleKreludor <= 101.24) {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[2]/td[5]/input")).click();
        }
        else if (angleKreludor >= 101.25 && angleKreludor <= 123.74) {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[2]/td[6]/input")).click();
        }
        else if (angleKreludor >= 123.75 && angleKreludor <= 146.24) {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[2]/td[7]/input")).click();
        }
        else if (angleKreludor >= 146.25 && angleKreludor <= 168.74) {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[2]/td[8]/input")).click();
        }
        else if (angleKreludor >= 168.75 && angleKreludor <= 191.24) {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[1]/td[1]/input")).click();
        }
        else if (angleKreludor >= 191.25 && angleKreludor <= 213.74) {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[1]/td[2]/input")).click();
        }
        else if (angleKreludor >= 213.75 && angleKreludor <= 236.24) {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[1]/td[3]/input")).click();
        }
        else if (angleKreludor >= 236.25 && angleKreludor <= 258.74) {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[1]/td[4]/input")).click();
        }
        else if (angleKreludor >= 258.75 && angleKreludor <= 281.24) {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[1]/td[5]/input")).click();
        }
        else if (angleKreludor >= 281.25 && angleKreludor <= 303.74) {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[1]/td[6]/input")).click();
        }
        else if (angleKreludor >= 303.75 && angleKreludor <= 326.24) {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[1]/td[7]/input")).click();
        }
        else if (angleKreludor >= 326.25 && angleKreludor <= 348.74) {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[1]/td[8]/input")).click();
        }
        else if (angleKreludor >= 348.75 && angleKreludor <= 360.0) {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/form/table/tbody/tr[2]/td[1]/input")).click();
        }

        Utils.sleepMode(5000);
        if (driver.getPageSource().contains("http://images.neopets.com/shenkuu/lunar/gnorbu_correct.gif")) {
            Utils.logMessage("Finished Lunar Puzzle.");
            return true;
        }
        Utils.logMessage("Failed Lunar Puzzle. Something went wrong.");
        return false;

    }
}