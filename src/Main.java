public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank("Java National Bank");

        // Demo accounts (User ID / PIN)
        bank.addAccount(new Account("user1", "Alice", "1234", 5000.00));
        bank.addAccount(new Account("user2", "Bob", "4321", 2500.00));
        bank.addAccount(new Account("user3", "Carol", "1111", 10000.00));

        ATM atm = new ATM(bank);
        atm.start();
    }
}
