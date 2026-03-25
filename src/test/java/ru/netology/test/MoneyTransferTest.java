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
        var verificationPage = loginPage.validLogin(authInfo.getLogin(), authInfo.getPassword());

        var verificationCode = DataHelper.getVerificationCode();
        dashboardPage = verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    @DisplayName("Should transfer money from second card to first card")
    void shouldTransferMoneyFromSecondCardToFirstCard() {
        var firstCard = DataHelper.getFirstCard();
        var secondCard = DataHelper.getSecondCard();

        int initialBalanceFirstCard = dashboardPage.getCardBalance(firstCard.getCardNumber());
        int initialBalanceSecondCard = dashboardPage.getCardBalance(secondCard.getCardNumber());

        int transferAmount = DataHelper.getValidAmount(initialBalanceSecondCard);

        var transferPage = dashboardPage.selectCardToTopUp(firstCard.getCardNumber());
        dashboardPage = transferPage.validTransfer(transferAmount, secondCard.getCardNumber());

        int actualBalanceFirstCard = dashboardPage.getCardBalance(firstCard.getCardNumber());
        int actualBalanceSecondCard = dashboardPage.getCardBalance(secondCard.getCardNumber());

        assertEquals(initialBalanceFirstCard + transferAmount, actualBalanceFirstCard);
        assertEquals(initialBalanceSecondCard - transferAmount, actualBalanceSecondCard);
    }

    @Test
    @DisplayName("Should transfer money from first card to second card")
    void shouldTransferMoneyFromFirstCardToSecondCard() {
        var firstCard = DataHelper.getFirstCard();
        var secondCard = DataHelper.getSecondCard();

        int initialBalanceFirstCard = dashboardPage.getCardBalance(firstCard.getCardNumber());
        int initialBalanceSecondCard = dashboardPage.getCardBalance(secondCard.getCardNumber());

        int transferAmount = DataHelper.getValidAmount(initialBalanceFirstCard);

        var transferPage = dashboardPage.selectCardToTopUp(secondCard.getCardNumber());
        dashboardPage = transferPage.validTransfer(transferAmount, firstCard.getCardNumber());

        int actualBalanceFirstCard = dashboardPage.getCardBalance(firstCard.getCardNumber());
        int actualBalanceSecondCard = dashboardPage.getCardBalance(secondCard.getCardNumber());

        assertEquals(initialBalanceFirstCard - transferAmount, actualBalanceFirstCard);
        assertEquals(initialBalanceSecondCard + transferAmount, actualBalanceSecondCard);
    }
}