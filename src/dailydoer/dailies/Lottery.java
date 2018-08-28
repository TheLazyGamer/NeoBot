package dailydoer.dailies;

import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Arrays;

public class Lottery
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Starting runLottery");
        //https://www.andrew.cmu.edu/user/kmliu/neopets/lottery2.html Same algorithm but in Javascript
        driver.get("http://www.neopets.com/games/lottery.phtml");

        int[] arr = new int[30];
        for (int i = 0; i < 30; i++) {
            arr[i] = i + 1;
        }

        int[] arr1 = shuffle(arr);
        int[] arr2 = leaf(Arrays.copyOfRange(arr1, 0, 15), Arrays.copyOfRange(arr1, 15, 30));
        arr2 = leaf(Arrays.copyOfRange(arr2, 0, 15), Arrays.copyOfRange(arr2, 15, 30));
        int[] arr3 = leaf(Arrays.copyOfRange(arr2, 0, 15), Arrays.copyOfRange(arr2, 15, 30));
        arr3 = leaf(Arrays.copyOfRange(arr3, 0, 15), Arrays.copyOfRange(arr3, 15, 30));
        int[] arr4 = leaf(Arrays.copyOfRange(arr3, 0, 15), Arrays.copyOfRange(arr3, 15, 30));
        arr4 = leaf(Arrays.copyOfRange(arr4, 0, 15), Arrays.copyOfRange(arr4, 15, 30));
        ArrayList<Integer> marr = new ArrayList<Integer>();
        for (int x = 0; x < arr1.length; x++) {
            marr.add(arr1[x]);
        }
        for (int x = 0; x < arr2.length; x++) {
            marr.add(arr2[x]);
        }
        for (int x = 0; x < arr3.length; x++) {
            marr.add(arr3[x]);
        }
        for (int x = 0; x < arr4.length; x++) {
            marr.add(arr4[x]);
        }

        int i = 0;
        for (; i < 20; i++) {
            System.out.print("Ticket " + (i + 1) + ": ");
            int j = 0;
            driver.findElement(By.name("one")).clear();
            driver.findElement(By.name("one")).sendKeys("" + marr.get(6 * i + j));
            j++;
            driver.findElement(By.name("two")).clear();
            driver.findElement(By.name("two")).sendKeys("" + marr.get(6 * i + j));
            j++;
            driver.findElement(By.name("three")).clear();
            driver.findElement(By.name("three")).sendKeys("" + marr.get(6 * i + j));
            j++;
            driver.findElement(By.name("four")).clear();
            driver.findElement(By.name("four")).sendKeys("" + marr.get(6 * i + j));
            j++;
            driver.findElement(By.name("five")).clear();
            driver.findElement(By.name("five")).sendKeys("" + marr.get(6 * i + j));
            j++;
            driver.findElement(By.name("six")).clear();
            driver.findElement(By.name("six")).sendKeys("" + marr.get(6 * i + j));
            driver.findElement(By.xpath("//input[@value='Buy a Lottery Ticket!']")).click();
            Utils.sleepMode(5000);
        }
        if (i == 20) {
            Utils.logMessage("Successfully ending runLottery");
            return true;
        }
        Utils.logMessage("Failed ending runLottery");
        return false;
    }

    /**
     * Used by runLottery to generate an array of numbers.
     * @param a The first array of numbers
     * @param b second first array of numbers
     * @return The new array
     * @see Lottery#run(WebDriver) runLottery
     */
    private static int[] leaf(int[] a, int[] b) {
        int[] res = new int[30];
        for (int i = 0; i < a.length; i++) {
            res[i * 2] = a[i];
            res[i * 2 + 1] = b[i];
        }
        return res;
    }

    /**
     * Used by runLottery to randomize (shuffle) an array.
     * @param o The array of numbers
     * @return The newly shuffled array
     * @see Lottery#run(WebDriver) runLottery
     */
    private static int[] shuffle(int[] o) {
        int j = 0;
        int x = 0;
        int i = o.length;
        while (i > 0) {
            j = (int) Math.floor((Math.random() * i));
            x = o[--i];
            o[i] = o[j];
            o[j] = x;
        }
        return o;
    }
}