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
import ptit.gms.dto.request.CreateTruckReqDto;
import ptit.gms.dto.response.CreateBinResDto;
import ptit.gms.dto.response.GetBinResDto;
import ptit.gms.dto.response.PaginationResDto;
import ptit.gms.dto.response.TruckResDto;
import ptit.gms.service.TruckService;
import ptit.gms.utils.ResponseUtils;

@Slf4j
@RestController
@RequestMapping("/trucks")
public class TruckController {
    @Autowired
    TruckService truckService;

    @PostMapping("")
    public ResponseEntity<ResponseObject> createTruck(
            @Valid @RequestBody CreateTruckReqDto createTruckReqDto){
        TruckResDto truckResDto = truckService.createTruck(createTruckReqDto);

        return ResponseUtils.responseWithCode(CodeResponse.CREATED, truckResDto);
    }

    @GetMapping("listPagination")
    public ResponseEntity<ResponseObject> listBinsPagination(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "company-asc") String sort
    ){
        PaginationResDto<TruckResDto> listTrucks = truckService.listBinsPagination(page - 1, size, sort);

        return ResponseUtils.responseWithCode(CodeResponse.OK, listTrucks);
    }

    @GetMapping("/{plate}")
    public ResponseEntity<ResponseObject> getTruck(@PathVariable String plate){
        TruckResDto truckResDto = truckService.getTruck(plate);

        return ResponseUtils.responseWithCode(CodeResponse.OK, truckResDto);
    }


    @DeleteMapping("/{plate}")
    public ResponseEntity<ResponseObject> deleteTruck(@PathVariable String plate){
        truckService.deleteTruck(plate);

        return ResponseUtils.responseWithCodeAndMsg(CodeResponse.OK, "Xóa thành công", null);
    }
}
