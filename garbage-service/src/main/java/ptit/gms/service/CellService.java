package ptit.gms.service;

import ptit.gms.dto.request.LoadCellReqDto;
import ptit.gms.dto.response.LoadCellResDto;

public interface CellService {
    LoadCellResDto loadCell(LoadCellReqDto loadCellReqDto);
}
