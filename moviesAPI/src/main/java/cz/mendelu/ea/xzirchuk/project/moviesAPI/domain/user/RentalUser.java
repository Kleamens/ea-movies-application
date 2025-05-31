package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsExclude;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RentalUser {
    @Id
    @EqualsExclude

    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @NotNull
    @NotEmpty
    String name;

    @NotNull
    @NotEmpty
    String surname;

    @NotNull
    @NotEmpty
    String email;
}
