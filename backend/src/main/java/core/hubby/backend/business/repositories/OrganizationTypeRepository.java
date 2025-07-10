package core.hubby.backend.business.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import core.hubby.backend.business.entities.OrganizationType;

@Repository
public interface OrganizationTypeRepository extends JpaRepository<OrganizationType, UUID> {

}
