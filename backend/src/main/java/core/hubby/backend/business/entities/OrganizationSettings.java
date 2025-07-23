package core.hubby.backend.business.entities;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import core.hubby.backend.business.entities.embedded.InvoiceSettings;
import core.hubby.backend.business.entities.embedded.LineItemSettings;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This is a configuration settings for organization object.
 * Setting default functionalities or values happen here.
 */
@Entity
@Table(name = "organization_setting", schema = "app_sc")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrganizationSettings implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "setting_id", nullable = false, unique = true)
	private UUID settingId;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id", nullable = false, unique = true, referencedColumnName = "id")
	private Organization organization;
	
	// Default settings for invoices
	@Embedded
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "invoice_settings")
	private InvoiceSettings invoiceSettings;
	
	// Default settings for line items
	@Embedded
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "line_item_settings")
	private LineItemSettings lineItemSettings;
}
