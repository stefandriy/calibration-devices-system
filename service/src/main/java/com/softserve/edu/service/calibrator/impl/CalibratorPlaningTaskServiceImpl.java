package com.softserve.edu.service.calibrator.impl;

import com.softserve.edu.entity.catalogue.Team.DisassemblyTeam;
import com.softserve.edu.entity.device.CalibrationModule;
import com.softserve.edu.entity.device.CounterType;
import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.verification.calibration.CalibrationTask;
import com.softserve.edu.repository.*;
import com.softserve.edu.service.calibrator.CalibratorPlanningTaskService;
import com.softserve.edu.service.tool.MailService;
import com.softserve.edu.service.tool.impl.MailServiceImpl;
import com.softserve.edu.service.utils.ZipArchiver;
import com.softserve.edu.service.utils.export.DbfTableExporter;
import com.softserve.edu.service.utils.export.XlsTableExporter;
import com.softserve.edu.specification.CalibrationTaskSpecificationBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class CalibratorPlaningTaskServiceImpl implements CalibratorPlanningTaskService {

    @Autowired
    private CalibrationPlanningTaskRepository taskRepository;

    @Autowired
    private VerificationRepository verificationRepository;

    @Autowired
    private CalibrationModuleRepository moduleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalibrationDisassemblyTeamRepository teamRepository;

    @Autowired
    private CounterTypeRepository counterTypeRepository;

    @Autowired
    private MailService mailService;


    private Logger logger = Logger.getLogger(CalibratorPlaningTaskServiceImpl.class);

    /**
     * This method returns filtered and sorted
     * page of calibration tasks
     *
     * @param filterParams filtering parameters
     * @param pageable parameters for pagination and sorting
     * @return filtered and sorted page of calibration tasks
     */
    public Page<CalibrationTask> getFilteredPageOfCalibrationTasks(Map<String, String> filterParams,
                                                                   Pageable pageable, String username) {
        User user = userRepository.findOne(username);
        filterParams.put("organizationCode", user.getOrganization().getAdditionInfoOrganization().getCodeEDRPOU());
        CalibrationTaskSpecificationBuilder specificationBuilder = new CalibrationTaskSpecificationBuilder(filterParams);
        Specification<CalibrationTask> searchSpec = specificationBuilder.buildPredicate();
        return taskRepository.findAll(searchSpec, pageable);
    }

    /**
     * This method fetches all calibration tasks and returns
     * a sorted page of them
     *
     * @param pageable parameters for pagination and sorting
     * @return sorted page of calibration tasks
     */
    public Page<CalibrationTask> findAllCalibrationTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    /**
     * This method saves new task for the station. It checks if counter
     * statuses for the verifications are the same, if not
     *
     * @param taskDate
     * @param moduleNumber
     * @param verificationsId
     * @param userId
     * @throws IllegalArgumentException(). Also it checks if calibration module
     *                                     device type is the same as device type of the verification, if not
     *                                     method @throws IllegalArgumentException().
     */
    @Override
    public void addNewTaskForStation(Date taskDate, String moduleNumber, List<String> verificationsId, String userId) {
        CalibrationModule module = moduleRepository.findByModuleNumber(moduleNumber);
        if (module == null) {
            logger.error("module wasn't found");
            throw new IllegalArgumentException();
        }
        User user = userRepository.findOne(userId);
        if (user == null) {
            logger.error("user wasn't found");
            throw new IllegalArgumentException();
        }
        CalibrationTask task = new CalibrationTask(module, null, new Date(), taskDate, user);
        try {
            // sendTaskToStation(task);
            taskRepository.save(task);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        for (String verifID : verificationsId) {
            Verification verification = verificationRepository.findOne(verifID);
            if (verification == null) {
                logger.error("verification wasn't found");
                throw new IllegalArgumentException();
            } else {
                verification.setTaskStatus(Status.TEST_PLACE_DETERMINED);
                verification.setTask(task);
                verificationRepository.save(verification);
            }
        }
    }

    /**
     * This method save new task for the team. It checks if counter
     * statuses for the verifications are the same, if not
     *
     * @param taskDate
     * @param serialNumber
     * @param verificationsId
     * @param userId
     * @throws IllegalArgumentException(). Also it checks if station
     *                                     device type is is as device type of the verification, if not
     *                                     method @throws IllegalArgumentException().
     */
    @Override
    public void addNewTaskForTeam(Date taskDate, String serialNumber, List<String> verificationsId, String userId) {
        Set<Verification> verifications = new HashSet<>();
        DisassemblyTeam team = teamRepository.findOne(serialNumber);
        team.setEffectiveTo(taskDate);
        int i = 0;
        boolean counterStatus = false;
        for (String verifID : verificationsId) {
            Verification verification = verificationRepository.findOne(verifID);
            if (verification == null) {
                logger.error("verification haven't found");
            } else {
                if (i == 0) {
                    counterStatus = verification.isCounterStatus();
                }
                if (counterStatus == verification.isCounterStatus()) {
                    if (team.getSpecialization() == verification.getDevice().getDeviceType()) {
                        verification.setTaskStatus(Status.TASK_PLANED);
                        verificationRepository.save(verification);
                        verifications.add(verification);
                        i++;
                    } else {
                        logger.error("verification and module has different device types");
                        throw new IllegalArgumentException();
                    }
                } else {
                    logger.error("verifications has different counter status");
                    throw new IllegalArgumentException();
                }
            }
        }
        teamRepository.save(team);
        User user = userRepository.findOne(userId);
        taskRepository.save(new CalibrationTask(null, team, new Date(), taskDate, user, verifications));
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
        User user = userRepository.findOne(userName);
        if (user == null) {
            logger.error("Cannot found user!");
        }
        Set<UserRole> roles = user.getUserRoles();
        for (UserRole role : roles) {
            if (role.equals(UserRole.CALIBRATOR_ADMIN)) {
                return verificationRepository.findByTaskStatusAndCalibratorId(Status.PLANNING_TASK, user.getOrganization().getId()).size();
            }
        }
        return verificationRepository.findByCalibratorEmployeeUsernameAndTaskStatus(user.getUsername(), Status.PLANNING_TASK).size();

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
     * @param sortCriteria
     * @param sortOrder
     * @return Page<Verification>
     */
    @Override
    public Page<Verification> findByTaskStatusAndCalibratorId(Long id, int pageNumber, int itemsPerPage,
                                                              String sortCriteria, String sortOrder) {
        Pageable pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.ASC,
                "clientData.clientAddress.district", "clientData.clientAddress.street", "clientData.clientAddress.building", "clientData.clientAddress.flat"));
        if (sortCriteria.equals("date")) {
            if (sortOrder.equals("asc")) {
                pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.ASC,
                        "sentToCalibratorDate"));
            } else {
                pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.DESC,
                        "sentToCalibratorDate"));
            }
        } else if (sortCriteria.equals("client_last_name")) {
            if (sortOrder.equals("asc")) {
                pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.ASC,
                        "clientData.lastName", "clientData.firstName"));
            } else {
                pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.DESC,
                        "clientData.lastName", "clientData.firstName"));
            }
        } else if (sortCriteria.equals("providerName")) {
            if (sortOrder.equals("asc")) {
                pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.ASC,
                        "provider.name"));
            } else {
                pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.DESC,
                        "provider.name"));
            }
        } else if (sortCriteria.equals("dateOfVerif") || sortCriteria.equals("timeOfVerif")) {
            if (sortOrder.equals("asc")) {
                pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.ASC,
                        "info.dateOfVerif", "info.timeFrom"));
            } else {
                pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.DESC,
                        "info.dateOfVerif", "info.timeFrom"));
            }
        } else if (sortCriteria.equals("noWaterToDate")) {
            if (sortOrder.equals("asc")) {
                pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.ASC,
                        "info.noWaterToDate"));
            } else {
                pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.DESC,
                        "info.noWaterToDate"));
            }
        } else if (sortCriteria.equals("district") || sortCriteria.equals("street") || sortCriteria.equals("building_flat")) {
            if (sortOrder.equals("asc")) {
                pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.ASC,
                        "clientData.clientAddress.district", "clientData.clientAddress.street", "clientData.clientAddress.building", "clientData.clientAddress.flat"));
            } else {
                pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.DESC,
                        "clientData.clientAddress.district", "clientData.clientAddress.street", "clientData.clientAddress.building", "clientData.clientAddress.flat"));
            }
        }
        return verificationRepository.findByTaskStatusAndCalibratorId(Status.PLANNING_TASK, id, pageRequest);
    }


    /**
     * This method returns page of verifications with
     * status planning task filtered by calibrator id,
     * and sorted by client address. If user has role
     * admin it calls method findByTaskStatusAndCalibratorId()
     *
     * @param userName
     * @param pageNumber
     * @param itemsPerPage
     * @param sortCriteria
     * @param sortOrder
     * @return Page<Verification>
     * @throws NullPointerException();
     */
    @Override
    public Page<Verification> findVerificationsByCalibratorEmployeeAndTaskStatus(String userName, int pageNumber,
                                                                                 int itemsPerPage, String sortCriteria,
                                                                                 String sortOrder) {
        User user = userRepository.findOne(userName);
        if (user == null) {
            logger.error("Cannot found user!");
            throw new NullPointerException();
        }
        Set<UserRole> roles = user.getUserRoles();
        for (UserRole role : roles) {
            if (role.equals(UserRole.CALIBRATOR_ADMIN)) {
                return findByTaskStatusAndCalibratorId(user.getOrganization().getId(), pageNumber, itemsPerPage
                        , sortCriteria, sortOrder);
            }
        }
        Pageable pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.ASC,
                "clientData.clientAddress.district", "clientData.clientAddress.street", "clientData.clientAddress.building", "clientData.clientAddress.flat"));
        return verificationRepository.findByCalibratorEmployeeUsernameAndTaskStatus(user.getUsername(), Status.PLANNING_TASK, pageRequest);
    }

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
        if (verification == null) {
            logger.error("Cannot found verification!");
            throw new NullPointerException();
        }
        List<CounterType> counterTypes = counterTypeRepository.findByDeviceId(verification.getDevice().getId());
        if (counterTypes == null) {
            logger.error("Cannot found counter types for verification!");
            throw new NullPointerException();
        }
        return counterTypes;
    }

    public void sendTaskToStation(CalibrationTask calibrationTask) throws Exception {
        Verification[] verifications = calibrationTask
                .getVerifications()
                .toArray(new Verification[calibrationTask.getVerifications().size()]);

        Map<String, List<String>> dataForXls = getDataForXls(calibrationTask, verifications);
        XlsTableExporter xlsTableExporter = new XlsTableExporter();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String filename = dateFormat.format(calibrationTask.getCreateTaskDate()) + "_" +
                calibrationTask.getModule().getModuleNumber() + "_" + String.valueOf((new Date()).getTime());

        File tempFolder = new File("temp");
        tempFolder.setWritable(true);
        tempFolder.setExecutable(true);
        tempFolder.setReadable(true);
        tempFolder.mkdirs();

        File xlsFile = new File(tempFolder.getAbsolutePath() + File.separator + filename + ".xls");
        File dbfFile = new File(tempFolder.getAbsolutePath() + File.separator + filename + ".dbf");
        File zipFile = new File(tempFolder.getAbsolutePath() + File.separator + filename + ".zip");

        BufferedOutputStream xlsStream = null;
        BufferedOutputStream dbfStream = null;
        BufferedOutputStream zipStream = null;
        try {
            xlsStream = new BufferedOutputStream(new FileOutputStream(xlsFile.getName()));
            dbfStream = new BufferedOutputStream(new FileOutputStream(dbfFile.getName()));
            zipStream = new BufferedOutputStream(new FileOutputStream(zipFile.getName()));
        } catch (Exception ex) {
            throw ex;
        }


        try {
            boolean xlsSuccess;
            try {
                xlsTableExporter.export(dataForXls, xlsStream);
                xlsSuccess = true;
            } catch (Exception e) {
                e.printStackTrace();
                xlsSuccess = false;
            }

            Map<String, List<String>> dataForDbf = getDataForDbf(calibrationTask, verifications);
            DbfTableExporter dbfTableExporter = new DbfTableExporter();

            boolean dbfSuccess;
            try {
                dbfTableExporter.export(dataForDbf, dbfStream);
                dbfSuccess = true;
            } catch (Exception e) {
                e.printStackTrace();
                dbfSuccess = false;
            }

            boolean zipSuccess = false;
            if (xlsSuccess && dbfSuccess) {
                try {
                    ZipArchiver zip = new ZipArchiver();
                    List<File> sources = new ArrayList<>();
                    sources.add(xlsFile);
                    sources.add(dbfFile);
                    zip.createZip(zipStream, sources);
                    zipSuccess = true;
                } catch (Exception e) {
                    zipSuccess = false;
                }
            }

            if (xlsSuccess && dbfSuccess && zipSuccess && zipFile != null) {
                String email = calibrationTask.getModule().getEmail();
                mailService.sendMailWithAttachment(email, "Завдання", " ", zipFile);
                // TODO: Can't test sending mails on local machine!
            }
        } finally {
            xlsFile.delete();
            dbfFile.delete();
            zipFile.delete();
        }
    }

    private Map<String, List<String>> getDataForXls(CalibrationTask calibrationTask, Verification[] verifications) {
        Map<String, List<String>> data = new LinkedHashMap<>();

        // region Define lists

        // Дата завдання
        List<String> taskDate = new ArrayList<>();
        // Провайдер
        List<String> provider = new ArrayList<>();
        // Район
        List<String> district = new ArrayList<>();
        // Адреса
        List<String> address = new ArrayList<>();
        // Будинок
        List<String> building = new ArrayList<>();
        // Квартира
        List<String> flat = new ArrayList<>();
        // Під'їзд
        List<String> entrance = new ArrayList<>();
        // Поверх
        List<String> floor = new ArrayList<>();
        // К-ть лічильників
        List<String> countersNumber = new ArrayList<>();
        // ПІБ
        List<String> fullName = new ArrayList<>();
        // Телефон
        List<String> telephone = new ArrayList<>();
        // Бажаний час
        List<String> time = new ArrayList<>();
        // Примітка
        List<String> comment = new ArrayList<>();

        // endregion

        // region Fill lists

        for (Verification verification : verifications) {
            SimpleDateFormat simpleTaskDate = new SimpleDateFormat("dd.MM.yyyy");
            try {
                taskDate.add(simpleTaskDate.format(calibrationTask.getDateOfTask()));
            } catch (Exception ex) {
                taskDate.add(" ");
            }

            try {
                provider.add(verification.getProvider().getName());
            } catch (Exception ex) {
                provider.add(" ");
            }

            try {
                district.add(verification.getClientData().getClientAddress().getDistrict());
            } catch (Exception ex) {
                district.add(" ");
            }

            try {
                address.add(verification.getClientData().getClientAddress().getAddress());
            } catch (Exception ex) {
                address.add(" ");
            }

            try {
                building.add(verification.getClientData().getClientAddress().getBuilding());
            } catch (Exception ex) {
                building.add(" ");
            }

            try {
                flat.add(verification.getClientData().getClientAddress().getFlat().toString());
            } catch (Exception ex) {
                flat.add(" ");
            }

            try {
                entrance.add(String.valueOf(verification.getInfo().getEntrance()));
            } catch (Exception ex) {
                entrance.add(" ");
            }

            try {
                floor.add(String.valueOf(verification.getInfo().getFloor()));
            } catch (Exception ex) {
                floor.add(" ");
            }

            try {
                countersNumber.add(String.valueOf(1));
            } catch (Exception ex) {
                countersNumber.add(" ");
            }

            try {
                fullName.add(verification.getClientData().getFullName());
            } catch (Exception ex) {
                fullName.add(" ");
            }

            try {
                telephone.add(verification.getClientData().getPhone());
            } catch (Exception ex) {
                telephone.add(" ");
            }

            try {
                time.add(verification.getProcessTimeExceeding().toString());
            } catch (Exception ex) {
                time.add(" ");
            }

            try {
                comment.add(verification.getComment().toString());
            } catch (Exception ex) {
                comment.add(" ");
            }
        }

        // endregion

        // region Fill map

        data.put("Дата завдання", taskDate);
        data.put("Провайдер", provider);
        data.put("Район", district);
        data.put("Адреса", address);
        data.put("Будинок", building);
        data.put("Квартира", flat);
        data.put("Під'їзд", entrance);
        data.put("Поверх", floor);
        data.put("К-ть лічильників", countersNumber);
        data.put("ПІБ", fullName);
        data.put("Телефон", telephone);
        data.put("Бажаний час", time);
        data.put("Примітка", comment);

        // endregion

        return data;
    }

    private Map<String, List<String>> getDataForDbf(CalibrationTask calibrationTask, Verification[] verifications) {
        Map<String, List<String>> data = new LinkedHashMap<>();

        // region Define lists

        // Ідентифікатор заявки
        List<String> id = new ArrayList<>();
        // Прізвище абонента
        List<String> surname = new ArrayList<>();
        // Ім'я абонента
        List<String> name = new ArrayList<>();
        // По-батькові абонента
        List<String> middle = new ArrayList<>();
        // Місто
        List<String> city = new ArrayList<>();
        // Район
        List<String> district = new ArrayList<>();
        // Сектор
        List<String> sector = new ArrayList<>();
        // Вулиця
        List<String> street = new ArrayList<>();
        // Номер будинку
        List<String> building = new ArrayList<>();
        // Номер квартири
        List<String> flat = new ArrayList<>();
        // Телефон
        List<String> telephone = new ArrayList<>();
        // Бажана дата та час перевірки
        List<String> datetime = new ArrayList<>();
        // Номер лічильника
        List<String> counterNumber = new ArrayList<>();
        // Коментар
        List<String> comment = new ArrayList<>();
        // Замовник
        List<String> customer = new ArrayList<>();

        // endregion

        // region Fill lists

        for (Verification verification : verifications) {
            try {
                id.add(verification.getId());
            } catch (Exception ex) {
                id.add(" ");
            }

            try {
                surname.add(verification.getClientData().getLastName());
            } catch (Exception ex) {
                surname.add(" ");
            }

            try {
                name.add(verification.getClientData().getFirstName());
            } catch (Exception ex) {
                name.add(" ");
            }

            try {
                middle.add(verification.getClientData().getMiddleName());
            } catch (Exception ex) {
                middle.add(" ");
            }

            try {
                city.add(verification.getClientData().getClientAddress().getLocality());
            } catch (Exception ex) {
                city.add(" ");
            }

            try {
                district.add(verification.getClientData().getClientAddress().getDistrict());
            } catch (Exception ex) {
                district.add(" ");
            }

            try {
                sector.add(verification.getClientData().getClientAddress().getRegion());
            } catch (Exception ex) {
                sector.add(" ");
            }

            try {
                street.add(verification.getClientData().getClientAddress().getStreet());
            } catch (Exception ex) {
                street.add(" ");
            }

            try {
                building.add(verification.getClientData().getClientAddress().getBuilding());
            } catch (Exception ex) {
                building.add(" ");
            }

            try {
                flat.add(verification.getClientData().getClientAddress().getFlat());
            } catch (Exception ex) {
                flat.add(" ");
            }

            try {
                telephone.add(verification.getClientData().getPhone());
            } catch (Exception ex) {
                telephone.add(" ");
            }

            try {
                SimpleDateFormat simpleTaskDate = new SimpleDateFormat("dd.MM.yyyy");
                datetime.add(simpleTaskDate.format(calibrationTask.getDateOfTask()));
            } catch (Exception ex) {
                datetime.add(" ");
            }

            try {
                counterNumber.add(verification.getDevice().getNumber());
            } catch (Exception ex) {
                counterNumber.add(" ");
            }

            try {
                comment.add(verification.getComment().toString());
            } catch (Exception ex) {
                comment.add(" ");
            }

            try {
                customer.add(verification.getCalibratorEmployee().getUsername());
            } catch (Exception ex) {
                customer.add(" ");
            }
        }

        // endregion

        // region Fill map

        data.put("ID заявки", id);
        data.put("Прізвище", surname);
        data.put("Ім'я", name);
        data.put("По-батьков", middle);
        data.put("Місто", city);
        data.put("Район", district);
        data.put("Сектор", sector);
        data.put("Вулиця", street);
        data.put("Будинок", building);
        data.put("Квартира", flat);
        data.put("Телефон", telephone);
        data.put("Дата/час", datetime);
        data.put("Лічильник", counterNumber);
        data.put("Примітка", comment);
        data.put("Замовник", customer);

        // endregion

        return data;
    }
}