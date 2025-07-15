package core.hubby.backend.tax.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import core.hubby.backend.tax.entities.SalesTaxBasis;

@Repository
public interface SalesTaxBasisRepository extends JpaRepository<SalesTaxBasis, UUID> {

}
