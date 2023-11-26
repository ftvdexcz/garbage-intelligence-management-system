package ptit.gms.store.mysql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "bins_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BinEntity {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "company")
    private String company;

    @Column(name = "lat")
    private BigDecimal lat;

    @Column(name = "lon")
    private BigDecimal lon;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "address")
    private String address;

    @Column(name = "created_user")
    private String createdUser;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_date")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "updated_date")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @Column(name = "status")
    private int status;
}
