package cz.mendelu.ea.xzirchuk.project.moviesAPI.domain.user;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepository extends CrudRepository<RentalUser, UUID> {

}
