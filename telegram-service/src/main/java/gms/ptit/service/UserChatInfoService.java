package gms.ptit.service;

import gms.ptit.mysql.entity.UserChatInfoEntity;

import java.util.List;

public interface UserChatInfoService {
    UserChatInfoEntity getUserInfoByChatId(String chatId);

    int subscribeWithChatId(long chatId, int eventType);

    int deleteWithChatId(long chatId);

    List<UserChatInfoEntity> findAllByTypeIn(List<Integer> types);
}
