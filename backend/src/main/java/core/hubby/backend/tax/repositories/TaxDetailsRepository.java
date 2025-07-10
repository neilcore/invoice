package core.hubby.backend.tax.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import core.hubby.backend.tax.entities.TaxDetails;

@Repository
public interface TaxDetailsRepository extends JpaRepository<TaxDetails, UUID> {

}
