package com.softserve.edu.service;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import com.softserve.edu.repository.DeviceRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;

import java.io.FileNotFoundException;

public class DeviceServiceTest {

	@InjectMocks
	DeviceService deviceService;

	@Mock
	private DeviceRepository deviceRepository;

	@Mock
	PageRequest pageRequest;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testExistsWithDeviceid() throws FileNotFoundException {
		Long id = 1L;
		when(deviceRepository.findOne(id)).thenReturn(null);
		Assert.assertFalse(deviceService.existsWithDeviceid(id));

	}

	@Ignore
	@Test
	public void testGetById() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test for null returning if no device.
	 */
	@Test
	public void testGetAll() {
		when(deviceRepository.findAll()).thenReturn(null);
		Assert.assertNull(deviceService.getAll());

	}

	@Test
	public void testGetDevicesBySearchAndPagination() {
		deviceService.getDevicesBySearchAndPagination(2, 5, null);
		verify(deviceRepository).findAll(any(PageRequest.class));

	}

	@Test
	public void testSecondGetDevicesBySearchAndPagination() {
		String search = "search";
		deviceService.getDevicesBySearchAndPagination(2, 5, search);

		verify(deviceRepository).findByNumberLikeIgnoreCase(eq("%" + search + "%"), any(PageRequest.class));

	}

	@Test
	public void testGetAllByType() {
		String deviceName = "Test";
		when(deviceRepository.findByDeviceName(deviceName)).thenReturn(null);
		Assert.assertNull(deviceService.getAllByType(deviceName));
	}

}
