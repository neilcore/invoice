package core.hubby.backend.accounts.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import core.hubby.backend.accounts.entities.Accounts;

@Repository
public interface AccountRepository extends JpaRepository<Accounts, UUID> {
	// Statuses
	static final String ACCOUNT_STATUS_ACTIVE = "ACTIVE";
	static final String ACCOUNT_STATUS_ARCHIVED = "ARCHIVED";
}
