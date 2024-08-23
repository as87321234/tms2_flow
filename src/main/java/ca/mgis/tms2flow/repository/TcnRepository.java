package ca.mgis.tms2flow.repository;


import ca.mgis.tms2flow.model.Dcn;
import ca.mgis.tms2flow.model.Tcn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface TcnRepository extends JpaRepository<Tcn, Long> {
	
	Tcn getNewTcn();
}
