package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    private DashboardPage dashboardPage;

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");

        var authInfo = DataHelper.getAuthInfo();
        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(authInfo);

        var verificationCode = DataHelper.getVerificationCode();
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    @DisplayName("Should transfer money from second card to first card")
    void shouldTransferMoneyFromSecondCardToFirstCard() {
        var firstCard = DataHelper.getFirstCard();
        var secondCard = DataHelper.getSecondCard();

        int initialBalanceFirstCard = dashboardPage.getCardBalance(firstCard);
        int initialBalanceSecondCard = dashboardPage.getCardBalance(secondCard);

        int transferAmount = DataHelper.getValidAmount(initialBalanceSecondCard);

        var transferPage = dashboardPage.selectCardToTopUp(firstCard);
        dashboardPage = transferPage.validTransfer(transferAmount, secondCard);

        int actualBalanceFirstCard = dashboardPage.getCardBalance(firstCard);
        int actualBalanceSecondCard = dashboardPage.getCardBalance(secondCard);

        assertEquals(initialBalanceFirstCard + transferAmount, actualBalanceFirstCard);
        assertEquals(initialBalanceSecondCard - transferAmount, actualBalanceSecondCard);
    }

    @Test
    @DisplayName("Should transfer money from first card to second card")
    void shouldTransferMoneyFromFirstCardToSecondCard() {
        var firstCard = DataHelper.getFirstCard();
        var secondCard = DataHelper.getSecondCard();

        int initialBalanceFirstCard = dashboardPage.getCardBalance(firstCard);
        int initialBalanceSecondCard = dashboardPage.getCardBalance(secondCard);

        int transferAmount = DataHelper.getValidAmount(initialBalanceFirstCard);

        var transferPage = dashboardPage.selectCardToTopUp(secondCard);
        dashboardPage = transferPage.validTransfer(transferAmount, firstCard);

        int actualBalanceFirstCard = dashboardPage.getCardBalance(firstCard);
        int actualBalanceSecondCard = dashboardPage.getCardBalance(secondCard);

        assertEquals(initialBalanceFirstCard - transferAmount, actualBalanceFirstCard);
        assertEquals(initialBalanceSecondCard + transferAmount, actualBalanceSecondCard);
    }
}