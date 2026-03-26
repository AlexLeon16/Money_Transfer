package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private final ElementsCollection cards = $$(".list__item");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        String maskedNumber = DataHelper.getMaskedCardNumber(cardInfo.getCardNumber());
        String cardText = cards.findBy(text(maskedNumber)).text();
        return extractBalance(cardText);
    }

    public TransferPage selectCardToTopUp(DataHelper.CardInfo cardInfo) {
        String maskedNumber = DataHelper.getMaskedCardNumber(cardInfo.getCardNumber());
        cards.findBy(text(maskedNumber)).$("button").click();
        return new TransferPage();
    }

    private int extractBalance(String text) {
        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceFinish);
        String value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value.replaceAll("\\s", ""));
    }
}