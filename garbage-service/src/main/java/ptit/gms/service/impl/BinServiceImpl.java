package ptit.gms.service.impl;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ptit.gms.constant.Constant;
import ptit.gms.dto.request.CreateBinReqDto;
import ptit.gms.dto.request.UpdateBinReqDto;
import ptit.gms.dto.response.CompanyOwnerResDto;
import ptit.gms.dto.response.CreateBinResDto;
import ptit.gms.dto.response.GetBinResDto;
import ptit.gms.dto.response.UpdateBinResDto;
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
                createdUser(GenerateUtils.generateRandomUUID()). // get created user id from header later ...
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
        if(bin == null){
            throw ApiException.ErrNotFound().message(String.format("bin_code %s (mã điểm thu) không tồn tại", id)).build();
        }

        return bin;
    }

    @Override
    public List<GetBinResDto> listBins(){
        log.info("[BinServiceImpl - listBins]");

        String role = "ADMIN";
        List<GetBinResDto> bins = new ArrayList<>();

        // check role, if user: get bin belong to this user, if admin return all
        if (role.equals("ADMIN")){
            bins = binRepository.listBins();
        }else if(role.equals("USER")){
            bins = binRepository.listBinsByCreatedUser("id-of-user");
        }
        return bins;
    }

    @Override
    public void deleteBin(String id){
        log.info("[BinServiceImpl - deleteBin] id: {}", id);
        Optional<BinEntity> bin = binRepository.findById(id);
        if(bin.isEmpty()){
            throw ApiException.ErrNotFound().message(String.format("bin_code %s (mã điểm thu) không tồn tại", id)).build();
        }

        BinEntity binEntity = bin.get();
        binEntity.setStatus(Constant.STATUS_BIN_INACTIVE);
        binRepository.save(binEntity);
    }

    @Override
    @Transactional
    public UpdateBinResDto updateBin(String id, UpdateBinReqDto updateBinReqDto, MultipartFile file) {
        log.info("[BinServiceImpl - updateBin] id: {}, updateBinResDto: {}", id, updateBinReqDto);

        Optional<BinEntity> bin = binRepository.findById(id);
        if(bin.isEmpty()){
            throw ApiException.ErrNotFound().message(String.format("bin_code %s (mã điểm thu) không tồn tại", id)).build();
        }

        BinEntity binEntity = bin.get();
        if(binEntity.getStatus() != Constant.STATUS_BIN_ACTIVE){
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
            binEntity.setImageUrl(imageUrl);

        if(updateBinReqDto.getCompany() != null)
            binEntity.setCompany(updateBinReqDto.getCompany());

        if(updateBinReqDto.getLat() != null)
            binEntity.setLat(updateBinReqDto.getLat());

        if(updateBinReqDto.getLon() != null)
            binEntity.setLon(updateBinReqDto.getLon());

        if(updateBinReqDto.getCapacity() != null)
            binEntity.setCapacity(updateBinReqDto.getCapacity());

        if(updateBinReqDto.getAddress() != null)
            binEntity.setAddress(updateBinReqDto.getAddress());

        binRepository.save(binEntity);

        Optional<CompanyOwnerEntity> owner = companyOwnerRepository.findById(binEntity.getOwnerId());
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

        return updateBinResDto;
    }
}
