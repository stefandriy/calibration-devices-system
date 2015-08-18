package com.softserve.edu.entity;

import com.softserve.edu.entity.catalogue.Region;
import com.softserve.edu.repository.catalogue.RegionRepository;
import com.softserve.edu.service.catalogue.RegionService;
import config.DBUnitConfig;
import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by Loc on 17.08.2015.
 */
public class RegionTest extends DBUnitConfig{

    private RegionService service = new RegionService();
  //  private EntityManager em = Persistence.createEntityManagerFactory("DBUnitEx").createEntityManager();
  //private RegionRepository regionRepository;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        beforeData = new FlatXmlDataSetBuilder().build(
                Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream("../resources/com/softserve/edu/entity/Region/region-data.xml"));

        tester.setDataSet(beforeData);
        tester.onSetup();
    }

    public RegionTest(String name) {
        super(name);
    }

    @Test
    public void testGetAll() throws Exception {
        List<Region> Regions = (List<Region>) service.getAll();

        IDataSet expectedData = new FlatXmlDataSetBuilder().build(
                Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream("com/softserve/edu/entity/Region/region-data.xml"));

        IDataSet actualData = tester.getConnection().createDataSet();
        Assertion.assertEquals(expectedData, actualData);
        Assert.assertEquals(expectedData.getTable("Region").getRowCount(), Regions.size());
    }

   /* @Test
    public void testSave() throws Exception {
        Region Region = new Region("Aas");
        Region.setId((long) 1);


        service.save(Region);

        IDataSet expectedData = new FlatXmlDataSetBuilder().build(
                Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream("com/devcolibri/entity/Region/Region-data-save.xml"));

        IDataSet actualData = tester.getConnection().createDataSet();

        String[] ignore = {"id"};
        Assertion.assertEqualsIgnoreCols(expectedData, actualData, "Region", ignore);
    }

    //others tests*/

}