package e2e

import io.github.bonigarcia.wdm.WebDriverManager
import io.kotest.core.spec.style.DescribeSpec
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

class Sex5Test: DescribeSpec({
    lateinit var driver: WebDriver

    beforeSpec {

        WebDriverManager.chromedriver().driverVersion("131.0.6778.205").setup()  // а потому что эта шляпа только так работает
        WebDriverManager.chromedriver().clearResolutionCache()


        driver = ChromeDriver()
    }

    afterSpec {
        driver.quit()
    }

    describe("test UC-SEX5") {
        it("психолог проводит консультацию") {
            driver.get("http://localhost:3000")
            val wait = WebDriverWait(driver, java.time.Duration.ofSeconds(10))
            // Авторизировались
            val mailField: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='username']")))
            val passwdField: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"password\"]")))
            val sendButton: WebElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"kc-login\"]")))

            mailField.sendKeys("psycho@example.com")
            passwdField.sendKeys("psycho")
            sendButton.click()

            Thread.sleep(1000)

            val consultations: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"root\"]/div/header/h4[2]")))
            consultations.click()
            Thread.sleep(1000)

            val slot: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"root\"]/div/div[1]/div/div[2]/div[2]/div[37]/div[2]")))
            slot.click()
            val openSlot: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("svg[stroke='currentColor'][viewBox='0 0 448 512']")))
            openSlot.click()

            val openConsultation: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"modal-root\"]/div/div/div/div/div[3]/button")))
            openConsultation.click()

            val note: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"modal-root\"]/div[2]/div/div/textarea")))
            note.sendKeys("клиенту необходимо принять тест репорт по мпи")
            Thread.sleep(1000)
            val endConsultation = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"modal-root\"]/div[2]/div/div/div[3]/button[2]")))
            Thread.sleep(1000)
            endConsultation.click()
            Thread.sleep(10000)
        }
    }
})
