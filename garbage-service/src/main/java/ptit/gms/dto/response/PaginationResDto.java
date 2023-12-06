package ptit.gms.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginationResDto<T> {
    @JsonProperty("totals")
    private long totals;

    @JsonProperty("pages")
    private int pages;

    @JsonProperty("page")
    private int page;

    @JsonProperty("size")
    private int size;

    @JsonProperty("results")
    private List<T> results;
}
