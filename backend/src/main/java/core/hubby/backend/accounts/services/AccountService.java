package core.hubby.backend.accounts.services;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

import core.hubby.backend.accounts.controller.dto.AccountCodeExistsResponse;
import core.hubby.backend.accounts.controller.dto.AccountResponse;
import core.hubby.backend.accounts.entities.AccountCategory;
import core.hubby.backend.accounts.entities.AccountType;
import core.hubby.backend.accounts.entities.Accounts;
import core.hubby.backend.accounts.repositories.AccountCategoryRepository;
import core.hubby.backend.accounts.repositories.AccountRepository;
import core.hubby.backend.accounts.repositories.AccountTypeRepository;
import core.hubby.backend.business.entities.Organization;
import core.hubby.backend.business.repositories.OrganizationRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
	private final AccountCategoryRepository accountCategoryRepository;
	private final AccountRepository accountRepository;
	private final AccountTypeRepository accountTypeRepository;
	private final OrganizationRepository organizationRepository;
	
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
	
	
	@Transactional
	public void createDefaultAccounts(@NotNull UUID organizationId) {
		
		Organization getOrganization = organizationRepository.findById(organizationId).get();
		
		// Account categories
		AccountCategory assetCat = accountCategoryRepository.findById(AccountCategoryRepository.assetId).get();
		AccountCategory liabilityCat = accountCategoryRepository.findById(AccountCategoryRepository.liabilityId).get();
		AccountCategory revenueCat = accountCategoryRepository.findById(AccountCategoryRepository.revenueId).get();
		AccountCategory equityCat = accountCategoryRepository.findById(AccountCategoryRepository.equityId).get();
		AccountCategory expenseCat = accountCategoryRepository.findById(AccountCategoryRepository.expenseId).get();
		
		// Create asset accounts
		AccountType currentAsset = accountTypeRepository.save(new AccountType("Current Asset", ""));
		Accounts accountReceivable = new Accounts(
				getOrganization,
				"100",
				"Receivable Account",
				currentAsset,
				assetCat
		);
		accountReceivable.setDescription("Money owed to the busines.");
		
		Accounts preparedExpense = new Accounts(
				getOrganization,
				"103",
				"Prepared Expenses",
				currentAsset,
				assetCat
		);
		preparedExpense.setDescription("Expenses paid in advance.");
		
		AccountType inventoryType = accountTypeRepository.save(new AccountType("Inventory", ""));
		Accounts inventoryAssetAccount = new Accounts(
				getOrganization,
				"101",
				"Inventory",
				inventoryType,
				assetCat
		);
		inventoryAssetAccount.setDescription("Account for businesses that hold and track stock.");
		
		AccountType fixedAssetType = accountTypeRepository.save(new AccountType("Fixed Assets", ""));
		Accounts fixedAssetAccount = new Accounts(
				getOrganization,
				"102",
				"Fixed Asset",
				fixedAssetType,
				assetCat
		);
		fixedAssetAccount.setDescription("Accounts for long-term assets.");
		
		accountRepository.saveAll(List.of(accountReceivable, preparedExpense, inventoryAssetAccount, fixedAssetAccount));
		
		// Create liability accounts
		AccountType currentLiabilityType = accountTypeRepository.save(new AccountType("Current Liability", ""));
		Accounts accountPayable = new Accounts(
				getOrganization,
				"300",
				"Payable Account",
				currentLiabilityType,
				liabilityCat
		);
		accountPayable.setDescription("Tracks money you owe to your suppliers.");
		
		AccountType liabilityType = accountTypeRepository.save(new AccountType("Liability", ""));
		Accounts loans = new Accounts(
				getOrganization,
				"302",
				"Loans Payable",
				liabilityType,
				liabilityCat
		);
		loans.setDescription("Long-term debt.");
		
		accountRepository.saveAll(List.of(accountPayable, loans));
		
		// Create Equity accounts
		AccountType equityType = accountTypeRepository.save(new AccountType("Equity", ""));
		Accounts annualEarning = new Accounts(
				getOrganization,
				"600",
				"Retained Earnings",
				equityType,
				equityCat
		);
		annualEarning.setDescription("Profits retained in the busines.");
		
		Accounts ownersDrawing = new Accounts(
				getOrganization,
				"601",
				"Owner’s Drawings",
				equityType,
				equityCat
		);
		ownersDrawing.setDescription("Owner withdrawals.");
		
		accountRepository.saveAll(List.of(annualEarning, ownersDrawing));
		
		// Create revenue accounts
		AccountType revenueType = accountTypeRepository.save(new AccountType("Revenue", ""));
		Accounts salesRevenueAcc = new Accounts(
				getOrganization,
				"200",
				"Sales Revenue",
				revenueType,
				revenueCat
		);
		salesRevenueAcc.setDescription("Income from core operations.");
		
		Accounts serviceIncomeAcc = new Accounts(
				getOrganization,
				"201",
				"Service Income",
				revenueType,
				revenueCat
		);
		serviceIncomeAcc.setDescription("Fees for services provided.");
		
		accountRepository.saveAll(List.of(salesRevenueAcc, serviceIncomeAcc));
		
		// Create expenses accounts
		AccountType expenseType = accountTypeRepository.save(new AccountType("Expense", ""));
		Accounts officeExpenses = new Accounts(
				getOrganization,
				"300",
				"Office Expenses",
				expenseType,
				expenseCat
		);
		officeExpenses.setDescription("Costs that a business incurs for the day-to-day operation and maintenance of its workspace.");
		
		Accounts advertisingExpenses = new Accounts(
				getOrganization,
				"301",
				"Marketing Expense",
				expenseType,
				expenseCat
		);
		advertisingExpenses.setDescription("Costs a business incurs to promote its products, services, or brand to a target audience.");
		
		Accounts consultingAndAccountingExpenses = new Accounts(
				getOrganization,
				"302",
				"Consulting & Accounting",
				expenseType,
				expenseCat
		);
		consultingAndAccountingExpenses.setDescription("Costs a business incurs for professional services from external experts.");
		
		AccountType directCost = accountTypeRepository.save(new AccountType("Direct Costs", ""));
		Accounts costOfGoodsSold = new Accounts(
				getOrganization,
				"303",
				"Cost of Goods Sold",
				directCost,
				expenseCat
		);
		costOfGoodsSold.setDescription("Cost of materials / production.");
		
		Accounts utilities = new Accounts(
				getOrganization,
				"304",
				"Utilities",
				expenseType,
				expenseCat
		);
		utilities.setDescription("Electricity, water, etc.");
		
		Accounts travelAndEntertainment = new Accounts(
				getOrganization,
				"305",
				"Travel & Entertainment",
				expenseType,
				expenseCat
		);
		travelAndEntertainment.setDescription("Business travel, meals.");
		
		accountRepository.saveAll(List.of(
				officeExpenses, advertisingExpenses,
				consultingAndAccountingExpenses, costOfGoodsSold,
				utilities, travelAndEntertainment
		));
	}
}
