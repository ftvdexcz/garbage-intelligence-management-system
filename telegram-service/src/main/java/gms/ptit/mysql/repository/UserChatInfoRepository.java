package gms.ptit.mysql.repository;

import gms.ptit.mysql.entity.UserChatInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserChatInfoRepository extends JpaRepository<UserChatInfoEntity, String> {
    UserChatInfoEntity findByChatId(String chatId);

    @Modifying
    int deleteByChatId(String chatId);

    List<UserChatInfoEntity> findByTypeIn(List<Integer> types);
}
