package e2e

import io.github.bonigarcia.wdm.WebDriverManager
import io.kotest.core.spec.style.DescribeSpec
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.nio.file.WatchEvent

class Sex8Test : DescribeSpec({
    lateinit var driver: WebDriver

    beforeSpec {

        WebDriverManager.chromedriver().driverVersion("131.0.6778.205")
            .setup()  // а потому что эта шляпа только так работает
        WebDriverManager.chromedriver().clearResolutionCache()


        driver = ChromeDriver()
    }

    afterSpec {
        driver.quit()
    }

    describe("test UC-SEX8") {
        it("клиент ставит оценку психологу после консультации") {
            driver.get("http://localhost:3000")
            val wait = WebDriverWait(driver, java.time.Duration.ofSeconds(10))
            // Авторизировались
            val mailField: WebElement =
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='username']")))
            val passwdField: WebElement =
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"password\"]")))
            val sendButton: WebElement =
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"kc-login\"]")))

            mailField.sendKeys("client@example.com")
            passwdField.sendKeys("client")
            sendButton.click()

            val myApplications: WebElement =
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div/header/h4[2]")))
            myApplications.click()

            val acceptedApplications: WebElement =
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div/div[1]/div[1]/span[2]")))
            acceptedApplications.click()

            val evaluateApplication: WebElement =
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div/div[1]/div[3]/div/button")))
            evaluateApplication.click()

            val moreStars: WebElement =
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("svg[stroke='currentColor'][viewBox='0 0 320 512']")))
            moreStars.click()
            moreStars.click()

            val review: WebElement =
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"modal-root\"]/div/div/div/textarea")))
            review.sendKeys("все супер-пупер")

            val evaluateButton: WebElement =
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"modal-root\"]/div/div/div/button")))
            evaluateButton.click()

        }
    }
})