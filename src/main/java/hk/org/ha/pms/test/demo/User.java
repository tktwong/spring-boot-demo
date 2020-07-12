package hk.org.ha.pms.test.demo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message="Name is mandatory")
    private String name;
}
