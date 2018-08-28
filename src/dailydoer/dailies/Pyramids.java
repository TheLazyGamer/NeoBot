package dailydoer.dailies;

import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class Pyramids
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Starting runPyramids");
        driver.get("http://www.neopets.com/games/pyramids/index.phtml");

        if (Utils.isElementPresentXP("//input[@value='Play Pyramids!']", driver)) {
            driver.findElement(By.xpath("//input[@value='Play Pyramids!']")).click();
        }
        else {
            driver.findElement(By.xpath("//input[@value='Continue Playing']")).click();
        }

        while (true) {
            boolean cardsToPlay = true;

            while (cardsToPlay) {
                try {

                    List<WebElement> boards = driver.findElements(By.tagName("table"));
                    WebElement board = boards.get(11); //table containing the game
                    List<WebElement> cards = board.findElements(By.tagName("img"));
                    String curTemp = cards.get(1).getAttribute("src");
                    int cur = Integer.parseInt(curTemp.substring(curTemp.lastIndexOf("/") + 1, curTemp.indexOf("_"))); //current card to play on
                    ArrayList<Integer> face = new ArrayList<Integer>(); //array for comparing playable cards to the topmost card on deck
                    ArrayList<String> vis = new ArrayList<String>(); //array containing the same playable cards, for playing them.
                    for (int x = 2; x < cards.size(); x++) {
                        String cardImageSrc = cards.get(x).getAttribute("src");
                        if (!cardImageSrc.contains("pyramid") && !cardImageSrc.contains("blank")) {
                            face.add(Integer.parseInt(cardImageSrc.substring(cardImageSrc.lastIndexOf("/") + 1, cardImageSrc.indexOf("_"))));
                            vis.add(cardImageSrc.substring(cardImageSrc.lastIndexOf("/"), cardImageSrc.lastIndexOf(".")));
                        }
                    }
                    Utils.logMessage("In Pyramids about to find a playable");
                    int x = face.size() - 1;

                    for (; x >= 0; x--) {
                        int faceUpPlayableCard = face.get(x);
                        if ((cur == 2 && (faceUpPlayableCard == 14 || faceUpPlayableCard == 3)) || (cur == 14 && (faceUpPlayableCard == 13 || faceUpPlayableCard == 2)) || (faceUpPlayableCard == cur - 1 || faceUpPlayableCard == cur + 1)) {
                            String faceUpPlayableCardFull = vis.get(x);
                            for (int y = 0; y < cards.size(); y++) {
                                if (cards.get(y).getAttribute("src").contains(faceUpPlayableCardFull)) {
                                    cards.get(y).click();
                                    y = 9999;
                                    x = -9999;
                                }
                            }
                        }
                    }
                    if (x >= -1) {
                        cardsToPlay = false;
                    }
                }
                catch(Exception ex) {
                    Utils.logMessage("Exception occured in Pyramids");
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    ex.printStackTrace(pw);
                    String sStackTrace = sw.toString(); //stacktrace as a string
                    Utils.logMessage(sStackTrace);
                    driver.get("http://www.neopets.com/games/pyramids/index.phtml");
                    driver.findElement(By.xpath("//input[@value='Cancel Current Game']")).click();
                    driver.findElement(By.xpath("//input[@value='Play Pyramids!']")).click();
                }
            }

            try {
                if (!Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/p/font/b/a", driver)) {
                    driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr[2]/td/table/tbody/tr[1]/td/a")).click();
                    Utils.sleepMode(1250);
                }
                else { //Collect Points is present meaning game is over
                    Utils.sleepMode(5000);
                    driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/p/font/b/a")).click();
                    Utils.sleepMode(5000);
                    String winText = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]")).getText();
                    if (winText.contains("You have reached the")) {
                        break;
                    }
                    driver.get("http://www.neopets.com/games/pyramids/index.phtml");
                    Utils.sleepMode(5000);

                    if (Utils.isElementPresentXP("//input[@value='Play Pyramids!']", driver)) {
                        driver.findElement(By.xpath("//input[@value='Play Pyramids!']")).click();
                    }
                    else {
                        driver.findElement(By.xpath("//input[@value='Continue Playing']")).click();
                    }
                    Utils.sleepMode(5000);
                }
            }
            catch(NoSuchElementException ex) {
                driver.navigate().refresh();
            }
        }
        Utils.logMessage("Successfully ending runPyramids");
        return true;
    }
}