package core.hubby.backend.accounts;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import core.hubby.backend.accounts.entities.AccountCategory;
import core.hubby.backend.accounts.entities.AccountType;
import core.hubby.backend.accounts.repositories.AccountCategoryRepository;
import core.hubby.backend.accounts.repositories.AccountTypeRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AccountsConfig implements CommandLineRunner {
	private final AccountTypeRepository accountTypeRepository;
	private final AccountCategoryRepository accountCategoryRepository;

	@Override
	public void run(String... args) throws Exception {
		// Create default categories
		AccountCategory asset = accountCategoryRepository.save(new AccountCategory("ASSET"));
		AccountCategory liability = accountCategoryRepository.save(new AccountCategory("LIABILITY"));
		AccountCategory equity = accountCategoryRepository.save(new AccountCategory("EXPENSE"));
		AccountCategory revenue = accountCategoryRepository.save(new AccountCategory("REVENUE"));
		AccountCategory expense = accountCategoryRepository.save(new AccountCategory("EXPENSE"));
		
		// Create default account types
		accountTypeRepository.saveAll(Set.of(
				new AccountType("CURRENT", "Current asset account", asset),
				new AccountType("INVENTORY", "Inventory asset account", asset),
				new AccountType("CURRLIAB", "Current liability account", liability),
				new AccountType("EQUITY", "Equity account", equity),
				new AccountType("EXPENSE", "Expense account", expense),
				new AccountType("FIXED", "Fixed asset account", asset),
				new AccountType("LIABILITY", "Liability account", liability),
				new AccountType("NONCURRENT", "Non current asset account", asset),
				new AccountType("REVENUE", "Revenue account", revenue),
				new AccountType("SALES", "Sales account", revenue),
				new AccountType("TERMLIAB", "Non current liability account", liability),
				new AccountType("DIRECTCOSTS", "Direct costs account", expense)
		));
	
	}

}
