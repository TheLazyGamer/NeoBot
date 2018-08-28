package dailydoer.dailies;

import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

public class Solitaire
{
    public static boolean madeMove = false;
    public static int cardDiv = 2;
    public static boolean firstMove = true;

    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Starting runSolitaire");
        driver.get("http://www.neopets.com/games/sakhmet_solitaire/index.phtml");
        madeMove = false;
        cardDiv = 2;
        firstMove = true;

        if (Utils.isElementPresentXP("//input[@value='Continue Playing']", driver)) {
            driver.findElement(By.xpath("//input[@value='Continue Playing']")).click();
        }
        else {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/div/form/input[1]")).click();
            driver.findElement(By.xpath("//input[@value='Play Sakhmet Solitaire!']")).click();
        }

        while (true) {
            try {
                processDrawnCard(driver);

                if (madeMove || firstMove) {
                    //We are now done moving our drawn card. Onto the cards already on the board at the bottoms of their pile
                    processBottomCards(driver);

                    //We will now consolidate the top cards
                    processTopCards(driver);
                }
            }
            catch(NoSuchElementException ex) {
                driver.navigate().refresh();
            }
            firstMove = false;
            if (madeMove) {
                //Do nothing and reloop until it becomes false
                madeMove = false;
            }
            else {
                //Draw card. If there is no card, end the game (including checking if we hit max NP for the day)
                if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[2]/a/img", driver)) {
                    driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[2]/a/img")).click();
                    //Random ran = new Random();
                    //int pause = ran.nextInt(1000);
                    //sleepMode(1000+pause);
                    if (!Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[2]/a/img", driver)) {
                        cardDiv = 3;
                    }
                }
                else {
                    //Click collect winnings
                    driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[3]/div/form[1]/a/b")).click();
                    Utils.handleAlert(driver);
                    String completionText = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]")).getText();
                    if (completionText.contains("You have reached the")) {
                        break;
                    }
                    driver.findElement(By.xpath("//input[@value='Play Sakhmet Solitaire Again!']")).click();
                    madeMove = false;
                    cardDiv = 2;
                    firstMove = true;

                    driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/div/form/input[1]")).click();
                    driver.findElement(By.xpath("//input[@value='Play Sakhmet Solitaire!']")).click();
                }
            }
        }
        Utils.logMessage("Successfully ending runSolitaire");
        return true;
    }

    /**
     * Process the drawn card, sending it to an empty space or a pile.
     * @param driver The WebDriver
     * @see NeoDailies#runSolitaire(WebDriver) runSolitaire
     * @see NeoDailies#processBottomCards(WebDriver) processBottomCards
     * @see NeoDailies#processTopCards(WebDriver) processTopCards
     */
    private static void processDrawnCard(WebDriver driver) {
        boolean drawnCardPlayed = false;
        while (!drawnCardPlayed) {
            if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[3]/a/img", driver)) {
                String drawnCardImg = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[3]/a/img")).getAttribute("src");
                String drawnCardID = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[3]/a/img")).getAttribute("id");
                int drawnCardNum = -1;
                String drawnCardSuit = "";
                if (drawnCardID.contains("_")) {
                    drawnCardSuit = drawnCardID.substring(drawnCardID.indexOf("_") + 1, drawnCardID.length());
                    drawnCardNum = Integer.parseInt(drawnCardID.substring(0, drawnCardID.indexOf("_")));
                }
                String drawnCardColor = "";
                if (drawnCardSuit.equals("spades") || drawnCardSuit.equals("clubs")) {
                    drawnCardColor = "black";
                }
                else if (drawnCardSuit.equals("hearts") || drawnCardSuit.equals("diamonds")) {
                    drawnCardColor = "red";
                }

                Utils.logMessage("processDrawnCard drawnCardImg: " + drawnCardImg);

                for (int x = 5; x < 9; x++) {
                    String acePileCardImg = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[" + x + "]/a/img")).getAttribute("src");

                    if (acePileCardImg.contains("new_open")) {
                        if (drawnCardNum == 14) {
                            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[3]/a/img")).click();
                            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[" + x + "]/a/img")).click();
                            drawnCardPlayed = true;
                            madeMove = true;
                            break;
                        }
                    }
                    else {
                        int acePileCardNum = Integer.parseInt(acePileCardImg.substring(acePileCardImg.lastIndexOf("/") + 1, acePileCardImg.lastIndexOf("_")));
                        String acePileCardSuit = acePileCardImg.substring(acePileCardImg.lastIndexOf("_") + 1, acePileCardImg.lastIndexOf("."));

                        if (((acePileCardNum == 14 && drawnCardNum == 2) && drawnCardSuit.equals(acePileCardSuit)) || (acePileCardNum == drawnCardNum - 1 && drawnCardSuit.equals(acePileCardSuit))) {
                            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[3]/a/img")).click();
                            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[" + x + "]/a/img")).click();
                            drawnCardPlayed = true;
                            madeMove = true;
                            break;
                        }
                    }
                }

                if (!drawnCardPlayed) { //Let's put our drawn card into a pile!
                    for (int x = 2; x < 9; x++) {
                        int y = 1;
                        while (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/a[" + y + "]/img", driver)) {
                            y++;
                        }
                        y--;
                        String normalPileCardImg = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/a[" + y + "]/img")).getAttribute("src");
                        if (normalPileCardImg.contains("new_blank_card")) {
                            if (drawnCardNum == 13) {
                                driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[3]/a/img")).click();
                                driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/a[" + y + "]/img")).click();
                                drawnCardPlayed = true;
                                madeMove = true;
                                break;
                            }
                        }
                        else {
                            int normalPileCardNum = Integer.parseInt(normalPileCardImg.substring(normalPileCardImg.lastIndexOf("/") + 1, normalPileCardImg.lastIndexOf("_")));
                            String normalPileCardSuit = normalPileCardImg.substring(normalPileCardImg.lastIndexOf("_") + 1, normalPileCardImg.lastIndexOf("."));
                            String normalPileCardColor = "";
                            if (normalPileCardSuit.equals("spades") || normalPileCardSuit.equals("clubs")) {
                                normalPileCardColor = "black";
                            }
                            else if (normalPileCardSuit.equals("hearts") || normalPileCardSuit.equals("diamonds")) {
                                normalPileCardColor = "red";
                            }
                            if (normalPileCardNum == drawnCardNum + 1 && !normalPileCardColor.equals(drawnCardColor) && normalPileCardNum != 14) {
                                driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[3]/a/img")).click();
                                driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/a[" + y + "]/img")).click();
                                drawnCardPlayed = true;
                                madeMove = true;
                                break;
                            }
                        }
                    }
                }
                if (drawnCardPlayed) {
                    drawnCardPlayed = false;
                }
                else {
                    drawnCardPlayed = true;
                }
            }
            else {
                drawnCardPlayed = true;
            }
        }
    }

    /**
     * Process the cards at the bottom of piles, sending them to empty spaces or ace piles.
     * @param driver The WebDriver
     * @see NeoDailies#runSolitaire(WebDriver) runSolitaire
     * @see NeoDailies#processDrawnCard(WebDriver) processDrawnCard
     * @see NeoDailies#processTopCards(WebDriver) processTopCards
     */
    private static void processBottomCards(WebDriver driver) {
        for (int x = 2; x < 9; x++) {
            Utils.logMessage("processBottomCards processing bottom card");
            int y = 1;
            while (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/a[" + y + "]/img", driver)) {
                y++;
            }
            y--;

            String bottomPileCardImg = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/a[" + y + "]/img")).getAttribute("src");
            if (!bottomPileCardImg.contains("new_blank_card")) {
                int bottomPileCardNum = Integer.parseInt(bottomPileCardImg.substring(bottomPileCardImg.lastIndexOf("/") + 1, bottomPileCardImg.lastIndexOf("_")));

                if (bottomPileCardNum == 14) {
                    for (int n = 5; n < 9; n++) {
                        String acePileCardImg = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[" + n + "]/a/img")).getAttribute("src");

                        if (acePileCardImg.contains("new_open")) {
                            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/a[" + y + "]/img")).click();
                            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[" + n + "]/a/img")).click();
                            madeMove = true;
                            x = 1;
                            break;
                        }
                    }
                }
                else if (bottomPileCardNum == 13 && Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/img", driver)) {
                    for (int m = 2; m < 9; m++) {
                        int n = 1;
                        while (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + m + "]/div/a[" + n + "]/img", driver)) {
                            n++;
                        }
                        n--;
                        String possiblyEmptyPileCardImg = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + m + "]/div/a[" + n + "]/img")).getAttribute("src");
                        if (possiblyEmptyPileCardImg.contains("new_blank_card")) {
                            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/a[" + y + "]/img")).click();
                            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + m + "]/div/a[" + n + "]/img")).click();
                            madeMove = true;
                            x = 1;
                            break;
                        }
                    }
                }
                else { //The card in the bottom of the pile wasn't an ace or a king. Check if it can go into an Ace pile
                    String bottomPileCardSuit = bottomPileCardImg.substring(bottomPileCardImg.lastIndexOf("_") + 1, bottomPileCardImg.lastIndexOf("."));
                    Utils.logMessage("processBottomCards This is the bottom card: " + bottomPileCardImg);
                    for (int n = 5; n < 9; n++) {
                        String acePileCardImg = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[" + n + "]/a/img")).getAttribute("src");
                        Utils.logMessage("processBottomCards this is the acePileCardImg: " + acePileCardImg);
                        if (!acePileCardImg.contains("new_open")) {
                            String acePileCardSuit = acePileCardImg.substring(acePileCardImg.lastIndexOf("_") + 1, acePileCardImg.lastIndexOf("."));
                            int acePileCardNum = Integer.parseInt(acePileCardImg.substring(acePileCardImg.lastIndexOf("/") + 1, acePileCardImg.lastIndexOf("_")));
                            if (bottomPileCardSuit.equals(acePileCardSuit) && (acePileCardNum == bottomPileCardNum - 1 || acePileCardNum == bottomPileCardNum + 12)) {
                                driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/a[" + y + "]/img")).click();
                                driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[" + n + "]/a/img")).click();
                                madeMove = true;
                                x = 1;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Process the cards at the top of piles, sending them to empty spaces or other piles.
     * @param driver The WebDriver
     * @see NeoDailies#runSolitaire(WebDriver) runSolitaire
     * @see NeoDailies#processBottomCards(WebDriver) processBottomCards
     * @see NeoDailies#processDrawnCard(WebDriver) processDrawnCard
     */
    private static void processTopCards(WebDriver driver) {
        for (int x = 2; x < 9; x++) {
            if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/a[1]/img", driver)) {
                String topPileCardImg = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/a[1]/img")).getAttribute("src");
                if (!topPileCardImg.contains("new_blank_card")) {
                    int topPileCardNum = Integer.parseInt(topPileCardImg.substring(topPileCardImg.lastIndexOf("/") + 1, topPileCardImg.lastIndexOf("_")));
                    String topPileCardSuit = topPileCardImg.substring(topPileCardImg.indexOf("_") + 1, topPileCardImg.lastIndexOf("."));

                    String topPileCardColor = "";
                    if (topPileCardSuit.equals("spades") || topPileCardSuit.equals("clubs")) {
                        topPileCardColor = "black";
                    }
                    else if (topPileCardSuit.equals("hearts") || topPileCardSuit.equals("diamonds")) {
                        topPileCardColor = "red";
                    }

                    if (topPileCardNum == 13) {
                        //If there's a down card on top of the king
                        if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/img[1]", driver)) {
                            //Check for an empty spot
                            for (int m = 2; m < 9; m++) {
                                int n = 1;
                                while (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + m + "]/div/a[" + n + "]/img", driver)) {
                                    n++;
                                }
                                n--;
                                String possiblyEmptyPileCardImg = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + m + "]/div/a[" + n + "]/img")).getAttribute("src");
                                if (possiblyEmptyPileCardImg.contains("new_blank_card")) {
                                    driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/a[1]/img")).click();
                                    driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + m + "]/div/a[" + n + "]/img")).click();
                                    madeMove = true;
                                    x = 1;
                                    processBottomCards(driver);
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        for (int m = 2; m < 9; m++) {
                            int n = 1;
                            while (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + m + "]/div/a[" + n + "]/img", driver)) {
                                n++;
                            }
                            n--;
                            String bottomPileCardImg = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + m + "]/div/a[" + n + "]/img")).getAttribute("src");
                            if (!bottomPileCardImg.contains("new_blank_card")) {
                                int bottomPileCardNum = Integer.parseInt(bottomPileCardImg.substring(bottomPileCardImg.lastIndexOf("/") + 1, bottomPileCardImg.lastIndexOf("_")));
                                String bottomPileCardSuit = bottomPileCardImg.substring(bottomPileCardImg.lastIndexOf("_") + 1, bottomPileCardImg.lastIndexOf("."));
                                String bottomPileCardColor = "";
                                if (bottomPileCardSuit.equals("spades") || bottomPileCardSuit.equals("clubs")) {
                                    bottomPileCardColor = "black";
                                }
                                else if (bottomPileCardSuit.equals("hearts") || bottomPileCardSuit.equals("diamonds")) {
                                    bottomPileCardColor = "red";
                                }

                                if (bottomPileCardNum == topPileCardNum + 1 && !topPileCardColor.equals(bottomPileCardColor)) {
                                    driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + x + "]/div/a[1]/img")).click();
                                    driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[" + cardDiv + "]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[" + m + "]/div/a[" + n + "]/img")).click();
                                    madeMove = true;
                                    x = 1;
                                    processBottomCards(driver);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}