package ptit.gms.service.impl;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ptit.gms.constant.Constant;
import ptit.gms.dto.request.CreateBinReqDto;
import ptit.gms.dto.request.UpdateBinReqDto;
import ptit.gms.dto.response.*;
import ptit.gms.exception.ApiException;
import ptit.gms.service.BinService;
import ptit.gms.store.mysql.entity.BinEntity;
import ptit.gms.store.mysql.entity.CompanyOwnerEntity;
import ptit.gms.store.mysql.entity.TotalCellEntity;
import ptit.gms.store.mysql.repository.BinRepository;
import ptit.gms.store.mysql.repository.CompanyOwnerRepository;
import ptit.gms.store.mysql.repository.TotalCellRepository;
import ptit.gms.utils.GenerateUtils;
import ptit.gms.utils.S3UploadUtils;
import ptit.gms.utils.TimeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BinServiceImpl implements BinService {
    @Autowired
    BinRepository binRepository;

    @Autowired
    CompanyOwnerRepository companyOwnerRepository;

    @Autowired
    TotalCellRepository totalCellRepository;

    @Autowired
    S3UploadUtils s3UploadUtils;

    @Override
    @Transactional
    public CreateBinResDto createBin(CreateBinReqDto createBinReqDto, MultipartFile file) {
        log.info("[BinServiceImpl - createBin] createBinReqDto: {}", createBinReqDto);

        if(!Constant.X_USER_ROLE.equals(Constant.ADMIN_ROLE_TYPE) && !Constant.X_USER_ROLE.equals(Constant.USER_ROLE_TYPE))
            throw ApiException.ErrUnauthorized().build();


        CompanyOwnerEntity ownerEntity = CompanyOwnerEntity.builder().
                id(GenerateUtils.generateRandomUUID()).
                name(createBinReqDto.getOwnerName()).
                phone(createBinReqDto.getOwnerPhone()).
                email(createBinReqDto.getOwnerEmail()).
                build();

        companyOwnerRepository.save(ownerEntity);

        String imageUrl = "";
        if (file != null && !file.isEmpty()){
            try {
                imageUrl = s3UploadUtils.uploadImageFile(Constant.S3_BIN_IMAGE_ENDPOINT, file);
            } catch (IOException e) {
                throw ApiException.ErrFileIOException().build();
            }
        }

        BinEntity binEntity = BinEntity.builder().
                id(GenerateUtils.generateRandomBinCode()).
                company(createBinReqDto.getCompany()).
                lat(createBinReqDto.getLat()).
                lon(createBinReqDto.getLon()).
                ownerId(ownerEntity.getId()).
                capacity(createBinReqDto.getCapacity()).
                createdUser(Constant.X_USER_ID). // get created user id from header ...
                address(createBinReqDto.getAddress()).
                imageUrl(imageUrl).
                status(Constant.STATUS_BIN_ACTIVE).
                build();

        binRepository.save(binEntity);

        TotalCellEntity totalCellEntity = TotalCellEntity.builder().
                binCode(binEntity.getId()).
                weight(0).
                updatedTimestamp(TimeUtils.getCurrentTimestampMs()).
                build();

        totalCellRepository.save(totalCellEntity);

        CreateBinResDto createBinResDto = CreateBinResDto.builder().
                id(binEntity.getId()).
                company(binEntity.getCompany()).
                address(binEntity.getAddress()).
                lat(binEntity.getLat()).
                lon(binEntity.getLon()).
                capacity(binEntity.getCapacity()).
                imageUrl(binEntity.getImageUrl()).
                owner(CompanyOwnerResDto.builder().
                        id(ownerEntity.getId()).
                        name(ownerEntity.getName()).
                        phone(ownerEntity.getPhone()).
                        email(ownerEntity.getEmail()).
                        build()).
                createdUser(binEntity.getCreatedUser()).
                build();

        return createBinResDto;
    }

    @Override
    public GetBinResDto getBin(String id) {
        log.info("[BinServiceImpl - getBin] id: {}", id);

        GetBinResDto bin = binRepository.getBinById(id);
        if(!Constant.X_USER_ROLE.equals(Constant.ADMIN_ROLE_TYPE)){
            if(!Constant.X_USER_ID.equals(bin.getCreatedUser()))
                throw ApiException.ErrUnauthorized().build();
        }

        if(bin == null){
            throw ApiException.ErrNotFound().message(String.format("bin_code %s (mã điểm thu) không tồn tại", id)).build();
        }

        return bin;
    }

    @Override
    public List<GetBinResDto> listBins(){
        log.info("[BinServiceImpl - listBins]");

        List<GetBinResDto> bins = new ArrayList<>();

        // check role, if user: get bin belong to this user, if admin return all
        if (Constant.X_USER_ROLE.equals(Constant.ADMIN_ROLE_TYPE)){
            bins = binRepository.listBins();
        }else if(Constant.X_USER_ROLE.equals(Constant.USER_ROLE_TYPE)){
            bins = binRepository.listBinsByCreatedUser(Constant.X_USER_ID);
        } else {
            throw ApiException.ErrUnauthorized().build();
        }
        return bins;
    }

    @Override
    public PaginationResDto<GetBinResDto> listBinsPagination(int page, int size, String sortBy) {
        log.info("[BinServiceImpl - listBinsPagination]");
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
            Page<GetBinResDto> pageBins = binRepository.listBinsPagination(paginationOption);
            return PaginationResDto.<GetBinResDto>builder().totals(pageBins.getTotalElements()).
                    pages(pageBins.getTotalPages()).
                    page(page + 1).
                    size(size).
                    results(pageBins.getContent()).
                    build();
        }else if(Constant.X_USER_ROLE.equals(Constant.USER_ROLE_TYPE)){
            Page<GetBinResDto> pageBins = binRepository.listBinsByCreatedUserPagination(Constant.X_USER_ID, paginationOption);
            return PaginationResDto.<GetBinResDto>builder().totals(pageBins.getTotalElements()).
                    pages(pageBins.getTotalPages()).
                    page(page + 1).
                    size(size).
                    results(pageBins.getContent()).
                    build();
        } else {
            throw ApiException.ErrUnauthorized().build();
        }
    }

    @Override
    public void deleteBin(String id){
        log.info("[BinServiceImpl - deleteBin] id: {}", id);
        BinEntity bin = binRepository.findByStatusAndId(Constant.STATUS_BIN_ACTIVE, id);
        if(!Constant.X_USER_ROLE.equals(Constant.ADMIN_ROLE_TYPE)){
            if(!Constant.X_USER_ID.equals(bin.getCreatedUser()))
                throw ApiException.ErrUnauthorized().build();
        }

        if(bin == null){
            throw ApiException.ErrNotFound().message(String.format("bin_code %s (mã điểm thu) không tồn tại", id)).build();
        }

        bin.setStatus(Constant.STATUS_BIN_INACTIVE);
        binRepository.save(bin);
    }

    @Override
    @Transactional
    public UpdateBinResDto updateBin(String id, UpdateBinReqDto updateBinReqDto, MultipartFile file) {
        log.info("[BinServiceImpl - updateBin] id: {}, updateBinResDto: {}", id, updateBinReqDto);

        BinEntity bin = binRepository.findByStatusAndId(Constant.STATUS_BIN_ACTIVE, id);
        if(!Constant.X_USER_ROLE.equals(Constant.ADMIN_ROLE_TYPE)){
            if(!Constant.X_USER_ID.equals(bin.getCreatedUser()))
                throw ApiException.ErrUnauthorized().build();
        }

        if(bin == null){
            throw ApiException.ErrNotFound().message(String.format("bin_code %s (mã điểm thu) không tồn tại", id)).build();
        }

        String imageUrl = "";
        if (file != null && !file.isEmpty()){
            try {
                imageUrl = s3UploadUtils.uploadImageFile(Constant.S3_BIN_IMAGE_ENDPOINT, file);
            } catch (IOException e) {
                throw ApiException.ErrFileIOException().build();
            }
        }
        if(!imageUrl.equals(""))
            bin.setImageUrl(imageUrl);

        if(updateBinReqDto.getCompany() != null)
            bin.setCompany(updateBinReqDto.getCompany());

        if(updateBinReqDto.getLat() != null)
            bin.setLat(updateBinReqDto.getLat());

        if(updateBinReqDto.getLon() != null)
            bin.setLon(updateBinReqDto.getLon());

        if(updateBinReqDto.getCapacity() != null)
            bin.setCapacity(updateBinReqDto.getCapacity());

        if(updateBinReqDto.getAddress() != null)
            bin.setAddress(updateBinReqDto.getAddress());

        binRepository.save(bin);

        Optional<CompanyOwnerEntity> owner = companyOwnerRepository.findById(bin.getOwnerId());
        if(owner.isEmpty()){
            throw ApiException.ErrDataLoss().build();
        }
        CompanyOwnerEntity ownerEntity = owner.get();

        if(updateBinReqDto.getOwnerName() != null)
            ownerEntity.setName(updateBinReqDto.getOwnerName());

        if(updateBinReqDto.getOwnerPhone() != null)
            ownerEntity.setPhone(updateBinReqDto.getOwnerPhone());

        if(updateBinReqDto.getOwnerEmail() != null)
            ownerEntity.setEmail(updateBinReqDto.getOwnerEmail());

        companyOwnerRepository.save(ownerEntity);

        UpdateBinResDto updateBinResDto = UpdateBinResDto.builder().
                id(bin.getId()).
                company(bin.getCompany()).
                address(bin.getAddress()).
                lat(bin.getLat()).
                lon(bin.getLon()).
                capacity(bin.getCapacity()).
                imageUrl(bin.getImageUrl()).
                owner(CompanyOwnerResDto.builder().
                        id(ownerEntity.getId()).
                        name(ownerEntity.getName()).
                        phone(ownerEntity.getPhone()).
                        email(ownerEntity.getEmail()).
                        build()).
                build();

        return updateBinResDto;
    }
}
