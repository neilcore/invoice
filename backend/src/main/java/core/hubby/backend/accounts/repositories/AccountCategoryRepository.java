package core.hubby.backend.accounts.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import core.hubby.backend.accounts.entities.AccountCategory;

@Repository
public interface AccountCategoryRepository extends JpaRepository<AccountCategory, UUID> {

}
