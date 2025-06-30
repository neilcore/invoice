package core.hubby.backend.business.repositories;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import core.hubby.backend.business.entities.PaymentTerms;
import core.hubby.backend.core.data.BaseJpaRepository;

@Repository
public interface PaymentTermsRepository extends BaseJpaRepository<PaymentTerms, UUID> {
	// Payment terms
	static final String PAYMENT_TERM_DAYSAFTERBILLDATE = "DAYSAFTERBILLDATE"; // day(s) after bill date
	static final String PAYMENT_TERM_DAYSAFTERBILLMONTH = "DAYSAFTERBILLMONTH"; // day(s) after bill month
	static final String PAYMENT_TERM_OFCURRENTMONTH = "OFCURRENTMONTH"; // of the current month
	static final String PAYMENT_TERM_OFFOLLOWINGMONTH = "OFFOLLOWINGMONTH"; // of the following month
}
