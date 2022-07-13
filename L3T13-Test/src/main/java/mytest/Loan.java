


public class Loan {
    private double yearlyInterestRate;
    private int years;
    private double amount;
    private java.util.Date loanDate;
    //Default constructor
    public Loan() {
        this(8, 1, 1000);
    }
    public Loan(double interest, int numberOfYears,
                double loanAmount) {
        yearlyInterestRate = interest;
        years = numberOfYears;
        amount = loanAmount;
        loanDate = new java.util.Date();
    }
    public double getYearlyInterestRate() {
        return yearlyInterestRate;
    }
    public void setYearlyInterestRate(double interest) {
        yearlyInterestRate = interest;
    }
    public int getYears() {
        return years;
    }
    public void setYears(int numberOfYears) {
        years = numberOfYears;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double loanAmount) {
        amount = loanAmount;
    }
    //Calculate monthly payment
    public double getMonthlyPayment() {
        double monthlyInterestRate = yearlyInterestRate / 1200;
        double monthlyPayment = amount * monthlyInterestRate / (1 -
                (1 / Math.pow(1 + monthlyInterestRate, years * 12)));
        return monthlyPayment;
    }
    // Calculate total payment
    public double getTotalPayment() {
        double totalPayment = getMonthlyPayment() * years * 12;
        return totalPayment;
    }
}