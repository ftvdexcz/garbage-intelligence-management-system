package ptit.gms.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class GetBinResDto {
    @JsonProperty("id")
    private String id;

    @JsonProperty("company")
    private String company;

    @JsonProperty("address")
    private String address;

    @JsonProperty("lat")
    private BigDecimal lat;

    @JsonProperty("lon")
    private BigDecimal lon;

    @JsonProperty("capacity")
    private int capacity;

    @JsonProperty("created_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdDate;

    @JsonProperty("updated_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date updatedDate;

    @JsonProperty("created_user")
    private String createdUser;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("weight")
    private TotalCellResDto totalCell;

    @JsonProperty("owner")
    private CompanyOwnerResDto owner;

    public GetBinResDto(String id, String company, String address, BigDecimal lat, BigDecimal lon,
                        int totalWeight, long updatedTimestamp, int capacity, Date createdDate, Date updatedDate,
                        String createdUser, String imageUrl, String ownerId, String ownerName, String ownerPhone, String ownerEmail){
        this.id = id;
        this.company = company;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
        this.capacity = capacity;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.createdUser = createdUser;
        this.imageUrl = imageUrl;
        TotalCellResDto totalCell = TotalCellResDto.builder().
                totalWeight(totalWeight).
                updatedTimestamp(updatedTimestamp).
                build();
        this.totalCell = totalCell;
        CompanyOwnerResDto owner = CompanyOwnerResDto.builder().
                id(ownerId).
                name(ownerName).
                phone(ownerPhone).
                email(ownerEmail).
                build();
        this.owner = owner;
    }
}
