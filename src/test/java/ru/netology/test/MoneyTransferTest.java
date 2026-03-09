package ru.netology.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MoneyTransferTest {
    private static final String LOGIN = "vasya";
    private static final String PASSWORD = "qwerty123";
    private static final String VERIFICATION_CODE = "12345";
    private static final String CARD_1 = "5559 0000 0000 0001";
    private static final String CARD_2 = "5559 0000 0000 0002";
    private static final int INITIAL_BALANCE = 10000;

    private DashboardPage dashboardPage;

    @BeforeEach
    void setUp() {
        Configuration.headless = true;
        Configuration.browser = "chrome";
        // Укажите правильный порт, на котором запускается приложение
        // Обычно это 8080 или 9999
        com.codeborne.selenide.Selenide.open("http://localhost:8080");

        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(LOGIN, PASSWORD);
        dashboardPage = verificationPage.validVerify(VERIFICATION_CODE);
    }

    @Test
    @Order(1)
    @DisplayName("Should transfer money from card 2 to card 1")
    void shouldTransferFromCard2ToCard1() {
        int initialBalanceCard1 = dashboardPage.getCardBalance(0);
        int initialBalanceCard2 = dashboardPage.getCardBalance(1);

        int transferAmount = 1000;

        var transferPage = dashboardPage.selectCardToTopUp(0);
        dashboardPage = transferPage.validTransfer(transferAmount, CARD_2);

        int newBalanceCard1 = dashboardPage.getCardBalance(0);
        int newBalanceCard2 = dashboardPage.getCardBalance(1);

        assertEquals(initialBalanceCard1 + transferAmount, newBalanceCard1);
        assertEquals(initialBalanceCard2 - transferAmount, newBalanceCard2);
    }

    @Test
    @Order(2)
    @DisplayName("Should transfer money from card 1 to card 2")
    void shouldTransferFromCard1ToCard2() {
        int initialBalanceCard1 = dashboardPage.getCardBalance(0);
        int initialBalanceCard2 = dashboardPage.getCardBalance(1);

        int transferAmount = 500;

        var transferPage = dashboardPage.selectCardToTopUp(1);
        dashboardPage = transferPage.validTransfer(transferAmount, CARD_1);

        int newBalanceCard1 = dashboardPage.getCardBalance(0);
        int newBalanceCard2 = dashboardPage.getCardBalance(1);

        assertEquals(initialBalanceCard1 - transferAmount, newBalanceCard1);
        assertEquals(initialBalanceCard2 + transferAmount, newBalanceCard2);
    }
}