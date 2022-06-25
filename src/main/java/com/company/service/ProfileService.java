package com.company.service;

import com.company.dto.ProfileDTO;
import com.company.entity.AttachEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.exps.*;
import com.company.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private AttachService attachService;

    public ProfileDTO create(ProfileDTO profileDto) {

        Optional<ProfileEntity> entity = profileRepository.findByEmail(profileDto.getEmail());
        if (entity.isPresent()) {
            throw new AlreadyExistPhone("Already exist phone");
        }

        if(!profileDto.getEmail().endsWith("@gmail.com")){
            throw new BadRequestException("Email or Password is wrong");
        }

        isValid(profileDto);

        ProfileEntity profile = new ProfileEntity();
        profile.setName(profileDto.getName());
        profile.setSurname(profileDto.getSurName());
        profile.setEmail(profileDto.getEmail());
        profile.setRole(ProfileRole.USER);
        profile.setPassword(profileDto.getPassword());
        profile.setStatus(ProfileStatus.ACTIVE);
        AttachEntity attachEntity = attachService.get(profileDto.getImageId());
        profile.setPhoto(attachEntity);

        profileRepository.save(profile);

        ProfileDTO responseDTO = new ProfileDTO();
        responseDTO.setId(profile.getId());
        responseDTO.setName(profileDto.getName());
        responseDTO.setSurName(profileDto.getSurName());
        responseDTO.setEmail(profileDto.getEmail());
        responseDTO.setRole(ProfileRole.USER);
        responseDTO.setPassword(profileDto.getPassword());
        responseDTO.setStatus(ProfileStatus.ACTIVE);
        responseDTO.setPassword(null);

        return responseDTO;
    }

    public List<ProfileDTO> getList() {
        Iterable<ProfileEntity> all = profileRepository.findAllByVisible(true);
        List<ProfileDTO> dtoList = new LinkedList<>();
        all.forEach(profileEntity -> {
            ProfileDTO dto = new ProfileDTO();
            dto.setId(profileEntity.getId());
            dto.setName(profileEntity.getName());
            dto.setSurName(profileEntity.getSurname());
            dto.setEmail(profileEntity.getEmail());
            dtoList.add(dto);
        });
        return dtoList;
    }

    public void update(Integer pId, ProfileDTO dto) {

        Optional<ProfileEntity> profile = profileRepository.findById(pId);

        if (profile.isEmpty()) {
            throw new ItemNotFoundException("Profile Not Found ");
        }

        isValidUpdate(dto);

        ProfileEntity entity = profile.get();

        entity.setName(dto.getName());
        entity.setSurname(dto.getSurName());
        entity.setEmail(dto.getEmail());
        profileRepository.save(entity);


    }

    public void updateImageProfile(String id, Integer profileId) {

        AttachEntity attach = attachService.get(id);

        ProfileEntity profile = get(profileId);

        if(!profileRepository.existsByPhotoId(profile.getId(),attach.getId())){
            throw new ItemNotFoundEseption("Profile da bunday id li attach yo'q");
        }

        profileRepository.changeProfileImage(attach.getId(), profile.getId());
        attachService.delete(id);

        ProfileEntity entity = get(profileId);
        AttachEntity attach1 = attachService.get(id);
        entity.setPhoto(attach1);
    }





    public void delete(Integer id) {
        Optional<ProfileEntity> profile = profileRepository.findById(id);
        if (profile.isEmpty()) {
            throw new ItemNotFoundException("not found profile");
        }
        if (!profile.get().getVisible()) {
            throw new AlreadyExist("IsVisible False edi");
        }

        profile.get().setVisible(Boolean.FALSE);
        profileRepository.save(profile.get());
    }

    private void isValidUpdate(ProfileDTO dto) {

        if (dto.getName() == null || dto.getName().length() < 3) {
            throw new BadRequestException("wrong name");
        }

        if (dto.getSurName() == null || dto.getSurName().length() < 4) {
            throw new BadRequestException("surname required.");
        }

        if (dto.getEmail() == null || dto.getEmail().length() < 3) {
            throw new BadRequestException("email required.");
        }


    }

    public ProfileEntity get(Integer id) {
        ProfileEntity profileEntity = profileRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException("Profile not found");
        });
        return profileEntity;
    }

    private void isValid(ProfileDTO dto) {

        if (dto.getName() == null || dto.getName().length() < 3) {
            throw new BadRequestException("wrong name");
        }

        if (dto.getSurName() == null || dto.getSurName().length() < 4) {
            throw new BadRequestException("surname required.");
        }

        if (dto.getEmail() == null || dto.getEmail().length() < 3) {
            throw new BadRequestException("email required.");
        }

    }

    public void save(ProfileEntity moderator) {

        Optional<ProfileEntity> byEmail = profileRepository.findByEmail(moderator.getEmail());

        if (!byEmail.isPresent()) {
            profileRepository.save(moderator);
        }

    }

    public PageImpl paginationProfile(int page, int size) {

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ProfileEntity> all = profileRepository.findAll(pageable);

//        List<ProfileEntity> list = all.getContent();

        List<ProfileDTO> dtoList = new LinkedList<>();

        all.getContent().forEach(profileEntity -> {
            ProfileDTO dto = new ProfileDTO();
            dto.setId(profileEntity.getId());
            dto.setName(profileEntity.getName());
            dto.setSurName(profileEntity.getSurname());
            dto.setEmail(profileEntity.getEmail());
            dto.setRole(profileEntity.getRole());
            dtoList.add(dto);
        });

        return new PageImpl(dtoList,pageable, all.getTotalElements());
    }
}

