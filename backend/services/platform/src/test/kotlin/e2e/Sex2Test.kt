package e2e

import io.kotest.core.spec.style.DescribeSpec
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.By


class Sex2Test : DescribeSpec({
    lateinit var driver: WebDriver

    beforeSpec {

        WebDriverManager.chromedriver().driverVersion("131.0.6778.205").setup()  // а потому что эта шляпа только так работает
        WebDriverManager.chromedriver().clearResolutionCache()


        driver = ChromeDriver()
    }

    afterSpec {
        driver.quit()
    }

    describe("test UC-SEX2") {
        it("психолог подтверждает консультацию") {
            driver.get("http://localhost:3000")
            val wait = WebDriverWait(driver, java.time.Duration.ofSeconds(10))
            // Авторизировались
            val mailField: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='username']")))
            val passwdField: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"password\"]")))
            val sendButton: WebElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"kc-login\"]")))

            mailField.sendKeys("client@example.com")
            passwdField.sendKeys("client")
            sendButton.click()

            Thread.sleep(1000)
            val psychoCard: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"root\"]/div/div[1]/div/div[2]/div[1]/div")))
            psychoCard.click()
            val openCard: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"root\"]/div/div[1]/div/div[1]/div[2]/button[1]")))
            openCard.click()
            Thread.sleep(1000)
            val emptySlot: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"root\"]/div/div[1]/div/div[2]/div[37]/div[2]")))
            emptySlot.click()
            Thread.sleep(1000)
            val checkbox: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='modal-root']/div/div/div/div[2]/input")))
            if (checkbox.isSelected) {
                checkbox.click()
            }
            val discr: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"modal-root\"]/div/div/div/textarea")))
            discr.sendKeys("Примите, пожалуйста...")
            val sendApplication: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"modal-root\"]/div/div/div/div[3]/button[1]")))
            sendApplication.click()
            Thread.sleep(1000)
            val logout: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"root\"]/div/header/h4[3]")))
            logout.click()
            Thread.sleep(1000)
            val logoutKeyCloack = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"kc-logout\"]")))
            logoutKeyCloack.click()
            val mailField2: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='username']")))
            val passwdField2: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"password\"]")))
            val sendButton2: WebElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"kc-login\"]")))


            mailField2.sendKeys("psycho@example.com")
            passwdField2.sendKeys("psycho")
            sendButton2.click()
            Thread.sleep(1000)
            val subscriptions: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"root\"]/div/header/h4[3]")))
            subscriptions.click()
            val baseSub: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"root\"]/div/div[1]/div/div[2]/div/div/div[1]/div[1]")))
            baseSub.click()
            val makeSub: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"root\"]/div/div[1]/div/div[2]/div/button")))
            makeSub.click()
            val okButton: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"modal-root\"]/div/div/div/button")))
            okButton.click()
            val consultations: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"root\"]/div/header/h4[2]")))
            consultations.click()
            val slotToBeChosen: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"root\"]/div/div[1]/div/div[2]/div[2]/div[37]/div[2]")))
            slotToBeChosen.click()
            val seeApplications: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"modal-root\"]/div/div/div/div/div/div/span")))
            seeApplications.click()
            val application: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"root\"]/div/div[1]/div[2]")))
            application.click()
            val addressInput: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"modal-root\"]/div/div/div/div[1]/input")))
            addressInput.sendKeys("университет итмо")
            val accept: WebElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"modal-root\"]/div/div/div/div[2]/button[1]")))
            accept.click()
            Thread.sleep(1000)
            driver.quit()
        }
    }
})
