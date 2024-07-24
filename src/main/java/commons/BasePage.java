package commons;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
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
        return new WebDriverWait(driver, Duration.ofSeconds(longTimeout)).until(ExpectedConditions.alertIsPresent());
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
        getElement(driver, xpathParent).click();
        List<WebElement> allItems = new WebDriverWait(driver, Duration.ofSeconds(longTimeout)).until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(getByLocator(xpathChild)));
        for (WebElement tempElement : allItems) {
            if (tempElement.getText().equals(expectedText)) {

                ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoViewIfNeeded(true);", tempElement);
                sleepInSecond(5);

                tempElement.click();
                break;
            }
        }
    }
    protected String getElementText(WebDriver driver, String locator){
        return getElement(driver, locator).getText();
    }
    protected String getElementText(WebDriver driver, String locator, String...restParams){
        return getElement(driver, getDynamicLocator(locator, restParams)).getText();
    }
    protected String getElementAttribute(WebDriver driver, String locator, String attributeName){
        return getElement(driver, locator).getAttribute(attributeName);
    }
    protected String getElementAttribute(WebDriver driver, String locator, String attributeName, String...restParams){
        return getElement(driver, getDynamicLocator(locator, restParams)).getAttribute(attributeName);
    }
    protected String getElementCssValue(WebDriver driver, String locator, String propertyName){
        return getElement(driver,locator).getCssValue(propertyName);
    }
    protected int getListElementSize(WebDriver driver, String locator){
        return getListElement(driver, locator).size();
    }
    protected boolean isElementSelected(WebDriver driver, String locator){
        return getElement(driver, locator).isSelected();
    }
    protected void checkToCheckboxOrRadio(WebDriver driver, String locator){
        if (!isElementSelected(driver, locator)){
            clickToElement(driver, locator);
        }
    }
    protected void checkToCheckboxOrRadio(WebDriver driver, String locator, String...restParams){
        if (!isElementSelected(driver, getDynamicLocator(locator, restParams))){
            clickToElement(driver, getDynamicLocator(locator, restParams));
        }
    }
    protected void uncheckToCheckbox(WebDriver driver, String locator) {
        if (isElementSelected(driver, locator)) {
            clickToElement(driver, locator);
        }
    }
    protected boolean isElementDisplayed(WebDriver driver, String locator){
        return getElement(driver, locator).isDisplayed();
    }
    protected boolean isElementDisplayed(WebDriver driver, String locator, String...restParams){
        return getElement(driver, getDynamicLocator(locator, restParams)).isDisplayed();
    }
    protected void setImplicitWait(WebDriver driver, long timeout){
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));
    }
    protected boolean isElementUndisplayed(WebDriver driver, String locator){
        setImplicitWait(driver, shortTimeout);
        List<WebElement> elements = getListElement(driver, locator);
        setImplicitWait(driver, longTimeout);
        if (elements.size() > 0 && elements.get(0).isDisplayed()){
            //Element co tren UI va co trong DOM
            return false;
        } else if (elements.size() > 0 && !elements.get(0).isDisplayed()){
            //Element khong co tren UI va co trong DOM
            return true;
        } else {
            return true;
        }
    }
    protected boolean isElementUndisplayed(WebDriver driver, String locator, String...restParams){
        setImplicitWait(driver, shortTimeout);
        List<WebElement> elements = getListElement(driver, getDynamicLocator(locator, restParams));
        setImplicitWait(driver, longTimeout);
        if (elements.size() > 0 && elements.get(0).isDisplayed()){
            //Element co tren UI va co trong DOM
            return false;
        } else if (elements.size() > 0 && !elements.get(0).isDisplayed()){
            //Element khong co tren UI va co trong DOM
            return true;
        } else {
            return true;
        }
    }
    protected void switchToFrame(WebDriver driver, String locator){
        driver.switchTo().frame(getElement(driver, locator));
    }
    protected void switchToDefaultContent(WebDriver driver){
        driver.switchTo().defaultContent();
    }
    protected void clickToElementByJs(WebDriver driver, String locator){
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", getElement(driver, locator));
        //sleepInSecond(3);
    }
    protected boolean isPageLoadedSuccess(WebDriver driver){
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));

        //Điều kiện 1
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return (Boolean) jsExecutor.executeScript("return (window.jQuery != null) && (jQuery.active === 0);");
            }
        };

        //Điều kiện 2
        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return jsExecutor.executeScript("return document.readyState").toString().equals("complete");
            }
        };
        return explicitWait.until(jQueryLoad) && explicitWait.until(jsLoad);
    }
    protected void waitForElementVisible(WebDriver driver, String locator){
        new WebDriverWait(driver, Duration.ofSeconds(longTimeout))
                .until(ExpectedConditions.visibilityOfElementLocated(getByLocator(locator)));
    }
    protected void waitForElementPresence(WebDriver driver, String locator){
        new WebDriverWait(driver, Duration.ofSeconds(longTimeout))
                .until(ExpectedConditions.presenceOfElementLocated(getByLocator(locator)));
    }
    protected void waitForListElementVisible(WebDriver driver, String locator){
        new WebDriverWait(driver, Duration.ofSeconds(longTimeout)).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(getByLocator(locator)));
    }
    protected void waitForListElementVisible(WebDriver driver, String locator, String...restParams){
        new WebDriverWait(driver, Duration.ofSeconds(longTimeout)).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(getByLocator(getDynamicLocator(locator, restParams))));
    }
    protected void waitForElementClickable(WebDriver driver, String locator){
        new WebDriverWait(driver, Duration.ofSeconds(longTimeout)).until(ExpectedConditions.elementToBeClickable(getByLocator(locator)));
    }
    protected void waitForElementClickable(WebDriver driver, String locator, String...restParams){
        new WebDriverWait(driver, Duration.ofSeconds(longTimeout)).until(ExpectedConditions.elementToBeClickable(getByLocator(getDynamicLocator(locator, restParams))));
    }
    public void waitForElementInvisible(WebDriver driver, String locator) {
        new WebDriverWait(driver, Duration.ofSeconds(longTimeout)).until(ExpectedConditions.invisibilityOfElementLocated(getByLocator(locator)));
    }
    protected void sleepInSecond(long timeout) {
        try {
            Thread.sleep(timeout * 1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private long longTimeout = GlobalConstants.LONG_TIMEOUT;
    private long shortTimeout = GlobalConstants.SHORT_TIMEOUT;
}

