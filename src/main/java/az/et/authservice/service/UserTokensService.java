package az.et.authservice.service;

import az.et.authservice.entity.UserTokensEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserTokensService {

    UserTokensEntity findByUserIdOrNew(Long userId);

    void insertOrUpdate(UserTokensEntity userTokensEntity);

}
