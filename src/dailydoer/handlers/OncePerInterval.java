package dailydoer.handlers;

import dailydoer.Utils;
import dailydoer.dailies.TrudysSurprise;
import dailydoer.hourlies.*;

import org.openqa.selenium.WebDriver;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.Instant;

public class OncePerInterval
{
    public static void run(WebDriver driver, String xmlNode, long requiredInterval) throws Exception
    {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new File("dailydoer/resources/PreviousRuns.xml"));
        doc.getDocumentElement().normalize();

        NodeList nlList = doc.getDocumentElement().getElementsByTagName(xmlNode).item(0).getChildNodes();
        Node lastRunNode = (Node) nlList.item(1);

        long lastRunEpoch = Long.parseLong(doc.getDocumentElement().getElementsByTagName(xmlNode).item(0).getTextContent().trim());

        long currentEpoch = Instant.now().getEpochSecond();

        boolean successTask = false;

        if ((currentEpoch - lastRunEpoch) > requiredInterval)
        {
            switch(xmlNode) {
                case "BuriedTreasure":
                    successTask = BuriedTreasure.run(driver);
                    break;
                case "ColtzansShrine":
                    successTask = ColtzansShrine.run(driver);
                    break;
                case "DeleteItems":
                    successTask = DeleteItems.run(driver);
                    break;
                case "DubloonTraining":
                    successTask = DubloonTraining.run(driver);
                    break;
                case "Employment":
                    successTask = Employment.run(driver);
                    break;
                case "Fishing":
                    successTask = Fishing.run(driver);
                    break;
                case "GraveDanger":
                    successTask = GraveDanger.run(driver);
                    break;
                case "GuessMarrow":
                    successTask = GuessMarrow.run(driver);
                    break;
                case "HealingSprings":
                    successTask = HealingSprings.run(driver);
                    break;
                case "IslandTraining":
                    successTask = IslandTraining.run(driver);
                    break;
                case "KitchenQuest":
                    successTask = KitchenQuest.run(driver);
                    break;
                case "NeoMail":
                    successTask = NeoMail.run(driver);
                    break;
                case "RepriceItems":
                    successTask = StockItems.run(driver, "Reprice");
                    break;
                case "SecondHandShoppe":
                    successTask = SecondHandShoppe.run(driver);
                    break;
                case "SellStock":
                    successTask = SellStock.run(driver);
                    break;
                case "ShopTill":
                    successTask = ShopTill.run(driver);
                    break;
                case "StockItems":
                    successTask = StockItems.run(driver, "Stock");
                    break;
                case "SymolHole":
                    successTask = SymolHole.run(driver);
                    break;
                case "Tarla":
                    successTask = Tarla.run(driver);
                    break;
                case "WheelOfExcitement":
                    successTask = WheelOfExcitement.run(driver);
                    break;
                case "WheelOfMediocrity":
                    successTask = WheelOfMediocrity.run(driver);
                    break;
                case "WheelOfMisfortune":
                    successTask = WheelOfMisfortune.run(driver);
                    break;
                case "WishingWell":
                    successTask = WishingWell.run(driver);
                    break;
            }

            if (successTask) {
                lastRunNode.setTextContent(String.valueOf(currentEpoch));

                //write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File("dailydoer/resources/PreviousRuns.xml"));
                transformer.transform(source, result);

                Utils.sleepMode(5000);
            }
        }
    }
}