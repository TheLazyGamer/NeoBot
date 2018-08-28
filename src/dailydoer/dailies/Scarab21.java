package dailydoer.dailies;

import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;

public class Scarab21
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Starting runScarab21");
        driver.get("http://www.neopets.com/games/scarab21/index.phtml");

        if (Utils.isElementPresentXP("//input[@value='Play Scarab 21!!!']", driver)) {
            driver.findElement(By.xpath("//input[@value='Play Scarab 21!!!']")).click();
        }
        else {
            driver.findElement(By.xpath("//input[@value='Cancel Current Game']")).click();
            driver.findElement(By.xpath("//input[@value='Play Scarab 21!!!']")).click();
        }

        while (true) {
            //Random ran = new Random();
            //int pause = ran.nextInt(1000);
            //sleepMode(2000 + pause);
            //Collect Points Message (meaning game over)
            if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/div/a/b", driver)) {
                if (driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/div/a/b")).getText().contains("Collect Points")) {
                    //Click Collect Points
                    driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/div/a/b")).click();
                    String winMessage = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/p")).getText();
                    if (winMessage.contains("You have reached the")) {
                        break;
                    }
                    driver.findElement(By.xpath("//input[@value='Play Again!']")).click();
                }

            }

            String drawnCardImg = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[1]/table[3]/tbody/tr/td[2]/img")).getAttribute("src");

            int drawnCardNum = Integer.parseInt(drawnCardImg.substring(drawnCardImg.lastIndexOf("/") + 1, drawnCardImg.lastIndexOf("_")));
            int mathDrawnCardNum = -1;
            if (drawnCardNum == 14) {
                mathDrawnCardNum = 11;
            }
            else if (drawnCardNum == 11 || drawnCardNum == 12 || drawnCardNum == 13) {
                mathDrawnCardNum = 10;
            }
            else {
                mathDrawnCardNum = drawnCardNum;
            }

            ArrayList<String> columnPoints = new ArrayList<String>();
            String column1Points = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[2]/table/tbody/tr[3]/td[1]")).getText().trim();
            String column2Points = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[2]/table/tbody/tr[3]/td[2]")).getText().trim();
            String column3Points = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[2]/table/tbody/tr[3]/td[3]")).getText().trim();
            String column4Points = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[2]/table/tbody/tr[3]/td[4]")).getText().trim();
            String column5Points = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[2]/table/tbody/tr[3]/td[5]")).getText().trim();
            columnPoints.add(column1Points);
            columnPoints.add(column2Points);
            columnPoints.add(column3Points);
            columnPoints.add(column4Points);
            columnPoints.add(column5Points);

            int recommendedColumn = -1;
            for (int x = 0; x < columnPoints.size(); x++) {

                int columnPointA = -1;
                int columnPointB = -1;

                String columnPoint = columnPoints.get(x);
                if (columnPoint.contains("or")) {
                    columnPointA = Integer.parseInt(columnPoint.substring(0, columnPoint.indexOf(" ")));
                    columnPointB = Integer.parseInt(columnPoint.substring(columnPoint.lastIndexOf(" ") + 1, columnPoint.length()));
                }
                else {
                    columnPointA = Integer.parseInt(columnPoint);
                }

                if (drawnCardNum == 14) {
                    if (columnPointA == 10 || columnPointB == 10) {
                        if (driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[2]/table/tbody/tr[2]/td[" + (x + 1) + "]/img[1]")).getAttribute("src").contains("11_spades") &&
                                drawnCardImg.contains("14_spades")) {
                            recommendedColumn = x + 1;
                            break;
                        }
                        else if (!Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[2]/table/tbody/tr[2]/td[" + (x + 1) + "]/img[2]", driver)) {
                            recommendedColumn = x + 1;
                        }
                        else if (recommendedColumn == -1) {
                            recommendedColumn = x + 1;
                        }
                    }
                    else if (columnPointA == 20 || columnPointB == 20) {
                        recommendedColumn = x + 1;
                    }
                }
                else if (mathDrawnCardNum == 10) {
                    if (columnPointA == 11 || columnPointB == 11) {
                        if (driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[2]/table/tbody/tr[2]/td[" + (x + 1) + "]/img[1]")).getAttribute("src").contains("14_spades") &&
                                drawnCardImg.contains("11_spades")) {
                            recommendedColumn = x + 1;
                            break;
                        }
                        else if (!Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[2]/table/tbody/tr[2]/td[" + (x + 1) + "]/img[2]", driver)) {
                            recommendedColumn = x + 1;
                            break;
                        }
                        else {
                            recommendedColumn = x + 1;
                            break;
                        }
                    }
                    else if (columnPointA == 0) {
                        recommendedColumn = x + 1;
                    }
                }
                else if (mathDrawnCardNum + columnPointA == 21 || mathDrawnCardNum + columnPointB == 21) {
                    recommendedColumn = x + 1;
                    break;
                }
                else if (mathDrawnCardNum + columnPointA == 11 || mathDrawnCardNum + columnPointB == 11) {
                    recommendedColumn = x + 1;
                }
                else if (mathDrawnCardNum == 10 && columnPointA == 0 && recommendedColumn == -1) {
                    recommendedColumn = x + 1;
                }
            }

            if (recommendedColumn != -1) {
                //We found a good column, let's move our card there
                driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[2]/table/tbody/tr[1]/td[" + recommendedColumn + "]/a/img")).click();
            }
            else {
                //we didn't find a good column. Just place it where we have the lowest number (should that include 0?)
                int lowestNumber = 999999;
                int lowestNumberColumn = 0;
                for (int x = 0; x < columnPoints.size(); x++) {
                    int columnPointA = -1;

                    String columnPoint = columnPoints.get(x);
                    if (columnPoint.contains("or")) {
                        columnPointA = Integer.parseInt(columnPoint.substring(0, columnPoint.indexOf(" ")));
                    }
                    else {
                        columnPointA = Integer.parseInt(columnPoint);
                    }

                    if (mathDrawnCardNum == 11) {
                        mathDrawnCardNum = 1;
                    }

                    Utils.logMessage("This is mathDrawnCardNum: " + mathDrawnCardNum);

                    if ((columnPointA < lowestNumber) && (columnPointA + mathDrawnCardNum <= 21) && columnPointA != 0 && columnPointA != 1) {
                        if (columnPointA == 10) {
                            if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[2]/table/tbody/tr[2]/td[" + (x + 1) + "]/img[2]", driver)) {
                                lowestNumber = columnPointA;
                                lowestNumberColumn = x + 1;
                            }
                        }
                        else {
                            if (columnPointA != 11) {
                                lowestNumber = columnPointA;
                                lowestNumberColumn = x + 1;
                            }
                        }
                    }
                    else if (columnPointA == 0 && drawnCardNum == 14) {
                        lowestNumber = columnPointA;
                        lowestNumberColumn = x + 1;
                        break;
                    }
                }

                if (lowestNumberColumn == 0) {
                    for (int x = 0; x < columnPoints.size(); x++) {
                        int columnPointA = -1;

                        String columnPoint = columnPoints.get(x);
                        if (columnPoint.contains("or")) {
                            columnPointA = Integer.parseInt(columnPoint.substring(0, columnPoint.indexOf(" ")));
                        }
                        else {
                            columnPointA = Integer.parseInt(columnPoint);
                        }

                        if (mathDrawnCardNum == 11) {
                            mathDrawnCardNum = 1;
                        }

                        if ((columnPointA < lowestNumber) && (columnPointA + mathDrawnCardNum <= 21) && columnPointA != 1) {
                            if (columnPointA == 10) {
                                if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[2]/table/tbody/tr[2]/td[" + (x + 1) + "]/img[2]", driver)) {
                                    lowestNumber = columnPointA;
                                    lowestNumberColumn = x + 1;
                                }
                            }
                            else if (columnPointA == 0 && mathDrawnCardNum == 10) {
                                lowestNumber = columnPointA;
                                lowestNumberColumn = x + 1;
                            }
                            else {
                                lowestNumber = columnPointA;
                                lowestNumberColumn = x + 1;
                            }
                        }
                    }
                }

                if (lowestNumberColumn == 0) {
                    for (int x = 0; x < columnPoints.size(); x++) {
                        int columnPointA = -1;

                        String columnPoint = columnPoints.get(x);
                        if (columnPoint.contains("or")) {
                            columnPointA = Integer.parseInt(columnPoint.substring(0, columnPoint.indexOf(" ")));
                        }
                        else {
                            columnPointA = Integer.parseInt(columnPoint);
                        }

                        if (mathDrawnCardNum == 11) {
                            mathDrawnCardNum = 1;
                        }

                        if ((columnPointA < lowestNumber) && (columnPointA + mathDrawnCardNum <= 21)) {
                            lowestNumber = columnPointA;
                            lowestNumberColumn = x + 1;
                            break;
                        }
                    }
                }

                driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/div/div[2]/center/table/tbody/tr/td[2]/table/tbody/tr[1]/td[" + lowestNumberColumn + "]/a/img")).click();
            }
        }
        Utils.logMessage("Successfully ending runScarab21");
        return true;
    }
}