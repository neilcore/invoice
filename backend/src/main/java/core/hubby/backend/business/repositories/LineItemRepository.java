package core.hubby.backend.business.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import core.hubby.backend.business.entities.LineItems;

@Repository
public interface LineItemRepository extends JpaRepository<LineItems, UUID> {
	// LineAmount types
	static final String LINE_AMOUNT_TYPE_EXCLUSIVE = "EXCLUSIVE"; //Line items are exclusive of tax
	static final String LINE_AMOUNT_TYPE_INCLUSIVE = "INCLUSIVE"; //Line items are inclusive tax
	static final String LINE_AMOUNT_TYPE_NO_TAX = "NO_TAX"; //Line have no tax

}
