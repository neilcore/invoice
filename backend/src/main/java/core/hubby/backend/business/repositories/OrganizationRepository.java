package core.hubby.backend.business.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import core.hubby.backend.business.entities.Organization;
import core.hubby.backend.core.data.BaseJpaRepository;

@Repository
public interface OrganizationRepository extends BaseJpaRepository<Organization, UUID> {
	
	/* INFO: Tax basis */
	static final String TAX_BASIS_CASH = "CASH";
	static final String TAX_BASIS_ACCRUAL = "ACCRUAL";
	
	/* INFO: LIST OF CURRENCIES */
	static final String CURRENCY_UNITES_STATES = "USD";
	static final String CURRENCY_MEXICO = "MXN";
	static final String CURRENY_JAPAN = "JPY";
	static final String CURRENCY_CHINA = "CNY";
	static final String CURRENCY_INDIA = "INR";
	static final String CURRENCY_BRAZIL = "BRL";
	static final String CURRENCY_SOUTH_AFRICA = "ZAR";
	static final String CURRENCY_EGYPT = "EGP";
	static final String CURRENCY_PHILIPPINES = "PHP";
	static final String CURRENCY_SWITZERLAND = "CHF";
	
	// Organization user invitation status
	static final String INVITATION_STATUS_PENDING = "PENDING";
	static final String INVITATION_STATUS_ACCEPTING = "ACCEPTED";
	static final String INVITATION_STATUS_DECLINED = "DECLINED";
	static final String INVITATION_STATUS_CANCELLED = "CANCELLED";
	
	// Supported external links
	static final String EXTERNAL_LINK_FACEBOOK = "FACEBOOK";
	static final String EXTERNAL_LINK_INSTAGRAM = "INSTAGRAM";
	static final String EXTERNAL_LINK_WEBSITE = "WEBSITE";
	static final String EXTERNAL_LINK_TIKTOK = "TIKTOK";
	
	// Supported phone type
	static final String PHONE_TYPE_DEFAULT = "DEFAULT";
	static final String PHONE_TYPE_DDI = "DDI";
	static final String PHONE_TYPE_MOBILE = "MOBILE";
	static final String PHONE_TYPE_FAX = "FAX";
	
	// Address lines
	static final String ADDRESS_LINE_1 = "addressLine1";
	static final String ADDRESS_LINE_2 = "addressLine2";
	static final String ADDRESS_LINE_3 = "addressLine3";
	static final String ADDRESS_LINE_4 = "addressLine4";
	
	@Query("select org from Organization org where org.id = :id")
	Optional<Organization> findOrganizationById(@Param("id") UUID id);
	
	
}
