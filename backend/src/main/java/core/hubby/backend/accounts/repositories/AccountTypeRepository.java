package core.hubby.backend.accounts.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import core.hubby.backend.accounts.entities.AccountType;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, UUID> {
	Optional<AccountType> findByNameIgnoreCase(String name);
}
