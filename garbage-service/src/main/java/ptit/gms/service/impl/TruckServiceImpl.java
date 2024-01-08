package ptit.gms.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ptit.gms.constant.Constant;
import ptit.gms.dto.event.KafkaEventCheckPlate;
import ptit.gms.dto.request.CheckPlateReqDto;
import ptit.gms.dto.request.CreateTruckReqDto;
import ptit.gms.dto.response.GetBinResDto;
import ptit.gms.dto.response.PaginationResDto;
import ptit.gms.dto.response.TruckResDto;
import ptit.gms.exception.ApiException;
import ptit.gms.service.TruckService;
import ptit.gms.store.kafka.KafkaProducerService;
import ptit.gms.store.mysql.entity.BinEntity;
import ptit.gms.store.mysql.entity.GarbageTruckEntity;
import ptit.gms.store.mysql.repository.BinRepository;
import ptit.gms.store.mysql.repository.GarbageTruckRepository;
import ptit.gms.utils.TimeUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TruckServiceImpl implements TruckService {
    @Autowired
    GarbageTruckRepository garbageTruckRepository;

    @Autowired
    KafkaProducerService kafkaProducerService;

    @Autowired
    BinRepository binRepository;

    @Override
    public TruckResDto getTruck(String plate) {
        log.info("[TruckServiceImpl - getTruck] plate: {}", plate);

        TruckResDto truck = garbageTruckRepository.getTruckByPlate(plate);
        if(!Constant.X_USER_ROLE.equals(Constant.ADMIN_ROLE_TYPE)){
            throw ApiException.ErrUnauthorized().build();
        }

        if(truck == null){
            throw ApiException.ErrNotFound().message(String.format("plate %s (biển số) không tồn tại", plate)).build();
        }

        return truck;
    }
    @Override
    public TruckResDto createTruck(CreateTruckReqDto createTruckReqDto) {
        log.info("[TruckServiceImpl - createTruck] createTruckReqDto: {}", createTruckReqDto);

        if(!Constant.X_USER_ROLE.equals(Constant.ADMIN_ROLE_TYPE))
            throw ApiException.ErrUnauthorized().build();

        TruckResDto truckResDto = garbageTruckRepository.getTruckByPlate(createTruckReqDto.getPlate());
        if(truckResDto != null)
            throw ApiException.ErrExisted().message("Biển số đã tồn tại").build();

        GarbageTruckEntity garbageTruckEntity = GarbageTruckEntity.builder().
                plate(createTruckReqDto.getPlate()).
                company(createTruckReqDto.getCompany()).
                capacity(createTruckReqDto.getCapacity()).
                build();

        garbageTruckRepository.save(garbageTruckEntity);
        return TruckResDto.builder().
                plate(createTruckReqDto.getPlate()).
                company(createTruckReqDto.getCompany()).
                capacity(createTruckReqDto.getCapacity()).
                build();
    }

    @Override
    public PaginationResDto<TruckResDto> listBinsPagination(int page, int size, String sortBy) {
        log.info("[TruckServiceImpl - listBinsPagination]");
        List<Sort.Order> orderList = new ArrayList<>();
        try {
            String[] sortMap = sortBy.split(",");
            for(String sortOption: sortMap) {
                var tmp = sortOption.split("-");
                String key = tmp[0];
                String sortDirection = tmp[1];
                Sort.Direction direction;
                if(sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())) {
                    direction = Sort.Direction.ASC;
                }else if(sortDirection.equalsIgnoreCase(Sort.Direction.DESC.name())) {
                    direction = Sort.Direction.DESC;
                }else {
                    throw ApiException.ErrInvalidArgument().build();
                }
                orderList.add(new Sort.Order(direction, key));
            }
        }catch(Exception ex) {
            throw ApiException.ErrInvalidArgument().build();
        }
        Pageable paginationOption = PageRequest.of(page, size, Sort.by(orderList));
        if (Constant.X_USER_ROLE.equals(Constant.ADMIN_ROLE_TYPE)){
            Page<TruckResDto> pageTrucks = garbageTruckRepository.listBinsPagination(paginationOption);
            return PaginationResDto.<TruckResDto>builder().totals(pageTrucks.getTotalElements()).
                    pages(pageTrucks.getTotalPages()).
                    page(page + 1).
                    size(size).
                    results(pageTrucks.getContent()).
                    build();
        } else {
            throw ApiException.ErrUnauthorized().build();
        }
    }

    @Override
    @Transactional
    public void deleteTruck(String plate){
        log.info("[TruckServiceImpl - deleteTruck] plate: {}", plate);
        int deleted = garbageTruckRepository.deleteByPlate(plate);
        if (deleted == 0){
            throw ApiException.ErrNotFound().message(String.format("plate %s (biển số) không tồn tại", plate)).build();
        }
    }

    @Override
    public void checkPlate(CheckPlateReqDto checkPlateReqDto, MultipartFile file) {
        log.info("[TruckServiceImpl - checkPlate] checkPlateReqDto: {}", checkPlateReqDto);
        if(!Constant.X_USER_ROLE.equals(Constant.ADMIN_ROLE_TYPE) && !Constant.X_USER_ROLE.equals((Constant.INTERNAL_ROLE_TYPE))){
            throw ApiException.ErrUnauthorized().build();
        }
        List<String> plates = garbageTruckRepository.getAllPlates();
        List<String> detectPlates = checkPlateReqDto.getPlates();
        Map<String, Boolean> checks = new HashMap<>();
        for(String d: detectPlates){
            for(String p: plates){
                if(StringUtils.getJaroWinklerDistance(d, p) > 0.8){
                    checks.put(d, true);
                }
            }
            if(checks.containsKey(d) == false){
                checks.putIfAbsent(d, false);
            }
        }

        BinEntity bin = binRepository.findByStatusAndId(Constant.STATUS_BIN_ACTIVE, checkPlateReqDto.getBinId());
        if(bin == null){
            log.info("[TruckServiceImpl - checkPlate] Mã điểm thu không tồn tại: %s", checkPlateReqDto.getBinId());
            throw ApiException.ErrNotFound().message(String.format("bin_code %s (mã điểm thu) không tồn tại", checkPlateReqDto.getBinId())).build();
        }

        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
            byte[] imageData = Files.readAllBytes(convFile.toPath());

            KafkaEventCheckPlate kafkaEventCheckPlate =  KafkaEventCheckPlate.builder().
                    checks(checks).
                    image(imageData).
                    binId(bin.getId()).
                    company(bin.getCompany()).
                    lat(bin.getLat()).
                    lon(bin.getLon()).
                    timestamp(TimeUtils.getCurrentTimestampMs()).
                    build();
            kafkaProducerService.publishEventCheckPlate(kafkaEventCheckPlate);

        } catch (Exception e) {
            throw ApiException.ErrFileIOException().build();
        }
    }
}
