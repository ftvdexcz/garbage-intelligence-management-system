package ptit.gms.service;

import org.springframework.web.multipart.MultipartFile;
import ptit.gms.dto.request.CreateBinReqDto;
import ptit.gms.dto.request.UpdateBinReqDto;
import ptit.gms.dto.response.CreateBinResDto;
import ptit.gms.dto.response.GetBinResDto;
import ptit.gms.dto.response.PaginationResDto;
import ptit.gms.dto.response.UpdateBinResDto;

import java.util.List;

public interface BinService {
    CreateBinResDto createBin(CreateBinReqDto createBinReqDto, MultipartFile file);

    GetBinResDto getBin(String id);

    List<GetBinResDto> listBins();

    PaginationResDto<GetBinResDto> listBinsPagination(int page, int size, String sortBy);

    void deleteBin(String id);

    UpdateBinResDto updateBin(String id, UpdateBinReqDto updateBinResDto, MultipartFile file);
}
