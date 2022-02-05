package az.et.authservice.repository;

import az.et.authservice.entity.UserTokensEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserTokensRepository extends JpaRepository<UserTokensEntity, Long> {

    Optional<UserTokensEntity> findByUserId(Long userId);

    List<UserTokensEntity> findAllByUserId(Long userId);

}
