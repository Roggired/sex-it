package e2e

import io.github.bonigarcia.wdm.WebDriverManager
import io.kotest.core.spec.style.DescribeSpec
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

class Sex12Test : DescribeSpec({
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

    describe("test UC-SEX12") {
        it("друган генерирует реферальную ссылку") {

            driver.get("http://localhost:3000")
            val wait = WebDriverWait(driver, java.time.Duration.ofSeconds(10))
            // Авторизировались
            var mailField: WebElement =
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='username']")))
            var passwdField: WebElement =
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"password\"]")))
            var sendButton: WebElement =
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"kc-login\"]")))

            mailField.sendKeys("psycho_friend@example.com")
            passwdField.sendKeys("psycho_friend")
            sendButton.click()

            var friends: WebElement =
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div/header/h4[2]")))
            friends.click()

            val addFriend: WebElement =
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div/div[1]/div[3]/div/button")))
            addFriend.click()

            var logout: WebElement =
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div/header/h4[4]")))
            logout.click()
            Thread.sleep(1000)
            var logoutKeyCloack =
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"kc-logout\"]")))
            logoutKeyCloack.click()

            mailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='username']")))
            passwdField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"password\"]")))
            sendButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"kc-login\"]")))

            mailField.sendKeys("psycho@example.com")
            passwdField.sendKeys("psycho")
            sendButton.click()

            friends =
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div/header/h4[4]")))
            friends.click()

            val acceptButton =
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div/div[1]/div[2]/div/div[2]/button[1]")))
            acceptButton.click()

            logout = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div/header/h4[5]")))
            logout.click()
            Thread.sleep(1000)
            logoutKeyCloack =
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"kc-logout\"]")))
            logoutKeyCloack.click()

            mailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='username']")))
            passwdField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"password\"]")))
            sendButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"kc-login\"]")))

            mailField.sendKeys("psycho_friend@example.com")
            passwdField.sendKeys("psycho_friend")
            sendButton.click()

            friends =
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div/header/h4[2]")))
            friends.click()

            val acceptedFriends =
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div/div[1]/div[1]/span[2]")))
            acceptedFriends.click()

            val getRefer =
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div/div[1]/div[3]/div/button")))
            getRefer.click()
            Thread.sleep(1000)
        }
    }
})