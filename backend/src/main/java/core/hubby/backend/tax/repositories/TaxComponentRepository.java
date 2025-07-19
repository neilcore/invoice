package core.hubby.backend.tax.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import core.hubby.backend.tax.entities.TaxComponent;

@Repository
public interface TaxComponentRepository extends JpaRepository<TaxComponent, UUID> {

}
