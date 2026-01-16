package org.devindie.patientservice.service;

import org.devindie.patientservice.dto.PatientRequestDTO;
import org.devindie.patientservice.dto.PatientResponseDTO;
import org.devindie.patientservice.exception.EmailAlreadyExistsException;
import org.devindie.patientservice.exception.PatientNotFoundException;
import org.devindie.patientservice.grpc.BillingServiceGrpcClient;
import org.devindie.patientservice.mapper.PatientMapper;
import org.devindie.patientservice.model.Patient;
import org.devindie.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final org.devindie.patientservice.kafka.KafkaProducer kafkaProducer;

    public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient, org.devindie.patientservice.kafka.KafkaProducer kafkaProducer) {
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.patientRepository = patientRepository;
        this.kafkaProducer = kafkaProducer;
    }

    public List<PatientResponseDTO> getAllPatients(){
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientMapper::toDTO).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())){
            throw new EmailAlreadyExistsException("Email already exists: " + patientRequestDTO.getEmail());
        }
        Patient patient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));

        billingServiceGrpcClient.createBillingAccount(patient.getId().toString(), patient.getName(), patient.getEmail());

        kafkaProducer.sendEvent(patient, "PATIENT_CREATED");

        return PatientMapper.toDTO(patient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO){
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));
        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)){
            throw new EmailAlreadyExistsException("Patient with this email address already exists: " + patientRequestDTO.getEmail());
        }
        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(java.time.LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        Patient updatedPatient = patientRepository.save(patient);

        kafkaProducer.sendEvent(patient, "PATIENT_UPDATED");

        return PatientMapper.toDTO(updatedPatient);

    }

    public void deletePatient(UUID id){
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));
        patientRepository.delete(patient);
    }



}
