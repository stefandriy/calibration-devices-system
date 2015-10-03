package com.softserve.edu.service.tool;

import com.softserve.edu.entity.device.Device;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DeviceService {

    boolean existsWithDeviceId(Long id);

    Device getById(Long id);

    List<Device> getAll();

    Page<Device> getDevicesBySearchAndPagination(int pageNumber, int itemsPerPage, String search);

    List<Device> getAllByType(String device);
}
