package core.hubby.backend.accounts.repositories.projections;

public interface AccountLookup {
	String getCode();
	String getAccountCode();
	AccountType getAccountType();
	String getTaxType();
	
	interface AccountType {
		String getName();
	}
}
