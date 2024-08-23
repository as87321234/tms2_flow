package ca.mgis.tms2flow.repository;


import ca.mgis.tms2flow.model.Tcn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@SuppressWarnings("unused")
@Repository
public interface TcnRepository extends JpaRepository<Tcn, Long> {
	
	@Query(value = "SELECT NEXTVAL('SEQUENCE_TCN')", nativeQuery = true)
	public Long getNextVal();
	
}
