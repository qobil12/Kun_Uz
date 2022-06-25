package com.company.service;

import com.company.dto.AuthDTO;
import com.company.dto.ProfileDTO;
import com.company.dto.RegistrationDTO;
import com.company.dto.VerificationDTO;
import com.company.entity.AttachEntity;
import com.company.entity.ProfileEntity;
import com.company.entity.SmsEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.exps.BadRequestException;
import com.company.repository.ProfileRepository;
import com.company.repository.SmsRepository;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
//    @Autowired
//    private SmsService smsService;
    @Autowired
    private SmsRepository smsRepository;
    @Autowired
    private AttachService attachService;
    @Autowired
    private MailService mailService;

    public ProfileDTO login(AuthDTO authDTO) {
        Optional<ProfileEntity> optional = profileRepository.findByEmail(authDTO.getEmail());
        if (optional.isEmpty()) {
            throw new BadRequestException("User not found");
        }
        ProfileEntity profile = optional.get();
        if (!profile.getPassword().equals(authDTO.getPassword())) {
            throw new BadRequestException("User not found");
        }

        if (!profile.getStatus().equals(ProfileStatus.ACTIVE)) {
            throw new BadRequestException("No ruxsat");
        }

        ProfileDTO dto = new ProfileDTO();
        dto.setName(profile.getName());
        dto.setSurName(profile.getSurname());
        dto.setJwt(JwtUtil.encode(profile.getId(), profile.getRole()));

        return dto;
    }

    public String registration(RegistrationDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isPresent()) {
            throw new BadRequestException("User already exists");
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setStatus(ProfileStatus.ACTIVE);
        entity.setRole(ProfileRole.USER);
        entity.setPhone(dto.getPhone());
        AttachEntity attachEntity = attachService.get(dto.getPhotoId());
        entity.setPhoto(attachEntity);

        profileRepository.save(entity);



        // smsService.sendRegistrationSms(dto.getPhone());

//        ProfileDTO responseDTO = new ProfileDTO();
//        responseDTO.setName(dto.getName());
//        responseDTO.setSurName(dto.getSurname());
//        responseDTO.setEmail(dto.getEmail());
//        responseDTO.setJwt(JwtUtil.encode(entity.getId(), entity.getRole()));
//
        mailService.sendRegistrationEmail(entity.getEmail(), entity.getId());

        return "sms was send";
    }

    public String verification(VerificationDTO dto) {
        Optional<SmsEntity> optional = smsRepository.findTopByPhoneOrderByCreatedDateDesc(dto.getPhone());
        if (optional.isEmpty()) {
            return "Phone Not Found";
        }

        SmsEntity sms = optional.get();
        LocalDateTime validDate = sms.getCreatedDate().plusMinutes(1);

        if (!sms.getCode().equals(dto.getCode())) {
            return "Code Invalid";
        }
        if (validDate.isBefore(LocalDateTime.now())) {
            return "Time is out";
        }

        profileRepository.updateStatusByPhone(dto.getPhone(), ProfileStatus.ACTIVE);
        return "Verification Done";
    }

    public String emailVerification(Integer id) {
        Optional<ProfileEntity> optional = profileRepository.findById(id);
        if (optional.isEmpty()) {
            return "<h1>User Not Found</h1>";
        }

        ProfileEntity profile = optional.get();
        profile.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(profile);
        return "<h1 style='align-text:center'>Success. Tabriklaymiz.</h1>";
    }


}

