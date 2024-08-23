package ca.mgis.tms2flow.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity

public class Dcn {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceDcn")
	@SequenceGenerator(name = "sequenceDcn", sequenceName = "sequence-dcn", allocationSize = 1)
	private Long id;
	
}
