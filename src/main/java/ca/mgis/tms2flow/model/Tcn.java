package ca.mgis.tms2flow.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
public class Tcn {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceTcn")
	@SequenceGenerator(name = "sequenceTcn", sequenceName = "sequence-tcn", allocationSize = 1)
	private Long id;

}
