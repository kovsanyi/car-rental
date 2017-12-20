package hu.unideb.inf.carrental.statistics.resource.model;

public class IncomeByBrandResponse {
    String brand;
    Integer income;

    public IncomeByBrandResponse(String brand, Integer income) {
        this.brand = brand;
        this.income = income;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getIncome() {
        return income;
    }

    public void setIncome(Integer income) {
        this.income = income;
    }
}
