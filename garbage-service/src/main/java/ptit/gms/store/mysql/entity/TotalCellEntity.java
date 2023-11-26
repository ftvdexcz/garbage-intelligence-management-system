package ptit.gms.store.mysql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cell_totals")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TotalCellEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "bin_code")
    private String binCode;

    @Column(name = "weight")
    private int weight;

    @Column(name = "updated_timestamp")
    private long updatedTimestamp;
}
