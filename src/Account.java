import java.util.ArrayList;

public class Account {
    private final String userId;
    private final String holderName;
    private final String pin;
    private double balance;
    private final ArrayList<Transaction> history = new ArrayList<>();

    public Account(String userId, String holderName, String pin, double openingBalance) {
        this.userId = userId;
        this.holderName = holderName;
        this.pin = pin;
        this.balance = openingBalance;
    }

    public String getUserId() { return userId; }
    public String getHolderName() { return holderName; }
    public double getBalance() { return balance; }
    public ArrayList<Transaction> getHistory() { return history; }

    public boolean checkPin(String enteredPin) {
        return pin.equals(enteredPin);
    }

    public boolean hasSufficientFunds(double amount) {
        return balance >= amount;
    }

    public void deposit(double amount) {
        balance += amount;
        history.add(new Transaction(TransactionType.DEPOSIT, amount, balance, "Cash deposit"));
    }

    public boolean withdraw(double amount) {
        if (!hasSufficientFunds(amount)) return false;
        balance -= amount;
        history.add(new Transaction(TransactionType.WITHDRAW, amount, balance, "Cash withdrawal"));
        return true;
    }

    // Used by Bank.transfer(); logs on each side of the transfer.
    void debitForTransfer(double amount, String toId) {
        balance -= amount;
        history.add(new Transaction(TransactionType.TRANSFER_OUT, amount, balance, "To account " + toId));
    }

    void creditFromTransfer(double amount, String fromId) {
        balance += amount;
        history.add(new Transaction(TransactionType.TRANSFER_IN, amount, balance, "From account " + fromId));
    }
}
