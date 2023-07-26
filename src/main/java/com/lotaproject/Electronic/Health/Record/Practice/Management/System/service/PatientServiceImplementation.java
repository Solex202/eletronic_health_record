package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.RegisterPatientRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.UpdatePatientDetailRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.PaginatedPatientResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.*;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.PatientRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.email.EmailSender;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.CannotRegisterPatientException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.ElectronicHealthException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.PatientDoesNotexistException;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.ExceptionMessages.*;

@Service
@AllArgsConstructor
@Slf4j
public class PatientServiceImplementation implements PatientService{
    private final MedicalHistoryService medicalHistoryService;
    @Autowired
    private  EmailSender emailSender;
    @Autowired
    Configuration configuration;
    private final BCryptPasswordEncoder encoder;
    @Autowired
    private  PatientRepository patientRepository;
    @Override
    public ApiResponse<?> registerPatient(RegisterPatientRequest request) throws IOException, TemplateException {
        var patient = new Patient();

        var builder = new StringBuilder();

        String patientIdentity = RandomString.make(7);
        Set<Role> role = new HashSet<>();
        role.add(Role.PATIENT);

        String encodedPassword = encoder.encode(request.getPassword());

        registerPatientValidation(request);

        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setMedication(request.getMedicalHistory().getMedication());
        medicalHistory.setAllergy(request.getMedicalHistory().getAllergy());
        medicalHistory.setAttachment(request.getMedicalHistory().getAttachment());
        medicalHistory.setAilment(request.getMedicalHistory().getAilment());
        medicalHistory.setCreatedDate(LocalDateTime.now());
        medicalHistory.setModifiedDate(LocalDateTime.now());

        medicalHistoryService.createMedicalHistory(medicalHistory);

        patient.setMedicalHistory(medicalHistory);
        patient.setRoles(role);
        patient.setPatientId(patientIdentity);
        patient.setAddress(request.getAddress());
        patient.setEmail(request.getEmail());
        patient.setPassword(encodedPassword);
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setPhoneNumber(request.getPhoneNumber());
        patient.setOccupation(request.getOccupation());
        patient.setGender(Gender.valueOf(request.getGender()));
        patient.setBloodGroup(BloodGroup.valueOf(request.getBloodGroup()));
        patient.setGenotype(Genotype.valueOf(request.getGenotype()));
        patient.setDob(LocalDate.parse(request.getDob()));
        patient.setGuardian(request.getGuardian());
        patient.setGuardianPhoneNumber(request.getGuardianPhoneNumber());
        patient.setRegisteredDate(LocalDateTime.now());
        patient.setModifiedDate(LocalDateTime.now());

        var savedPatient = patientRepository.save(patient);
        Map<String, Object> map = new HashMap<>();
        builder.append(FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate("patientservice.ftlh"), map)
        );

        emailSender.send(request.getEmail(), buildEmail(request.getFirstName()));

        return ApiResponse.builder().message("Registration Successfully").data(savedPatient).build();

    }
    private void registerPatientValidation(RegisterPatientRequest request) {
        StringBuilder builder = new StringBuilder();
        if(!emailIsValid(request.getEmail())) builder.append(EMAIL_IS_INVALID.getMessage()).append("\n");

        if(patientRepository.existsByEmail(request.getEmail())) builder.append(EMAIL_ALREADY_EXCEPTION.getMessage()).append("\n");
        if(!passwordIsValid(request.getPassword())) builder.append(INVALID_PASSWORD.getMessage());

        if(!builder.isEmpty()) throw new ElectronicHealthException(builder.toString());
    }
    @Override
    public Patient findByEmail(String email) {
        return patientRepository.findByEmail(email).orElseThrow(()-> new PatientDoesNotexistException(String.format(PATIENT_WITH_EMAIL_DOESNOT_EXIST.getMessage(), email)));
    }
    @Override
    public Patient findById(String id) {
        return patientRepository.findById(id).orElseThrow(()-> new PatientDoesNotexistException(String.format(PATIENT_WITH_ID_DOESNOT_EXIST.getMessage(), id)));
    }
    @Override
    public ApiResponse<?> updatePatientDetails(String id, UpdatePatientDetailRequest request1) {
        var patient = findById(id);

        updateEmailValidation(request1.getEmail(), patient);

        if(request1.getEmail() != null) patient.setEmail(request1.getEmail());
        if(request1.getFirstName() != null) patient.setFirstName(request1.getFirstName());
        if(request1.getDob() != null) patient.setDob(LocalDate.parse(request1.getDob()));
        if(request1.getLastName() != null) patient.setLastName(request1.getLastName());
        if(request1.getOccupation() != null) patient.setOccupation(request1.getOccupation());

        var newPatient = patientRepository.save(patient);
        return ApiResponse.builder().message(UPDATE_MESSAGE.getMessage()).data(newPatient).build();
    }
    @Override
    public PaginatedPatientResponse findByName(int pageNumber, int pageSize,String name) {
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "registeredDate");
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(order));

        Page<Patient> listOfPatient = patientRepository.findByFirstNameContainingOrLastNameContaining(name, name,pageable);
        return PaginatedPatientResponse.builder()
                .patients(listOfPatient.toList())
                .noOfPatients(listOfPatient.getContent().size())
                .currentPage(pageNumber)
                .pageSize(pageSize)
                .build();
    }

    private void updateEmailValidation(String email, Patient patient) {
        if(!emailIsValid(email)) throw new CannotRegisterPatientException(EMAIL_IS_INVALID.getMessage());

        if(patientRepository.existsByEmail(email)){
            if(Objects.equals(email, patient.getEmail())){
                patient.setEmail(email);
            }else throw new CannotRegisterPatientException(EMAIL_ALREADY_EXCEPTION.getMessage());
        }
    }
    private boolean emailIsValid(String email) {

        String regex = "[a-zA-z][\\w-]{1,20}@\\w{2,20}\\.\\w{2,3}$";
        Pattern pattern  = Pattern.compile(regex);
        Matcher matcher =  pattern.matcher(email);

        return matcher.matches();
    }

    private boolean passwordIsValid(String password) {
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$";
        Pattern pattern  = Pattern.compile(regex);
        Matcher matcher =  pattern.matcher(password);

        return matcher.matches();
    }


    private String buildEmail(String name) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}
