import java.util.ArrayList;
import java.util.Scanner;

public class ATM {
    private static final int MAX_ATTEMPTS = 3;
    private final Bank bank;
    private final Scanner scanner = new Scanner(System.in);

    public ATM(Bank bank) {
        this.bank = bank;
    }

    public void start() {
        System.out.println("=====================================");
        System.out.println("   Welcome to " + bank.getName() + " ATM");
        System.out.println("=====================================");

        Account account = login();
        if (account == null) {
            System.out.println("\nToo many incorrect attempts. Access denied.");
            return;
        }
        System.out.println("\nLogin successful. Hello, " + account.getHolderName() + "!");
        menuLoop(account);
    }

    private Account login() {
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            System.out.print("\nEnter User ID: ");
            String id = scanner.nextLine().trim();
            System.out.print("Enter PIN: ");
            String pin = scanner.nextLine().trim();

            Account acc = bank.authenticate(id, pin);
            if (acc != null) return acc;

            int left = MAX_ATTEMPTS - attempt;
            if (left > 0) System.out.println("Invalid User ID or PIN. Attempts left: " + left);
        }
        return null;
    }

    private void menuLoop(Account account) {
        boolean running = true;
        while (running) {
            System.out.println("\n------------- MAIN MENU -------------");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.printf("Current balance: %.2f%n", account.getBalance());
            System.out.print("Choose an option (1-5): ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": showHistory(account); break;
                case "2": withdraw(account); break;
                case "3": deposit(account); break;
                case "4": transfer(account); break;
                case "5":
                    System.out.println("\nThank you for banking with us. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please enter a number from 1 to 5.");
            }
        }
    }

    private void showHistory(Account account) {
        ArrayList<Transaction> list = account.getHistory();
        System.out.println("\n=== TRANSACTION HISTORY (this session) ===");
        if (list.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }
        System.out.printf("%-3s | %-19s | %-17s | %10s | %10s | %s%n",
                "#", "Date/Time", "Type", "Amount", "Balance", "Details");
        System.out.println("-".repeat(90));
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%-3d | %s%n", i + 1, list.get(i));
        }
    }

    private void withdraw(Account account) {
        double amount = readAmount("Enter amount to withdraw: ");
        if (amount <= 0) return;
        if (account.withdraw(amount)) {
            System.out.printf("Please collect your cash. New balance: %.2f%n", account.getBalance());
        } else {
            System.out.println("Insufficient Funds");
        }
    }

    private void deposit(Account account) {
        double amount = readAmount("Enter amount to deposit: ");
        if (amount <= 0) return;
        account.deposit(amount);
        System.out.printf("Deposit successful. New balance: %.2f%n", account.getBalance());
    }

    private void transfer(Account account) {
        System.out.print("Enter recipient account ID: ");
        String toId = scanner.nextLine().trim();
        double amount = readAmount("Enter amount to transfer: ");
        if (amount <= 0) return;

        switch (bank.transfer(account, toId, amount)) {
            case SUCCESS:
                System.out.printf("Transferred %.2f to %s. New balance: %.2f%n",
                        amount, toId, account.getBalance());
                break;
            case INSUFFICIENT_FUNDS:
                System.out.println("Insufficient Funds");
                break;
            case RECIPIENT_NOT_FOUND:
                System.out.println("Recipient account not found.");
                break;
            case SAME_ACCOUNT:
                System.out.println("You cannot transfer to your own account.");
                break;
        }
    }

    /** Reads a positive number; returns -1 if input is invalid. */
    private double readAmount(String prompt) {
        System.out.print(prompt);
        try {
            double amount = Double.parseDouble(scanner.nextLine().trim());
            if (amount <= 0) {
                System.out.println("Amount must be greater than zero.");
                return -1;
            }
            return amount;
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a number.");
            return -1;
        }
    }
}
