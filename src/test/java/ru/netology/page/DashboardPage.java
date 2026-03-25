package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {
    private ElementsCollection cards = $$(".list__item");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    private SelenideElement refreshButton = $("[data-test-id='action-refresh']");

    public int getCardBalance(int index) {
        var text = cards.get(index).text();
        return extractBalance(text);
    }

    public int getCardBalance(String cardNumber) {
        var text = cards.findBy(text(cardNumber)).text();
        return extractBalance(text);
    }

    public TransferPage selectCardToTopUp(String cardNumber) {
        cards.findBy(text(cardNumber)).$("button").click();
        return new TransferPage();
    }

    public void refreshDashboard() {
        refreshButton.click();
    }

    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value.replaceAll("\\s", "").replace(",", ""));
    }
}