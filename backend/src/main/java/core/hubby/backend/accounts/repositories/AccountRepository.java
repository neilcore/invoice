package core.hubby.backend.accounts.repositories;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import core.hubby.backend.accounts.entities.Accounts;
import core.hubby.backend.accounts.repositories.projections.AccountLookup;


@Repository
public interface AccountRepository extends JpaRepository<Accounts, UUID> {
	// Statuses
	static final String ACCOUNT_STATUS_ACTIVE = "ACTIVE";
	static final String ACCOUNT_STATUS_ARCHIVED = "ARCHIVED";
	
	// Account class types
	static final String ACC__CLASS_TYPE_ASSET = "ASSET";
	static final String ACC__CLASS_TYPE_EQUITY = "EQUITY";
	static final String ACC__CLASS_TYPE_EXPENSE = "EXPENSE";
	static final String ACC__CLASS_TYPE_LIABILITY = "LIABILITY";
	static final String ACC__CLASS_TYPE_REVENUE = "REVENUE";
	
	@Query(
			"SELECT ac.code FROM Accounts ac WHERE ac.code = :code " +
			"AND ac.organization = :organizationId"
	)
	Optional<AccountLookup> findAccountByCodeAndOrganization(
			@Param("code") String code,
			@Param("organizationId") UUID organizationId
	);
	
	boolean existsByCodeIgnoreCase(String code);
	
	Set<Accounts> findByClassTypeIgnoreCase(String classType);
	
	@Query("SELECT ac FROM Accounts ac WHERE ac.archived = false")
	Set<Accounts> findAllNotArchived();
}
