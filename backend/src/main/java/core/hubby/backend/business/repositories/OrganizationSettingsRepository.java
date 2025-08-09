package core.hubby.backend.business.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import core.hubby.backend.business.entities.OrganizationSettings;
import core.hubby.backend.business.entities.embedded.InvoiceSettings;


@Repository
public interface OrganizationSettingsRepository extends JpaRepository<OrganizationSettings, UUID> {
	
	@Query("SELECT os.invoiceSettings FROM OrganizationSettings os WHERE os.organization.id = :orgnUuid")
	InvoiceSettings findSettingsByOrganizationId(@Param("organizationID") UUID orgnUuid);
}
