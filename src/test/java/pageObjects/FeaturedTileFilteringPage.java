package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import javax.swing.*;

public class FeaturedTileFilteringPage {
    private WebDriver driver;

    @FindBy(name = "commonName")
    WebElement animalSearchInputBox;

    public FeaturedTileFilteringPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }
    @Test
    public void clickIAgree(){
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.cssSelector("a.iAgreeButton"))));
            driver.findElement(By.cssSelector("a.iAgreeButton")).click();
        } catch (Exception e){
            System.out.println("Agree Button Not Present");
        }
    }
    public void setAnimal(String animalText){
        waitForSearchResults();
        animalSearchInputBox.sendKeys(animalText);
    }
    public void setMaxLifeSpan(int maxLifeSpan) throws Exception{
        waitForSearchResults();
        while(true) {
            WebElement sliderButton = driver.findElement(By.className("hSliderThumb"));
            Actions builder = new Actions(driver);
            builder.dragAndDropBy(sliderButton,-5,0).build().perform();
            driver.findElement(By.tagName("body")).click();
            WebElement element = driver.findElement(By.className("sliderValueText"));
            String value = element.getText();
            int currSize = Integer.parseInt(value);
            if(currSize == maxLifeSpan)
                break;
        }
    }
    public void sortBy(String sortCriteria) throws Exception{
        waitForSearchResults();
        WebElement element = driver.findElements(By.xpath("//table[@class='selectItemLiteControl']//div[@role='presentation']")).get(1);
        scrollElementIntoView(element);
        element.click();
        List<WebElement> sortByDropDownElements = driver.findElements(By.xpath("//tr[@role='option']//td//div[@role='presentation']"));
        for(WebElement dropDownElement : sortByDropDownElements){
            if(dropDownElement.getText().trim().equalsIgnoreCase(sortCriteria)){
                dropDownElement.click();
                break;
            }
        }
    }
    public void checkAscending(){
        waitForSearchResults();
        WebElement checkbox = driver.findElement(By.xpath("//span[@class='checkboxFalse']"));
        scrollElementIntoView(checkbox);
        checkbox.click();
    }
    public int getResultsCount(){
        waitForSearchResults();
        return driver.findElements(By.xpath("//div[@class='simpleTile']")).size();
    }

    public void waitForSearchResults() {
        try {
            Thread.sleep(2000);
        } catch (Exception e){
        }
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='simpleTile']")));
    }
    private void scrollElementIntoView(WebElement element){
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].scrollIntoView(true);", element);
    }
}
