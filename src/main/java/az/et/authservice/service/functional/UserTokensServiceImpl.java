package az.et.authservice.service.functional;

import az.et.authservice.entity.UserTokensEntity;
import az.et.authservice.repository.UserTokensRepository;
import az.et.authservice.service.UserTokensService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserTokensServiceImpl implements UserTokensService {

    private final UserTokensRepository userTokensRepository;

    @Override
    public UserTokensEntity findByUserIdOrNew(Long userId) {
        return userTokensRepository.findByUserId(userId)
                .orElse(new UserTokensEntity());
    }

    @Override
    public void insertOrUpdate(UserTokensEntity userTokensEntity) {
        userTokensRepository.save(userTokensEntity);
    }
}
