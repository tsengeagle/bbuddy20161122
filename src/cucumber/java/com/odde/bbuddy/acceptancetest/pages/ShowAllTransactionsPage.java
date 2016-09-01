package com.odde.bbuddy.acceptancetest.pages;

import com.odde.bbuddy.acceptancetest.driver.UiDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("cucumber-glue")
public class ShowAllTransactionsPage {

    @Autowired
    UiDriver uiDriver;

    public void show() {
        uiDriver.navigateTo("/transaction/list");
    }

}
