package core.hubby.backend.accounts.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import core.hubby.backend.accounts.entities.AccountCategory;

@Repository
public interface AccountCategoryRepository extends JpaRepository<AccountCategory, UUID> {
	// Category IDs
	static final UUID assetId = UUID.fromString("e9a3dd9f-938c-4c50-b03c-27d98fed5b05");
	static final UUID liabilityId = UUID.fromString("587f484b-0641-48ad-a5ff-51e998c48803");
	static final UUID equityId = UUID.fromString("3386b49e-13b3-4702-a510-16caff6699b5");
	static final UUID expenseId = UUID.fromString("148dfb5a-4b9a-4577-8767-555893365aa3");
	static final UUID revenueId = UUID.fromString("b1ca04e5-19bf-44a5-9f82-906ef2eec998");
}
