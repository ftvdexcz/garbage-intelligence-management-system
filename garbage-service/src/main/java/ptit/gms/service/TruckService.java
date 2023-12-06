package ptit.gms.service;

import ptit.gms.dto.request.CreateTruckReqDto;
import ptit.gms.dto.response.GetBinResDto;
import ptit.gms.dto.response.PaginationResDto;
import ptit.gms.dto.response.TruckResDto;

public interface TruckService {
    TruckResDto createTruck(CreateTruckReqDto createTruckReqDto);

    PaginationResDto<TruckResDto> listBinsPagination(int page, int size, String sortBy);

    TruckResDto getTruck(String plate);

    void deleteTruck(String plate);
}
