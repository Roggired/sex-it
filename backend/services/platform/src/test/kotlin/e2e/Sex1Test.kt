package e2e

import io.kotest.core.spec.style.DescribeSpec
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import io.github.bonigarcia.wdm.WebDriverManager
import io.kotest.matchers.shouldBe
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor


class Sex1Test : DescribeSpec({
    lateinit var driver: WebDriver

    beforeSpec {

        WebDriverManager.chromedriver().driverVersion("131.0.6778.205").setup()  // а потому что эта шляпа только так работает
        WebDriverManager.chromedriver().clearResolutionCache()


        driver = ChromeDriver()
    }

    afterSpec {
        driver.quit()
    }

    describe("test UC-SEX1") {
        it("создание профиля психолога и заполнение слота") {
            driver.get("http://localhost:3000")
            driver.manage().window().fullscreen()
            val wait = WebDriverWait(driver, java.time.Duration.ofSeconds(10))
            // Авторизировались
            val mailField: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='username']")))
            val passwdField: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"password\"]")))
            val sendButton: WebElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"kc-login\"]")))

            mailField.sendKeys("psycho@example.com")
            passwdField.sendKeys("psycho")
            sendButton.click()

            // заполняем профиль
            val profileButton: WebElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div/header/h4[1]")))
            profileButton.click()
            var nameField: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\":r0:\"]")))
            val aboutMeField: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"root\"]/div/div[1]/div/div[2]/textarea")))
            val saveButton: WebElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div/div[1]/div/div[3]/button[1]")))

            nameField.clear()
            nameField.sendKeys("Машусик Карасёва")
            aboutMeField.clear()
            aboutMeField.sendKeys("Примите тест план...")
            saveButton.click()
            Thread.sleep(1000)

            profileButton.click()
            Thread.sleep(1000)

            nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\":r3:\"]")))

            val enteredText = nameField.getAttribute("value")
            enteredText shouldBe "Машусик Карасёва"

            // заполняем консультации
            val consultationButton: WebElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div/header/h4[2]")))
            consultationButton.click()

            val jsExecutor = driver as JavascriptExecutor
            jsExecutor.executeScript("window.scrollBy(0, 1000);")

            Thread.sleep(1000)
            val plusButton = driver.findElement(By.cssSelector("svg.calendar-page__fab[stroke='currentColor']"))


            plusButton.click()
            val dateInput: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\":r6:\"]")))
            val timeInput: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\":r7:\"]")))

            dateInput.clear()
            Thread.sleep(500)
            dateInput.sendKeys("31-12-2024")
            timeInput.clear()
            timeInput.sendKeys("10:00")


            Thread.sleep(100)
            val createButton: WebElement = wait.until (ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"modal-root\"]/div/div/div/div[3]/button")))
            createButton.click()
        }
    }
})
