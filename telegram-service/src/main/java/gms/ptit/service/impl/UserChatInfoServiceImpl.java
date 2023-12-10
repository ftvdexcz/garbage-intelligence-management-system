package gms.ptit.service.impl;

import gms.ptit.constant.Constants;
import gms.ptit.mysql.entity.UserChatInfoEntity;
import gms.ptit.mysql.repository.UserChatInfoRepository;
import gms.ptit.service.UserChatInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class UserChatInfoServiceImpl implements UserChatInfoService {
    @Autowired
    UserChatInfoRepository userChatInfoRepository;

    @Override
    public UserChatInfoEntity getUserInfoByChatId(String chatId) {
        return userChatInfoRepository.findByChatId(chatId);
    }

    @Override
    public int subscribeWithChatId(long chatId, int eventType) {
        UserChatInfoEntity userChatInfoEntity = userChatInfoRepository.findByChatId(String.valueOf(chatId));
        if(userChatInfoEntity != null){
            userChatInfoEntity.setType(eventType);
            userChatInfoRepository.save(userChatInfoEntity);
            return Constants.UPDATE_EVENT_TYPE_SUBSCRIBE_STATUS;
        }

        UserChatInfoEntity createUserChat = UserChatInfoEntity.builder().
                chatId(String.valueOf(chatId)).
                type(eventType).
                build();
        userChatInfoRepository.save(createUserChat);
        return Constants.CREATE_EVENT_TYPE_SUBSCRIBE_STATUS;
    }

    @Override
    @Transactional
    public int deleteWithChatId(long chatId) {
        return userChatInfoRepository.deleteByChatId(String.valueOf(chatId));
    }

    @Override
    public List<UserChatInfoEntity> findAllByTypeIn(List<Integer> types) {
        return userChatInfoRepository.findByTypeIn(types);
    }
}
