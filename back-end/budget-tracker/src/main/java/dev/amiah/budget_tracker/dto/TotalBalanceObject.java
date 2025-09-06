package dev.amiah.budget_tracker.dto;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class TotalBalanceObject {

    private BigDecimal total;

    public TotalBalanceObject() {}

    public TotalBalanceObject(BigDecimal total) {
        this.total = total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }


    public BigDecimal getTotal() {
        // Return 0 instead of null
        if (total != null) {
            return total.setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }
}
