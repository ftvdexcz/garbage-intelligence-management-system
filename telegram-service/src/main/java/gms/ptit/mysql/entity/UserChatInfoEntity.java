package gms.ptit.mysql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "user_chat_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserChatInfoEntity {
    @Id
    @Column(name = "chat_id")
    private String chatId;

    @Column(name = "username")
    private String username;

    @Column(name = "type")
    private int type;

    @Column(name = "created_date")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "updated_date")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
}
