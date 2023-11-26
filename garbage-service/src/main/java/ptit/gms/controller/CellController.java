package ptit.gms.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptit.gms.constant.CodeResponse;
import ptit.gms.dto.ResponseObject;
import ptit.gms.dto.request.LoadCellReqDto;
import ptit.gms.dto.response.LoadCellResDto;
import ptit.gms.service.CellService;
import ptit.gms.utils.ResponseUtils;

@Slf4j
@RestController
@RequestMapping("/cells")
public class CellController {
    @Autowired
    CellService cellService;

    @PostMapping()
    public ResponseEntity<ResponseObject> loadCell(@Valid @RequestBody LoadCellReqDto loadCellReqDto){
        LoadCellResDto loadCellResDto = cellService.loadCell(loadCellReqDto);

        return ResponseUtils.responseWithCodeAndMsg(CodeResponse.CREATED, "Cập nhật khối lượng thành công", loadCellResDto);
    }
}
