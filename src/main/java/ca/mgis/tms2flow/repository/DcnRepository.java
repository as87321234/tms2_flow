package ca.mgis.tms2flow.repository;


import ca.mgis.tms2flow.model.Dcn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface DcnRepository extends JpaRepository<Dcn, Long> {
	
	void getNewDcn();
	
}
