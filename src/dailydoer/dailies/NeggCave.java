package dailydoer.dailies;

import dailydoer.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class NeggCave
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Running Negg Cave...");
        driver.get("http://www.neopets.com/shenkuu/neggcave/");
        Utils.sleepMode(5000);
        driver.get("http://www.neopets.com/shenkuu/neggcave/");

        String neggBreakerSource = driver.getPageSource();

        String jsCode = "return SolvePuzzle(arguments[0]); function getArr(nRows, nCols, init) { var clueItems = []; "
                + "for (var j = 0; j < nRows; ++j) { var row = []; for (var k = 0; k < nCols; ++k) { row.push(init); } "
                + "clueItems.push(row); } return clueItems; } function clueFits(guess, clue) { for (var x = 0; x <= "
                + "3 - clue.length; ++x) { for (var y = 0; y <= 3 - clue[0].length; ++y) { var works = true; for (var "
                + "dx = 0; dx < clue.length; ++dx) { for (var dy = 0; dy < clue[0].length; ++dy) { var guessSqr = "
                + "guess[x + dx][y + dy]; var clueSqr = clue[dx][dy]; if (!((clueSqr[0] == 3 || guessSqr % 3 == "
                + "clueSqr[0]) && (clueSqr[1] == 3 || (Math.floor(guessSqr / 3) == clueSqr[1])))) { works = false; dx "
                + "= 3; break; } } } if (works) { return true; } } } return false; } function SolvePuzzle(source) { "
                + "source = source.split(\"<table class=\\\"mnc_clue_table\\\">\"); var clueSquares = [ [\"mnc_negg_clue_s0c0\", "
                + "[0, 0]], [\"mnc_negg_clue_s0c1\", [0, 1]], [\"mnc_negg_clue_s0c2\", [0, 2]], [\"mnc_negg_clue_s0cX\", [0, 3]], "
                + "[\"mnc_negg_clue_s1c0\", [1, 0]], [\"mnc_negg_clue_s1c1\", [1, 1]], [\"mnc_negg_clue_s1c2\", [1, 2]], "
                + "[\"mnc_negg_clue_s1cX\", [1, 3]], [\"mnc_negg_clue_s2c0\", [2, 0]], [\"mnc_negg_clue_s2c1\", [2, 1]], "
                + "[\"mnc_negg_clue_s2c2\", [2, 2]], [\"mnc_negg_clue_s2cX\", [2, 3]], [\"mnc_negg_clue_sXc0\", [3, 0]], "
                + "[\"mnc_negg_clue_sXc1\", [3, 1]], [\"mnc_negg_clue_sXc2\", [3, 2]], [\"mnc_negg_clue_sXcX\", [3, 3]], "
                + "[\"<td class=\\\"empty\\\"></td>\", [3, 3]], ]; var out = \"\"; var clues = []; for (var i = 1; i < "
                + "source.length; ++i) { var table = source[i].substring(0, source[i].indexOf(\"</table>\")); var nItems = "
                + "table.split(\"<td\").length - 1; var nRows = table.split(\"<tr>\").length - 1; var nCols = nItems / nRows; "
                + "var clueItems = getArr(nRows, nCols, -1); var m = 0; for (var k = 0; k < table.length; ++k) { for (var j = "
                + "0; j < clueSquares.length; ++j) { var clueStr = clueSquares[j][0]; var clueDesc = clueSquares[j][1]; if "
                + "(table.substring(k, k + clueStr.length) == clueStr) { clueItems[Math.floor(m / nCols)][m % nCols] = clueDesc; "
                + "m++; break; } } } clues.push(clueItems); } var permCount = 1; var N = 9; for (var i = 1; i <= N; ++i) { "
                + "permCount *= i; } for (var perm0 = 0; perm0 < permCount; ++perm0) { var perm = perm0; var curPerm = permCount; "
                + "var guess = []; for (var i = 0; i < N; ++i) { guess.push(-1); } for (var i = N; i >= 1; --i) { curPerm /= i; "
                + "var place = Math.floor(perm / curPerm); var m = 0; for (var j = 0; j < N; ++j) { if (guess[j] == -1) { if (m == "
                + "place) { guess[j] = i - 1; } m++; } } perm %= curPerm; } var guess1 = getArr(3, 3, -1); for (var i = 0; i < N; "
                + "++i) { guess1[Math.floor(i / 3)][i % 3] = guess[i]; } var isSolution = true; for (var i = 0; i < clues.length; "
                + "++i) { if (!clueFits(guess1, clues[i])) { isSolution = false; break; } } if (isSolution) { return guess; } } "
                + "return [9, 9, 9, 9, 9, 9, 9, 9, 9]; } ";

        JavascriptExecutor js = (JavascriptExecutor) driver;
        Object jsReturn = js.executeScript(jsCode, neggBreakerSource);
        String[] jsValues = jsReturn.toString().replace(" ", "").replace("[", "").replace("]", "").split(",");
        for (int x = 0; x < jsValues.length; x++) {
            String neggBoxID = "";
            if (x == 0) {
                neggBoxID = "mnc_grid_cell_0_0";
            }
            else if (x == 1) {
                neggBoxID = "mnc_grid_cell_0_1";
            }
            else if (x == 2) {
                neggBoxID = "mnc_grid_cell_0_2";
            }
            else if (x == 3) {
                neggBoxID = "mnc_grid_cell_1_0";
            }
            else if (x == 4) {
                neggBoxID = "mnc_grid_cell_1_1";
            }
            else if (x == 5) {
                neggBoxID = "mnc_grid_cell_1_2";
            }
            else if (x == 6) {
                neggBoxID = "mnc_grid_cell_2_0";
            }
            else if (x == 7) {
                neggBoxID = "mnc_grid_cell_2_1";
            }
            else if (x == 8) {
                neggBoxID = "mnc_grid_cell_2_2";
            }

            int jsValue = Integer.parseInt(jsValues[x]);

            if (jsValue == 0) { //blue water
                driver.findElement(By.id("mnc_parch_ui_color_0")).click(); //Click blue
                driver.findElement(By.id("mnc_parch_ui_symbol_0")).click(); //Click water
            }
            else if (jsValue == 1) { //blue fire
                driver.findElement(By.id("mnc_parch_ui_color_0")).click(); //Click blue
                driver.findElement(By.id("mnc_parch_ui_symbol_1")).click(); //Click fire
            }
            else if (jsValue == 2) { //blue wind
                driver.findElement(By.id("mnc_parch_ui_color_0")).click(); //Click blue
                driver.findElement(By.id("mnc_parch_ui_symbol_2")).click(); //Click wind
            }
            else if (jsValue == 3) { //red water
                driver.findElement(By.id("mnc_parch_ui_color_1")).click(); //Click red
                driver.findElement(By.id("mnc_parch_ui_symbol_0")).click(); //Click water
            }
            else if (jsValue == 4) { //red fire
                driver.findElement(By.id("mnc_parch_ui_color_1")).click(); //Click red
                driver.findElement(By.id("mnc_parch_ui_symbol_1")).click(); //Click fire
            }
            else if (jsValue == 5) { //red wind
                driver.findElement(By.id("mnc_parch_ui_color_1")).click(); //Click red
                driver.findElement(By.id("mnc_parch_ui_symbol_2")).click(); //Click wind
            }
            else if (jsValue == 6) { //yellow water
                driver.findElement(By.id("mnc_parch_ui_color_2")).click(); //Click yellow
                driver.findElement(By.id("mnc_parch_ui_symbol_0")).click(); //Click water
            }
            else if (jsValue == 7) { //yellow fire
                driver.findElement(By.id("mnc_parch_ui_color_2")).click(); //Click yellow
                driver.findElement(By.id("mnc_parch_ui_symbol_1")).click(); //Click fire
            }
            else if (jsValue == 8) { //yellow wind
                driver.findElement(By.id("mnc_parch_ui_color_2")).click(); //Click yellow
                driver.findElement(By.id("mnc_parch_ui_symbol_2")).click(); //Click wind
            }

            driver.findElement(By.id(neggBoxID)).click(); //Click grid cell
            driver.findElement(By.id("mnc_parch_ui_clear_text")).click(); //Click Clear to reset shape and color selection
        }
        driver.findElement(By.id("mnc_negg_submit_text")).click(); //Click Submit
        Utils.sleepMode(20000);
        Utils.logMessage("Finished Negg Cave.");
        return true;
    }
}