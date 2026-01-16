package org.devindie.patientservice.mapper;

import jakarta.validation.Valid;
import org.devindie.patientservice.dto.PatientRequestDTO;
import org.devindie.patientservice.dto.PatientResponseDTO;
import org.devindie.patientservice.model.Patient;

public class PatientMapper {

    public static PatientResponseDTO toDTO(Patient patient) {
        PatientResponseDTO patientDto = new PatientResponseDTO();
        patientDto.setId(patient.getId().toString());
        patientDto.setName(patient.getName());
        patientDto.setEmail(patient.getEmail());
        patientDto.setAddress(patient.getAddress());
        patientDto.setDateOfBirth(patient.getDateOfBirth().toString());
        return patientDto;
    }

    public static Patient toModel(PatientRequestDTO patientRequestDTO) {
        Patient patient = new Patient();
        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(java.time.LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        patient.setRegisteredDate(java.time.LocalDate.parse(patientRequestDTO.getRegisteredDate()));
        return patient;
    }
}
