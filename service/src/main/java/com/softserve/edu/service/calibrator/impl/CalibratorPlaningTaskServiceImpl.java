package com.softserve.edu.service.calibrator.impl;

import com.softserve.edu.entity.device.CalibrationModule;
import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.verification.calibration.CalibrationTask;
import com.softserve.edu.repository.*;
import com.softserve.edu.service.calibrator.CalibratorPlanningTaskService;
import org.apache.log4j.Logger;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Service
@Transactional(readOnly = true)
public class CalibratorPlaningTaskServiceImpl implements CalibratorPlanningTaskService {


    @Autowired
    private CalibrationPlanningTaskRepository taskRepository;

    @Autowired
    private VerificationRepository verificationRepository;

    @Autowired
    private VerificationPlanningTaskRepository planningTaskRepository;

    @Autowired
    private UserRepository userRepository;

    private Logger logger = Logger.getLogger(CalibratorPlaningTaskServiceImpl.class);

    @Autowired
    private CalibrationModuleRepository moduleRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private AdditionalInfoRepository additionalInfoRepository;



    /**
     * This method save new task for the station. It checks if counter
     * statuses for the verifications are the same, if not
     * @throws IllegalArgumentException(). Also it checks if station
     * device type is is as device type of the verification, if not
     * method @throws IllegalArgumentException().
     *
     * @param taskDate
     * @param serialNumber
     * @param verificationsId
     * @param userId
     */
    @Override
    public void addNewTask(Date taskDate, String serialNumber, List<String> verificationsId, Long organizationId) {
        Set<Verification> verifications = new HashSet<>();
        for (String verifID : verificationsId) {
            Verification verification = verificationRepository.findOne(verifID);
            if (verification == null) {
                logger.error("verification haven't found");
            } else {
                verification.setTaskStatus(Status.TASK_PLANED);
                verificationRepository.save(verification);
                verifications.add(verification);
            }
        }

        CalibrationModule module = moduleRepository.findBySerialNumber(serialNumber);
        module.setWorkDate(taskDate);
        moduleRepository.save(module);
        Organization organization = organizationRepository.findOne(organizationId);
        CalibrationTask task = new CalibrationTask(module, null, new Date(), taskDate, organization, verifications);
        taskRepository.save(task);

    }

    /**
     * This method find count for the verifications with status
     * planning task which assigned for the calibrator employee.
     * If employee has role admin it return count of all verifications with status planning
     * task which related to the calibrator organization
     *
     * @param userName
     * @return count of verifications (int)
     */
    @Override
    public int findVerificationsByCalibratorEmployeeAndTaskStatusCount(String userName) {
        User user  = userRepository.findOne(userName);
        if (user == null){
            logger.error("Cannot found user!");
        }
        List<Verification> verifications = planningTaskRepository.findByCalibratorEmployeeUsernameAndTaskStatus(user.getUsername(), Status.PLANNING_TASK);
        return verifications.size();
    }

    /**
     * This method returns page of verifications with
     * status planning task filtered by calibrator id,
     * when calibrator is admin
     * and sorted by client address
     *
     * @param id
     * @param pageNumber
     * @param itemsPerPage
     * @return Page<Verification>
     */
    @Override
    public Page<Verification> findByTaskStatus(int pageNumber, int itemsPerPage) {
        Pageable pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.ASC,
                "clientData.clientAddress.district", "clientData.clientAddress.street", "clientData.clientAddress.building", "clientData.clientAddress.flat"));
        return planningTaskRepository.findByTaskStatus(Status.PLANNING_TASK, pageRequest);
    }


    /**
     *  This method returns page of verifications with
     *  status planning task filtered by calibrator id,
     *  and sorted by client address. If user has role
     *  admin it calls method findByTaskStatusAndCalibratorId()
     *
     * @param userName
     * @param pageNumber
     * @param itemsPerPage
     * @return Page<Verification>
     * @throws NullPointerException();
     */
    @Override
    public Page<Verification> findVerificationsByCalibratorEmployeeAndTaskStatus(String userName, int pageNumber, int itemsPerPage) {
        User user  = userRepository.findOne(userName);
        if (user == null){
            logger.error("Cannot found user!");
        }
        Set<UserRole> roles = user.getUserRoles();
        for (UserRole role : roles) {
            if (role.equals(UserRole.CALIBRATOR_ADMIN)) {
                return findByTaskStatus(pageNumber, itemsPerPage);
            }
        }
        Pageable pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.ASC,
                "clientData.clientAddress.district", "clientData.clientAddress.street", "clientData.clientAddress.building", "clientData.clientAddress.flat"));
        return planningTaskRepository.findByCalibratorEmployeeUsernameAndTaskStatus(user.getUsername(), Status.PLANNING_TASK, pageRequest);
    }

<<<<<<< HEAD
    /**
     * This method returns list of counter types
     * which has the same device id with the verification device id
     *
     * @param verifId
     * @return List<CounterType>
     * @throws NullPointerException();
     */
    @Override
    public List<CounterType> findSymbolsAndSizes(String verifId) {
        Verification verification = verificationRepository.findOne(verifId);
        if (verification == null){
            logger.error("Cannot found verification!");
            throw new NullPointerException();
        }
        List<CounterType> counterTypes = counterTypeRepository.findByDeviceId(verification.getDevice().getId());
        if (counterTypes == null){
            logger.error("Cannot found counter types for verification!");
            throw new NullPointerException();
        }
        return counterTypes;
    }
=======
//    @Override
//    public String createExcelFileFromVerifications(String[]verificationsId) throws IOException {
//            List<ExcelFileDTO> fileDTOs = new ArrayList<>();
//            for (String id : verificationsId) {
//                Verification verification = verificationRepository.findOne(id);
//                AdditionalInfo additionalInfo;
//                if(verification.isAddInfoExists()){
//                    additionalInfo = additionalInfoRepository.findAdditionalInfoByVerificationId(id);
//                    fileDTOs.add(new ExcelFileDTO(verification.getId(), verification.getClientData().getLastName(),
//                            verification.getClientData().getFirstName(), verification.getClientData().getLastName(),
//                            verification.getClientData().getClientAddress().getAddress(), verification.getClientData().getPhone(),
//                            additionalInfo.getEntrance(), additionalInfo.getDoorCode(), additionalInfo.getFloor()));
//                } else {
//                    fileDTOs.add(new ExcelFileDTO(verification.getId(), verification.getClientData().getLastName(),
//                            verification.getClientData().getFirstName(), verification.getClientData().getLastName(),
//                            verification.getClientData().getClientAddress().getAddress(), verification.getClientData().getPhone(),
//                            0, 0, 0));
//                }
//            }
//            Date date = new Date();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            String filename = "Bolunov"+ dateFormat.format(date).toString()+"-"+ date.getTime() + ".xls" ;
//            HSSFWorkbook workbook = new HSSFWorkbook();
//            HSSFSheet sheet = workbook.createSheet("FirstSheet");
//
//            HSSFRow rowhead = sheet.createRow((short) 0);
//            rowhead.createCell(0).setCellValue("Номер заявки");
//            rowhead.createCell(1).setCellValue("Прізвище кліента");
//            rowhead.createCell(2).setCellValue("Ім'я кліента");
//            rowhead.createCell(3).setCellValue("По батькові");
//            rowhead.createCell(4).setCellValue("Адресса");
//            rowhead.createCell(5).setCellValue("Телефон");
//            rowhead.createCell(6).setCellValue("Під'їзд");
//            rowhead.createCell(7).setCellValue("Код на дверях");
//            rowhead.createCell(8).setCellValue("Поверх");
//
//            int i = 0;
//            for (ExcelFileDTO dto: fileDTOs) {
//                i++;
//                HSSFRow row = sheet.createRow((short)i);
//                row.createCell(0).setCellValue(dto.getVerificationId());
//                row.createCell(1).setCellValue(dto.getSurname());
//                row.createCell(2).setCellValue(dto.getName());
//                row.createCell(3).setCellValue(dto.getMiddelName());
//                row.createCell(4).setCellValue(dto.getAdress());
//                row.createCell(5).setCellValue(dto.getTelephone());
//                row.createCell(6).setCellValue(dto.getEntrance());
//                row.createCell(7).setCellValue(dto.getDoorCode());
//                row.createCell(8).setCellValue(dto.getFloor());
//            }
//            FileOutputStream fileOut = new FileOutputStream("./"+filename);
//            workbook.write(fileOut);
//            fileOut.close();
//            System.out.println("Your excel file has been generated!");
//
//            return filename;
//
//    }
>>>>>>> origin/pavlo.berezin
}
