package com.softserve.edu.service.catalogue;

import com.softserve.edu.entity.catalogue.Building;

import java.util.List;

public interface BuildingService {

    List<Building> getBuildingsCorrespondingStreet(Long streetId);
}
