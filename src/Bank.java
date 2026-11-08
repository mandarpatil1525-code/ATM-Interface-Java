import java.util.HashMap;
import java.util.Map;

public class Bank {
    private final String name;
    private final Map<String, Account> accounts = new HashMap<>();

    public Bank(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public void addAccount(Account account) {
        accounts.put(account.getUserId(), account);
    }

    public Account findAccount(String userId) {
        return accounts.get(userId);
    }

    /** Returns the account if the ID and PIN match, otherwise null. */
    public Account authenticate(String userId, String pin) {
        Account acc = accounts.get(userId);
        return (acc != null && acc.checkPin(pin)) ? acc : null;
    }

    public enum TransferResult { SUCCESS, INSUFFICIENT_FUNDS, RECIPIENT_NOT_FOUND, SAME_ACCOUNT }

    public TransferResult transfer(Account from, String toId, double amount) {
        if (from.getUserId().equals(toId)) return TransferResult.SAME_ACCOUNT;
        Account to = accounts.get(toId);
        if (to == null) return TransferResult.RECIPIENT_NOT_FOUND;
        if (!from.hasSufficientFunds(amount)) return TransferResult.INSUFFICIENT_FUNDS;
        from.debitForTransfer(amount, toId);
        to.creditFromTransfer(amount, from.getUserId());
        return TransferResult.SUCCESS;
    }
}
