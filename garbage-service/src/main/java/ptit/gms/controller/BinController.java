package ptit.gms.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ptit.gms.constant.CodeResponse;
import ptit.gms.dto.ResponseObject;
import ptit.gms.dto.request.CreateBinReqDto;
import ptit.gms.dto.request.UpdateBinReqDto;
import ptit.gms.dto.response.CreateBinResDto;
import ptit.gms.dto.response.GetBinResDto;
import ptit.gms.dto.response.PaginationResDto;
import ptit.gms.dto.response.UpdateBinResDto;
import ptit.gms.service.BinService;
import ptit.gms.utils.ResponseUtils;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bins")
public class BinController {
    @Autowired
    BinService binService;

    @PostMapping("")
    public ResponseEntity<ResponseObject> createBin(
            @Valid @RequestPart("data") CreateBinReqDto createBinReqDto,
            @RequestPart(value = "bin_image", required = false) MultipartFile file){
        CreateBinResDto createBinResDto = binService.createBin(createBinReqDto, file);

        return ResponseUtils.responseWithCode(CodeResponse.CREATED, createBinResDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getBin(@PathVariable String id){
        GetBinResDto getBinResDto = binService.getBin(id);

        return ResponseUtils.responseWithCode(CodeResponse.OK, getBinResDto);
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> listBins(){
        List<GetBinResDto> listBins = binService.listBins();

        return ResponseUtils.responseWithCode(CodeResponse.OK, listBins);
    }

    @GetMapping("listPagination")
    public ResponseEntity<ResponseObject> listBinsPagination(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "company-asc") String sort
    ){
        PaginationResDto<GetBinResDto> listBins = binService.listBinsPagination(page - 1, size, sort);

        return ResponseUtils.responseWithCode(CodeResponse.OK, listBins);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteBin(@PathVariable String id){
        binService.deleteBin(id);

        return ResponseUtils.responseWithCodeAndMsg(CodeResponse.OK, "Xóa thành công", null);
    }

    @PostMapping("/updateBin/{id}")
    public ResponseEntity<ResponseObject> updateBin(@PathVariable String id,
                                                    @Valid @RequestPart("data") UpdateBinReqDto updateBinResDto,
                                                    @RequestPart(value = "bin_image", required = false) MultipartFile file){
        UpdateBinResDto updated = binService.updateBin(id, updateBinResDto, file);

        return ResponseUtils.responseWithCodeAndMsg(CodeResponse.OK, "Cập nhật thành công", updated);
    }
}

