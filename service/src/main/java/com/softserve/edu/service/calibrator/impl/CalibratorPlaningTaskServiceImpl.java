package com.softserve.edu.service.calibrator.impl;

import com.softserve.edu.common.Constants;
import com.softserve.edu.entity.catalogue.Team.DisassemblyTeam;
import com.softserve.edu.entity.device.CalibrationModule;
import com.softserve.edu.entity.device.CounterType;
import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.verification.calibration.CalibrationTask;
import com.softserve.edu.repository.*;
import com.softserve.edu.repository.catalogue.DistrictRepository;
import com.softserve.edu.repository.catalogue.LocalityRepository;
import com.softserve.edu.repository.catalogue.StreetRepository;
import com.softserve.edu.service.calibrator.CalibratorPlanningTaskService;
import com.softserve.edu.service.tool.MailService;
import com.softserve.edu.service.utils.ZipArchiver;
import com.softserve.edu.service.utils.export.DbTableExporter;
import com.softserve.edu.service.utils.export.TableExportColumn;
import com.softserve.edu.service.utils.export.XlsTableExporter;
import com.softserve.edu.specification.CalibrationTaskSpecificationBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
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

    @Autowired
    private LocalityRepository localityRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private StreetRepository streetRepository;


    private Logger logger = Logger.getLogger(CalibratorPlaningTaskServiceImpl.class);

    /**
     * This method returns filtered and sorted
     * page of calibration tasks
     *
     * @param filterParams filtering parameters
     * @param pageable     parameters for pagination and sorting
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
     * This method changes the date of calibration task
     *
     * @param taskID     ID of the calibration task the date of which is to be changed
     * @param dateOfTask new task date
     */
    public void changeTaskDate(Long taskID, Date dateOfTask) {
        CalibrationTask task = taskRepository.findOne(taskID);
        if (task == null || dateOfTask == null) {
            throw new IllegalArgumentException();
        }
        task.setDateOfTask(dateOfTask);
        try {
            taskRepository.save(task);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * This method saves new task for the station. It checks if counter
     * statuses for the verifications are the same, if not
     * Also it checks if calibration module device type is the same
     * as device type of the verification, if not method @throws IllegalArgumentException().
     *
     * @param taskDate
     * @param moduleSerialNumber
     * @param verificationsId
     * @param userId
     * @throws IllegalArgumentException().
     */
    @Override
    public Boolean addNewTaskForStation(Date taskDate, String moduleSerialNumber, List<String> verificationsId, String userId) {
        Boolean taskAlreadyExists = true;
        CalibrationTask task = taskRepository.findByDateOfTaskAndModule_SerialNumber(taskDate, moduleSerialNumber);
        if (task == null) {
            taskAlreadyExists = false;
            CalibrationModule module = moduleRepository.findBySerialNumber(moduleSerialNumber);
            if (module == null) {
                logger.error("module wasn't found");
                throw new IllegalArgumentException();
            }
            User user = userRepository.findOne(userId);
            if (user == null) {
                logger.error("user wasn't found");
                throw new IllegalArgumentException();
            }
            task = new CalibrationTask(module, null, new Date(), taskDate, user);
            taskRepository.save(task);
        }
        Iterable<Verification> verifications = verificationRepository.findAll(verificationsId);
        for (Verification verification : verifications) {
            verification.setStatus(Status.TEST_PLACE_DETERMINED);
            verification.setTaskStatus(Status.TEST_PLACE_DETERMINED);
            verification.setTask(task);
        }
        verificationRepository.save(verifications);
        return taskAlreadyExists;
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
                    if (team.getSpecialization().contains(verification.getDevice().getDeviceType())) {
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
        } else if (sortCriteria.equals("serviceability")) {
            if (sortOrder.equals("asc")) {
                pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.ASC,
                        "info.serviceability"));
            } else {
                pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.DESC,
                        "info.serviceability"));
            }
        } else if (sortCriteria.equals("sealPresence")) {
            if (sortOrder.equals("asc")) {
                pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.ASC,
                        "sealPresence"));
            } else {
                pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.DESC,
                        "sealPresence"));
            }
        } else if (sortCriteria.equals("telephone")) {
            if (sortOrder.equals("asc")) {
                pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.ASC,
                        "clientData.phone"));
            } else {
                pageRequest = new PageRequest(pageNumber - 1, itemsPerPage, new Sort(Sort.Direction.DESC,
                        "clientData.phone"));
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

    /**
     * Sends task to station
     *
     * @param id Task id
     * @throws Exception
     */
    public void sendTaskToStation(Long id) throws Exception {
        CalibrationTask calibrationTask = taskRepository.findOne(id);
        Verification[] verifications = verificationRepository.findByTask_Id(id);

        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DAY_MONTH_YEAR);

        String filename = calibrationTask.getModule().getModuleNumber() + "-" +
                dateFormat.format(calibrationTask.getDateOfTask()) + "_";

        File zipFile = File.createTempFile(filename, "." + Constants.ZIP_EXTENSION);
        zipFile.setWritable(true);
        zipFile.setReadable(true);
        zipFile.setExecutable(true);
        File xlsFile = File.createTempFile(filename, "." + Constants.XLS_EXTENSION);
        xlsFile.setWritable(true);
        xlsFile.setReadable(true);
        xlsFile.setExecutable(true);
        File dbFile = File.createTempFile(filename, "." + Constants.DB_EXTENSION);
        dbFile.setWritable(true);
        dbFile.setReadable(true);
        dbFile.setExecutable(true);

        try {
            XlsTableExporter xls = new XlsTableExporter();
            List<TableExportColumn> dataForXls = getDataForXls(calibrationTask, verifications);
            xls.exportToFile(dataForXls, xlsFile);

            DbTableExporter db = new DbTableExporter(Constants.DEFAULT_DB_TABLE_NAME);
            List<TableExportColumn> dataForDb = getDataForDb(calibrationTask, verifications);
            db.exportToFile(dataForDb, dbFile);

            List<File> files = new ArrayList<>();
            files.add(xlsFile);
            files.add(dbFile);

            ZipArchiver zip = new ZipArchiver();
            zip.createZip(files, zipFile);

            String email = calibrationTask.getModule().getEmail();
            mailService.sendMailWithAttachments(email, Constants.TASK + " " + calibrationTask.getId(), " ", zipFile);
            calibrationTask.setStatus(Status.SENT_TO_TEST_DEVICE);
            for (Verification verification : verifications) {
                verification.setStatus(Status.SENT_TO_TEST_DEVICE);
                verification.setTaskStatus(Status.SENT_TO_TEST_DEVICE);
            }
            taskRepository.save(calibrationTask);
            verificationRepository.save(Arrays.asList(verifications));
        } catch (Exception ex) {
            logger.error(ex);
            throw new RuntimeException(ex);
        } finally {
            xlsFile.delete();
            dbFile.delete();
        }
    }

    private List<TableExportColumn> getDataForXls(CalibrationTask calibrationTask, Verification[] verifications) {
        List<TableExportColumn> data = new ArrayList<TableExportColumn>();

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
                logger.error(ex);
            }

            try {
                provider.add(verification.getProvider().getName());
            } catch (Exception ex) {
                provider.add(" ");
                logger.error(ex);
            }

            try {
                district.add(verification.getClientData().getClientAddress().getDistrict());
            } catch (Exception ex) {
                district.add(" ");
                logger.error(ex);
            }

            try {
                address.add(verification.getClientData().getClientAddress().getAddress());
            } catch (Exception ex) {
                address.add(" ");
                logger.error(ex);
            }

            try {
                building.add(verification.getClientData().getClientAddress().getBuilding());
            } catch (Exception ex) {
                building.add(" ");
                logger.error(ex);
            }

            try {
                flat.add(verification.getClientData().getClientAddress().getFlat().toString());
            } catch (Exception ex) {
                flat.add(" ");
                logger.error(ex);
            }

            try {
                entrance.add(String.valueOf(verification.getInfo().getEntrance()));
            } catch (Exception ex) {
                entrance.add(" ");
                logger.error(ex);
            }

            try {
                floor.add(String.valueOf(verification.getInfo().getFloor()));
            } catch (Exception ex) {
                floor.add(" ");
                logger.error(ex);
            }

            try {
                countersNumber.add(String.valueOf(1));
            } catch (Exception ex) {
                countersNumber.add(" ");
                logger.error(ex);
            }

            try {
                fullName.add(verification.getClientData().getFullName());
            } catch (Exception ex) {
                fullName.add(" ");
                logger.error(ex);
            }

            try {
                telephone.add(verification.getClientData().getPhone());
            } catch (Exception ex) {
                telephone.add(" ");
                logger.error(ex);
            }

            try {
                time.add(verification.getProcessTimeExceeding().toString());
            } catch (Exception ex) {
                time.add(" ");
                logger.error(ex);
            }

            try {
                comment.add(verification.getComment().toString());
            } catch (Exception ex) {
                comment.add(" ");
                logger.error(ex);
            }
        }

        // endregion

        // region Fill map

        data.add(new TableExportColumn(Constants.TASK_DATE, taskDate));
        data.add(new TableExportColumn(Constants.PROVIDER, provider));
        data.add(new TableExportColumn(Constants.REGION, district));
        data.add(new TableExportColumn(Constants.ADDRESS, address));
        data.add(new TableExportColumn(Constants.BUILDING, building));
        data.add(new TableExportColumn(Constants.FLAT, flat));
        data.add(new TableExportColumn(Constants.ENTRANCE, entrance));
        data.add(new TableExportColumn(Constants.FLOOR, floor));
        data.add(new TableExportColumn(Constants.COUNTERS_NUMBER, countersNumber));
        data.add(new TableExportColumn(Constants.FULL_NAME_SHORT, fullName));
        data.add(new TableExportColumn(Constants.PHONE_NUMBER, telephone));
        data.add(new TableExportColumn(Constants.DESIRABLE_TIME, time));
        data.add(new TableExportColumn(Constants.COMMENT, comment));

        // endregion

        return data;
    }

    private List<TableExportColumn> getDataForDb(CalibrationTask calibrationTask, Verification[] verifications) {
        List<TableExportColumn> data = new ArrayList<>();

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
        List<String> cityId = new ArrayList<>();
        // Район
        List<String> district = new ArrayList<>();
        List<String> districtId = new ArrayList<>();
        // Сектор
        List<String> sector = new ArrayList<>();
        // Вулиця
        List<String> street = new ArrayList<>();
        List<String> streetId = new ArrayList<>();
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
                logger.error(ex);
            }

            try {
                surname.add(verification.getClientData().getLastName());
            } catch (Exception ex) {
                surname.add(" ");
                logger.error(ex);
            }

            try {
                name.add(verification.getClientData().getFirstName());
            } catch (Exception ex) {
                name.add(" ");
                logger.error(ex);
            }

            try {
                middle.add(verification.getClientData().getMiddleName());
            } catch (Exception ex) {
                middle.add(" ");
                logger.error(ex);
            }

            try {
                city.add(verification.getClientData().getClientAddress().getLocality());
            } catch (Exception ex) {
                city.add(" ");
                logger.error(ex);
            }

            try {
                String localityTemp = verification.getClientData().getClientAddress().getLocality();
                cityId.add(localityRepository.findIdByDesignation(localityTemp).toString());
            } catch (Exception ex) {
                city.add(" ");
                logger.error(ex);
            }


            try {
                district.add(verification.getClientData().getClientAddress().getDistrict());
            } catch (Exception ex) {
                district.add(" ");
                logger.error(ex);
            }

            try {
                String districtTemp = verification.getClientData().getClientAddress().getDistrict();
                districtId.add(districtRepository.findIdByDesignation(districtTemp).toString());
            } catch (Exception ex) {
                districtId.add(" ");
                logger.error(ex);
            }

            // TODO: Now there is details about "Bush".
            sector.add(" ");
            /*try {
                sector.add(verification.getClientData().getClientAddress().getRegion());
            } catch (Exception ex) {
                sector.add(" ");
                logger.error(ex);
            }*/

            try {
                street.add(verification.getClientData().getClientAddress().getStreet());
            } catch (Exception ex) {
                street.add(" ");
                logger.error(ex);
            }

            try {
                streetId.add(verification.getClientData().getClientAddress().getStreet());
            } catch (Exception ex) {
                streetId.add(" ");
                logger.error(ex);
            }

            try {
                building.add(verification.getClientData().getClientAddress().getBuilding());
            } catch (Exception ex) {
                building.add(" ");
                logger.error(ex);
            }

            try {
                flat.add(verification.getClientData().getClientAddress().getFlat());
            } catch (Exception ex) {
                flat.add(" ");
                logger.error(ex);
            }

            try {
                telephone.add(verification.getClientData().getPhone());
            } catch (Exception ex) {
                telephone.add(" ");
                logger.error(ex);
            }

            try {
                SimpleDateFormat simpleTaskDate = new SimpleDateFormat("dd.MM.yyyy");
                datetime.add(simpleTaskDate.format(calibrationTask.getDateOfTask()));
            } catch (Exception ex) {
                datetime.add(" ");
                logger.error(ex);
            }

            try {
                counterNumber.add(verification.getCounter().getNumberCounter());
            } catch (Exception ex) {
                counterNumber.add("-");
                logger.error(ex);
            }

            try {
                comment.add(verification.getComment().toString());
            } catch (Exception ex) {
                comment.add(" ");
                logger.error(ex);
            }

            try {
                customer.add(verification.getCalibratorEmployee().getUsername());
            } catch (Exception ex) {
                customer.add(" ");
                logger.error(ex);
            }
        }

        // endregion

        // region Fill List<TableExportColumn>

        // ID_Заявки
        data.add(new TableExportColumn("id_pc", "INTEGER", id));
        // Прізвище_абонента
        data.add(new TableExportColumn("surname", "TEXT", surname));
        // Ім'я_абонента
        data.add(new TableExportColumn("name", "TEXT", name));
        // По-батькові_абонента
        data.add(new TableExportColumn("middlename", "TEXT", middle));
        // Місто
        data.add(new TableExportColumn("city", "TEXT", city));
        //data.add(new TableExportColumn("id_city", "TEXT", cityId));
        // Район
        data.add(new TableExportColumn("district", "TEXT", district));
        //data.add(new TableExportColumn("id_district", "TEXT", districtId));
        // Сектор
        data.add(new TableExportColumn("bush", "TEXT", sector));
        // Вулиця
        data.add(new TableExportColumn("street", "TEXT", street));
        //data.add(new TableExportColumn("id_street", "TEXT", streetId));
        // Будинок
        data.add(new TableExportColumn("Building", "TEXT", building));
        // Квартира
        data.add(new TableExportColumn("Apartment", "TEXT", flat));
        // Телефон
        data.add(new TableExportColumn("Tel", "TEXT", telephone));
        // Дата/час_повірки
        data.add(new TableExportColumn("Date_visit", "TEXT", datetime));
        // Номер_лічильника
        data.add(new TableExportColumn("Counter_number", "TEXT", counterNumber));
        // Коментар
        data.add(new TableExportColumn("Note", "TEXT", comment));
        // Замовник
        data.add(new TableExportColumn("Customer", "TEXT", customer));

        // endregion

        return data;
    }
}