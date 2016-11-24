package com.odde.bbuddy.budget.domain;

import com.odde.bbuddy.budget.repo.BudgetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zbcjackson
 * @since 22/11/2016
 */
@Service
public class BudgetService {
    private final BudgetRepo repository;

    @Autowired
    public BudgetService(BudgetRepo repository) {
        this.repository = repository;
    }

    public void add(Budget budget,
                    Runnable failure) {
        if (!validateBudget(budget)) {
            failure.run();
            return;
        }

        Budget savedBudget = repository.findByMonth(budget.getMonth());
        if (savedBudget != null) {
            savedBudget.setAmount(budget.getAmount());
            repository.save(savedBudget);
        }else {
            repository.save(budget);
        }
    }

    private boolean hasPreviousOrCurrentOrNextBudget(Budget budget) {
        return getBudgetByMonthOffset(budget, 0) != null || getPreviousBudget(budget) != null || getNextBudget(budget) != null;
    }

    private Budget getNextBudget(Budget budget) {
        return getBudgetByMonthOffset(budget, 1);
    }

    private Budget getPreviousBudget(Budget budget) {
        return getBudgetByMonthOffset(budget, -1);
    }


    private Budget getBudgetByMonthOffset(Budget budget, int offset){
        String[] intentMonth = budget.getMonth().split("-");
        int monthNumber = Integer.parseInt(intentMonth[1]) + offset;
        return repository.findByMonth(String.format("%s-%02d", intentMonth[0] , monthNumber));
    }


    public List<Budget> list() {

        return repository.findAll()
                         .stream()
                         .sorted((b1, b2) -> b1.getMonth()
                                               .compareTo(b2.getMonth()))
                         .collect(Collectors.toList());
    }

    public boolean validateBudget(Budget budget) {
        return repository.count() == 0 || hasPreviousOrCurrentOrNextBudget(budget);
    }

    public double totalBudget(String startDate,
                              String endDate) {

        String startMonth=startDate.substring(0,7);
        String endMonth=endDate.substring(0,7);
        List<Budget> budgets = repository.findBetween(startMonth, endMonth);

        final LocalDate toDate = LocalDate.parse(endDate);
        final LocalDate fromDate = LocalDate.parse(startDate);
        return budgets.stream()
                      .mapToDouble(budget -> monthlyBudget(budget, fromDate, toDate))
                      .sum();

    }

    private double monthlyBudget(Budget budget,
                                 LocalDate fromDate,
                                 LocalDate toDate) {

        LocalDate budgetMonth = budget.thisMonth();

        int budgetLeftDays = getDuration(fromDate, toDate, budgetMonth);

        int days = budgetMonth.getMonth().length(true);
        return new BigDecimal(budget.getAmount()).divide(new BigDecimal(days), BigDecimal.ROUND_HALF_UP)
                                                 .multiply(new BigDecimal(budgetLeftDays))
                                                 .doubleValue();

    }

    private int getDuration(LocalDate fromDate, LocalDate toDate, LocalDate budgetMonth) {
        int duration;
        if (fromDate.getMonth()
                    .equals(toDate.getMonth())) {
            //計算同月的日期間隔
            duration = toDate.getDayOfMonth() - fromDate.getDayOfMonth() + 1;
        }
        else if (budgetMonth.getMonth()
                            .equals(fromDate.getMonth())) {
            //計算在該預算月份還有幾天
            duration = budgetMonth.getMonth().length(true) - fromDate.getDayOfMonth() + 1;
        }
        else if (budgetMonth.getMonth()
                            .equals(toDate.getMonth())) {

            //在最後一個月份直接看結束日期的"日"
            duration = toDate.getDayOfMonth();
        }
        else {
            duration = budgetMonth.getMonth().length(true);
        }
        return duration;
    }
}
