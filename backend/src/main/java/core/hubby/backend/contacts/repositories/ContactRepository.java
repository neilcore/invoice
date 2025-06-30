package core.hubby.backend.contacts.repositories;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import core.hubby.backend.contacts.entities.Contact;
import core.hubby.backend.core.data.BaseJpaRepository;

@Repository
public interface ContactRepository extends BaseJpaRepository<Contact, UUID> {
	// Contact status
	static final String CONTACT_STATUS_ACTIVE = "ACTIVE"; // The Contact is active and can be used in transactions
	static final String CONTACT_STATUS_ARCHIVED = "ARCHIVED"; // The Contact is archived and can no longer be used in transactions
	static final String CONTACT_STATUS_GDPRREQUEST = "GDPRREQUEST"; // The Contact is the subject of a GDPR erasure request and can no longer be used in transactions
}
