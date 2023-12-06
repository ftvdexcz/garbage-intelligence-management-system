package ptit.gms.store.mysql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "garbage_truck")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GarbageTruckEntity {
    @Id
    @Column(name = "plate")
    private String plate;

    @Column(name = "company")
    private String company;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "created_date")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "updated_date")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
}
