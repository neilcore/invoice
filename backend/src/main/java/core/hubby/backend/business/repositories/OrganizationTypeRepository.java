package core.hubby.backend.business.repositories;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import core.hubby.backend.business.entities.OrganizationType;
import core.hubby.backend.core.data.BaseJpaRepository;

@Repository
public interface OrganizationTypeRepository extends BaseJpaRepository<OrganizationType, UUID> {

}
