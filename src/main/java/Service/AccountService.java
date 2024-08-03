package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        this.accountDAO = new AccountDAO();
    }
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account addAccount(Account account){ //postRegisterAccount
        String username = account.getUsername();
        String password = account.getPassword();
        Account extantAccount = accountDAO.getAccountByUsername(username);

        // Username must not already exist, cant have a blank username, and must have a pasword longer than 4 characters.
        if (extantAccount == null && username != "" && password.length() >= 4){ 
            Account registeredAccount = accountDAO.insertAccount(account);
            return registeredAccount;
        }
        else{
            return null;
        }
    }

    public Account validateLogin(Account account){ //getLoginAccount
        return accountDAO.getAccount(account);
    }
}
