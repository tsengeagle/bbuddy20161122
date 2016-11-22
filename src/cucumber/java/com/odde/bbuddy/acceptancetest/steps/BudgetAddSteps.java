package com.odde.bbuddy.acceptancetest.steps;

import com.odde.bbuddy.acceptancetest.driver.UiDriver;
import cucumber.api.PendingException;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;

/**
 * Created by user on 2016/11/22.
 */
public class BudgetAddSteps {
    @When("^add (\\d+) at \"([^\"]*)\"$")
    public void add_at(int arg1, String arg2) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^DB has one record with \"([^\"]*)\" (\\d+)$")
    public void db_has_one_record_with(String arg1, int arg2) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
