package core.hubby.backend.contacts.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import core.hubby.backend.contacts.entities.PaymentTerms;

@Repository
public interface PaymentTermsRepository extends JpaRepository<PaymentTerms, UUID> {

}
