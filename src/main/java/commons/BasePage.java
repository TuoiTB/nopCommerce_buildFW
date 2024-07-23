package commons;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public class BasePage {
    //Action: click, sendkey, select,...

    protected void openUrl(WebDriver driver, String url){
        driver.get(url);
    }
    protected String getPageTitle(WebDriver driver){
        return driver.getTitle();
    }
    protected String getCurrentUrl(WebDriver driver){
        return driver.getCurrentUrl();
    }
    protected String getPageSource(WebDriver driver){
        return driver.getPageSource();
    }
    protected void backToPage(WebDriver driver){
        driver.navigate().back();
    }
    protected void refreshCurrentPage(WebDriver driver){
        driver.navigate().refresh();
    }
    protected Set<Cookie> getBrowserCookies(WebDriver driver){
        return driver.manage().getCookies();
    }
    protected void setCookies(WebDriver driver, Set<Cookie> cookies){
        for (Cookie cookie : cookies){
            driver.manage().addCookie(cookie);
        }
    }
    protected void deleteCookies(WebDriver driver){
        driver.manage().deleteAllCookies();
    }
    protected Alert waitForAlertPresence(WebDriver driver){
        return new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.alertIsPresent());
    }
    protected void acceptToAlert(WebDriver driver){
        waitForAlertPresence(driver).accept();
    }
    protected void cancelToAlert(WebDriver driver){
        waitForAlertPresence(driver).dismiss();
    }
    protected String getTextAlert(WebDriver driver){
        return waitForAlertPresence(driver).getText();
    }
    protected void sendKeyToAlert(WebDriver driver, String valueToSend){
        waitForAlertPresence(driver).sendKeys(valueToSend);
    }
    //Window handle
    //Only use 2 windows/tabs
    protected void switchToWindowById(WebDriver driver, String windowId){
        Set<String> allIDs = driver.getWindowHandles();
        for (String id : allIDs) {
            if(!id.equals(windowId)) {
                driver.switchTo().window(id);
                break;
            }
        }
    }
    //Can use more than 2 windows/tabs
    protected void switchWindowByTitle(WebDriver driver, String expectedTitle){
        Set<String> allIds = driver.getWindowHandles();
        for (String id: allIds){
            driver.switchTo().window(id);
            String actualTitle = driver.getTitle();
            if (actualTitle.equals(expectedTitle)){
                break;
            }
        }
    }
    //Close all windows/tabs, except begining parent window/tab
    protected void closeAllWindowsWithoutParent(WebDriver driver, String expectedId){
        Set<String> allIds = driver.getWindowHandles();
        for (String id: allIds) {
            if (!id.equals(expectedId)) {
                driver.switchTo().window(id);
                driver.close();
            }
        }
    }

    //Selenium Web Element Function
    //Dynamic locator
    protected By getByLocator (String locatorValue){
        By by = null;
        if (locatorValue.startsWith("xpath=") || locatorValue.startsWith("Xpath=")
                || locatorValue.startsWith("xPath=") || locatorValue.startsWith("XPATH=")){
            by = By.xpath(locatorValue.substring(6));
        }
        if (locatorValue.startsWith("css=") || locatorValue.startsWith("CSS=")
                || locatorValue.startsWith("Css=") || locatorValue.startsWith("cSS=")){
            by = By.cssSelector(locatorValue.substring(4));
        }
        if (locatorValue.startsWith("id=") || locatorValue.startsWith("Id=")
                || locatorValue.startsWith("iD=") || locatorValue.startsWith("ID=")){
            by = By.id(locatorValue.substring(3));
        }
        if (locatorValue.startsWith("name=") || locatorValue.startsWith("Name=")
                || locatorValue.startsWith("NAME=") || locatorValue.startsWith("naMe=")){
            by = By.name(locatorValue.substring(5));
        }
        if (locatorValue.startsWith("class=") || locatorValue.startsWith("Class=")
                || locatorValue.startsWith("CLASS=") || locatorValue.startsWith("cLass=")){
            by = By.className(locatorValue.substring(6));
        }
        if (locatorValue.startsWith("tagName=") || locatorValue.startsWith("tagname=")
                || locatorValue.startsWith("TagName=") || locatorValue.startsWith("TAGNAME=")){
            by = By.tagName(locatorValue.substring(8));
        } else {
            throw new RuntimeException("Locator type is not valid");
        }
        return by;
    }
    protected String getDynamicLocator(String locator, String... restParams){
        return String.format(locator, (Object[]) restParams);
    }
    protected WebElement getElement(WebDriver driver, String locator) {
        return driver.findElement(getByLocator(locator));
    }
    protected List<WebElement> getListElement (WebDriver driver, String locator){
        return driver.findElements(getByLocator(locator));
    }

    protected void clickToElement(WebDriver driver, String locator){
        getElement(driver, locator).click();
    }
    protected void clickToElement(WebDriver driver, String locator, String... restParams){
        getElement(driver, getDynamicLocator(locator, restParams)).click();
    }
    protected void sendkeyToElement(WebDriver driver, String locator, String valueToSend){
        getElement(driver, locator).clear();
        getElement(driver, locator).sendKeys(valueToSend);
    }
    protected void sendkeyToElement(WebDriver driver, String locator, String valueToSend, String... restParams){
        getElement(driver, getDynamicLocator(locator, restParams)).clear();
        getElement(driver, getDynamicLocator(locator, restParams)).sendKeys(valueToSend);
    }
    protected void selectDropdown(WebDriver driver, String locator, String itemText){
        new Select(getElement(driver, locator)).selectByVisibleText(itemText);
    }
    protected void selectDropdown(WebDriver driver, String locator, String itemText, String... restParams){
        new Select(getElement(driver, getDynamicLocator(locator, restParams))).selectByVisibleText(itemText);
    }
    protected String getFirstSelectedOptionText(WebDriver driver, String locator){
        return new Select(getElement(driver, locator)).getFirstSelectedOption().getText();
    }
    protected boolean isDropdownMultiple(WebDriver driver, String locator){
        return new Select(getElement(driver, locator)).isMultiple();
    }
    protected void selectItemInCustomDropdown(WebDriver driver, String xpathParent, String xpathChild, String expectedText){
        
    }

}

