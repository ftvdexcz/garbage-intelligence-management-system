package ptit.gms.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ptit.gms.constant.Constant;
import ptit.gms.dto.request.LoadCellReqDto;
import ptit.gms.dto.response.LoadCellResDto;
import ptit.gms.exception.ApiException;
import ptit.gms.service.CellService;
import ptit.gms.store.mysql.entity.BinEntity;
import ptit.gms.store.mysql.entity.CellEntity;
import ptit.gms.store.mysql.entity.TotalCellEntity;
import ptit.gms.store.mysql.repository.BinRepository;
import ptit.gms.store.mysql.repository.CellRepository;
import ptit.gms.store.mysql.repository.TotalCellRepository;
import ptit.gms.utils.TimeUtils;
import java.util.Optional;

@Slf4j
@Service
public class CellServiceImpl implements CellService {
    @Autowired
    CellRepository cellRepository;

    @Autowired
    BinRepository binRepository;

    @Autowired
    TotalCellRepository totalCellRepository;

    @Override
    @Transactional
    public LoadCellResDto loadCell(LoadCellReqDto loadCellReqDto) {
        log.info("[CellServiceImpl - loadCell] loadCellReqDto: {}", loadCellReqDto);

        String binCode = loadCellReqDto.getBinCode();

        Optional<BinEntity> bin = binRepository.findById(binCode);
        if(bin.isEmpty()){
            throw ApiException.ErrNotFound().message(String.format("bin_code %s (mã điểm thu) không tồn tại", binCode)).build();
        }

        BinEntity binEntity = bin.get();
        if(binEntity.getStatus() != Constant.STATUS_BIN_ACTIVE){
            throw ApiException.ErrNotFound().message(String.format("bin_code %s (mã điểm thu) không tồn tại", binCode)).build();
        }

        long curTimestamp = TimeUtils.getCurrentTimestampMs();
        CellEntity cell = CellEntity.builder().
                binCode(binCode).
                weight(loadCellReqDto.getWeight()).
                timestamp(curTimestamp).
                build();

        cellRepository.save(cell);

        TotalCellEntity totalCell =  totalCellRepository.findByBinCode(binCode);
        totalCell.setWeight(totalCell.getWeight() + loadCellReqDto.getWeight());
        totalCell.setUpdatedTimestamp(curTimestamp);
        totalCellRepository.save(totalCell);

        return LoadCellResDto.builder().
                binCode(binCode).
                weight(cell.getWeight()).
                totalWeight(totalCell.getWeight()).
                timestamp(cell.getTimestamp()).
                build();
    }
}
