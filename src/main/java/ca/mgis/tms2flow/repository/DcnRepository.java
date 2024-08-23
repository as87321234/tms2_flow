package ca.mgis.tms2flow.repository;


import ca.mgis.tms2flow.model.Dcn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@SuppressWarnings("unused")
@Repository
public interface DcnRepository extends JpaRepository<Dcn, Long> {
	
	@Query(value = "SELECT NEXTVAL('SEQUENCE_DCN')", nativeQuery = true)
	public Long getNextVal();
}
