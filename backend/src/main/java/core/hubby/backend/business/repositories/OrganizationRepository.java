package core.hubby.backend.business.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import core.hubby.backend.business.entities.Organization;
import core.hubby.backend.business.entities.UserAccount;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
	
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
	
	// Payment terms Elements
	static final String PAYMENT_TERMS_BILLS = "BILLS";
	static final String PAYMENT_TERMS_SALES = "SALES";
	
	@Query("select org from Organization org where org.id = :id")
	Optional<Organization> findOrganizationById(@Param("id") UUID id);
	
	@Query(
			value = "SELECT user_id FROM organization_user_invites WHERE organization_id = ?1" +
			" AND user_role = 'SUBSCRIBER'",
			nativeQuery = true
	)
	Optional<UserAccount> findOrganizationSubscriber(UUID organizationId);
	
	/**
	 * This will retrieve lineAmountType value using organization's value
	 * @param id - accepts {@linkplain java.util.UUID} object type
	 * @return - returns a {@linkplain java.util.Optional} object type.
	 */
    @Query("SELECT org.organizationType FROM Organization org WHERE org.id = :id")
    Optional<String> findLineAmountType(@Param("id") UUID id);
	
}
