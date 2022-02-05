package az.et.authservice.service.functional;

import az.et.authservice.entity.UserTokensEntity;
import az.et.authservice.repository.UserTokensRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserTokensService {

    private final UserTokensRepository userTokensRepository;

    public UserTokensEntity findByUserIdOrNew(Long userId) {
        return userTokensRepository.findByUserId(userId)
                .orElse(new UserTokensEntity());
    }

    public void insertOrUpdate(UserTokensEntity userTokensEntity) {
        userTokensRepository.save(userTokensEntity);
    }
}
