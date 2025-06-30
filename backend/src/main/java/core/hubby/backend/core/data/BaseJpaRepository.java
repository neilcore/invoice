package core.hubby.backend.core.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseJpaRepository<E, ID> extends JpaRepository<E, ID> {

	// Statuses
	static final String STATUS_ACTIVE = "active";
	static final String STATU__DRAFT = "draft";
	static final String STATUS_INACTIVE = "inactive";
	static final String STATUS_DELETED = "deleted";
	static final String STATUS_ARCHIVED = "archived";
	static final String STATU_PENDING = "pending";
	static final String STATUS_COMPLETED = "completed";
	static final String STATUS_FAILED = "failed";
	static final String STATUS_CANCELLED = "cancelled";
	static final String STATUS_APPROVED = "approved";
	static final String STATUS_REJECTED = "rejected";
	static final String STATUS_EXPIRED = "expired";
}
