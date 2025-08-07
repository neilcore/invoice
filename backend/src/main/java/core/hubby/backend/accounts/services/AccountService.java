package core.hubby.backend.accounts.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.stereotype.Service;

import core.hubby.backend.accounts.controller.dto.AccountCodeExistsResponse;
import core.hubby.backend.accounts.controller.dto.AccountResponse;
import core.hubby.backend.accounts.entities.AccountType;
import core.hubby.backend.accounts.entities.Accounts;
import core.hubby.backend.accounts.mapper.AccountMapper;
import core.hubby.backend.accounts.repositories.AccountRepository;
import core.hubby.backend.accounts.repositories.AccountTypeRepository;
import core.hubby.backend.business.entities.Organization;
import core.hubby.backend.business.repositories.OrganizationRepository;
import core.hubby.backend.core.api.CustomRestExceptionHandler;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final SecurityEvaluationContextExtension securityEvaluationContextExtension;

    private final CustomRestExceptionHandler customRestExceptionHandler;
	private final AccountRepository accountRepository;
	private final AccountTypeRepository accountTypeRepository;
	private final OrganizationRepository organizationRepository;
	private final AccountMapper accountMapper;
	
	public AccountResponse createAccount() {
		return null;
	}
	
	/**
	 * This will check if account code already exists or not
	 * @param code - accepts {@linkplain java.util.String} object type.
	 * @return - {@linkplain AccountCodeExistsResponse} object type.
	 */
	public AccountCodeExistsResponse checkIfCodeExists(String code) {
		boolean checkIfExists = accountRepository.existsByCodeIgnoreCase(code);
		
		return new AccountCodeExistsResponse(code, checkIfExists);
	}
	
	// TODO - work on getAccountByClassType method
	public void getAccountByClassType(@NotNull String classType) {
		Set<Accounts> classTypeAccounts = accountRepository.findByClassTypeIgnoreCase(classType);
	}
	
	public void createDefaultAccounts(@NotNull UUID organizationId) {
		Organization getOrganization = organizationRepository.findById(organizationId).get();
		Optional<AccountType> currentAsset = accountTypeRepository.findByNameIgnoreCase("CURRENT");
		Optional<AccountType> inventoryAsset = accountTypeRepository.findByNameIgnoreCase("INVENTORY");
		Optional<AccountType> fixedAsset = accountTypeRepository.findByNameIgnoreCase("FIXED");
		
		// Create asset accounts
		Accounts accountReceivable = new Accounts(
				getOrganization,
				"100",
				"Receivable Account",
				currentAsset.get()
		);
		accountReceivable.setDescription("Account that tracks money owed to you by your customers.");
		accountReceivable.setClassType(currentAsset.get().getCategory().getName());
		
		Accounts inventoryAssetAccount = new Accounts(
				getOrganization,
				"101",
				"Inventory",
				inventoryAsset.get()
		);
		inventoryAssetAccount.setDescription("Account for businesses that hold and track stock.");
		inventoryAssetAccount.setClassType(inventoryAsset.get().getCategory().getName());
		
		Accounts fixedAssetAccount = new Accounts(
				getOrganization,
				"102",
				"Fixed Asset",
				fixedAsset.get()
		);
		fixedAssetAccount.setDescription("Accounts for long-term assets.");
		fixedAssetAccount.setClassType(fixedAsset.get().getCategory().getName());
		
		accountRepository.saveAll(List.of(accountReceivable, inventoryAssetAccount, fixedAssetAccount));
		
		// Create liability accounts
		Optional<AccountType> liabilityAcc = accountTypeRepository.findByNameIgnoreCase("LIABILITY");
		Accounts accountPayable = new Accounts(
				getOrganization,
				"300",
				"Payable Account",
				liabilityAcc.get()
		);
		accountPayable.setDescription("Tracks money you owe to your suppliers.");
		accountPayable.setClassType(liabilityAcc.get().getCategory().getName());
		
		Accounts taxAcc = new Accounts(
				getOrganization,
				"301",
				"Tax",
				liabilityAcc.get()
		);
		taxAcc.setDescription("Accounts for taxes payable, such as Sales Tax (or VAT/GST), and other statutory liabilities.");
		taxAcc.setClassType(liabilityAcc.get().getCategory().getName());
		
		Accounts loans = new Accounts(
				getOrganization,
				"302",
				"Loans",
				liabilityAcc.get()
		);
		loans.setDescription("Any long-term liabilities.");
		loans.setClassType(liabilityAcc.get().getCategory().getName());
		
		accountRepository.saveAll(List.of(accountPayable, taxAcc, loans));
		
		// Create Equity accounts
		Optional<AccountType> equityAcc = accountTypeRepository.findByNameIgnoreCase("EQUITY");
		Accounts annualEarning = new Accounts(
				getOrganization,
				"600",
				"Current Year Earning",
				equityAcc.get()
		);
		annualEarning.setDescription("Shows the profit or loss for the current financial year.");
		annualEarning.setClassType(equityAcc.get().getCategory().getName());
		
		accountRepository.saveAll(List.of(annualEarning));
		
		// Create revenue accounts
		Optional<AccountType> salesRevenueAcc = accountTypeRepository.findByNameIgnoreCase("SALES");
		Optional<AccountType> revenueAcc = accountTypeRepository.findByNameIgnoreCase("REVENUE");
		
		Accounts salesAcc = new Accounts(
				getOrganization,
				"200",
				"Sales Account",
				salesRevenueAcc.get()
		);
		salesAcc.setDescription("For income generated from your core business operations.");
		salesAcc.setClassType(salesRevenueAcc.get().getCategory().getName());
		
		Accounts discountGiven = new Accounts(
				getOrganization,
				"201",
				"Discount Given",
				revenueAcc.get()
		);
		discountGiven.setDescription("Track any discounts you provide to customers.");
		discountGiven.setClassType(revenueAcc.get().getCategory().getName());
		
		accountRepository.saveAll(List.of(salesAcc, discountGiven));
		
		// Create expenses accounts
		Optional<AccountType> expenseAcc = accountTypeRepository.findByNameIgnoreCase("EXPENSE");
		Accounts officeExpenses = new Accounts(
				getOrganization,
				"200",
				"Office Expenses",
				expenseAcc.get()
		);
		officeExpenses.setDescription("Costs that a business incurs for the day-to-day operation and maintenance of its workspace.");
		officeExpenses.setClassType(expenseAcc.get().getCategory().getName());
		
		Accounts advertisingExpenses = new Accounts(
				getOrganization,
				"201",
				"Advertising",
				expenseAcc.get()
		);
		advertisingExpenses.setDescription("Costs a business incurs to promote its products, services, or brand to a target audience.");
		advertisingExpenses.setClassType(expenseAcc.get().getCategory().getName());
		
		Accounts consultingAndAccountingExpenses = new Accounts(
				getOrganization,
				"202",
				"Consulting & Accounting",
				expenseAcc.get()
		);
		consultingAndAccountingExpenses.setDescription("Costs a business incurs for professional services from external experts.");
		consultingAndAccountingExpenses.setClassType(expenseAcc.get().getCategory().getName());
		
		accountRepository.saveAll(List.of(officeExpenses, advertisingExpenses, consultingAndAccountingExpenses));
	}
}
