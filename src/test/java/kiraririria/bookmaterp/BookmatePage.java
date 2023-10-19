package kiraririria.bookmaterp;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

// page_url = https://bookmate.ru/@b3667744535/books/all
public class BookmatePage {
    public SelenideElement booksList = $x("//div[@class='user-sub-header-books__inner']");

}
