package com.travelnow;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import com.travelnow.dao.HotelDao;
import com.travelnow.models.Address;
import com.travelnow.models.BasicHotelInfo;
import com.travelnow.models.Room;
import com.travelnow.services.HotelsService;


import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.Assert.*;


@SpringBootTest
class TravelNowHotelsServiceTest {

	@InjectMocks
	private HotelsService hotelsService;

	@Mock
	private HotelDao hotelDao;

	@Before
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();

	@Test
	void testBasicHotelInfoValidators() throws Exception
	{
		// mock preparation

		BasicHotelInfo testHotel = new BasicHotelInfo();
		testHotel.setName("TestHotel");
		testHotel.setDescription("This is a test hotel");
		testHotel.setTelephoneNum(987654321);
		testHotel.setId(1);

		List<BasicHotelInfo> ownerHotels = new ArrayList<BasicHotelInfo>();
		ownerHotels.add(testHotel);

		when(hotelDao.getHotelsByOwnerId(1)).thenReturn(ownerHotels);

		doAnswer(AdditionalAnswers.answerVoid((operand, name) -> testHotel.setName((String) name) )
		).when(hotelDao).updateHotelName(anyInt(), anyString());
		
		doAnswer(AdditionalAnswers.answerVoid((operand, desc) -> testHotel.setDescription((String) desc) )
		).when(hotelDao).updateHotelDescription(anyInt(), anyString());		

		doAnswer(AdditionalAnswers.answerVoid((operand, num) -> testHotel.setTelephoneNum((Integer) num) )
		).when(hotelDao).updateTelNumber(anyInt(), anyInt());		
		

		// test valid data
 		hotelsService.updateBasicHotelInfo(1, "TestHotel2", "This is a test hotel 2", 123456798, 1);
		assertEquals(testHotel.getName(), "TestHotel2");
		assertEquals(testHotel.getDescription(), "This is a test hotel 2");
		assertEquals(testHotel.getTelephoneNum(), 123456798);

		// test invalid data
		Exception exception = null;
		// empty name
		try{
			hotelsService.updateBasicHotelInfo(1, "", "This is a test hotel 2", 123456798, 1);	
		}
		catch(Exception ex)
		{
			exception = ex;
		}
		assertNotNull(exception);
		assertTrue(exception.getMessage().contains("Hotel name can't be empty"));

		// empty description
		exception = null;
		try{
			hotelsService.updateBasicHotelInfo(1, "TestHotel2", "", 123456798, 1);	
		}
		catch(Exception ex)
		{
			exception = ex;
		}
		assertNotNull(exception);
		assertTrue(exception.getMessage().contains("Description can't be empty"));

		// wrong tel num digist num
		exception = null;
		try{
			hotelsService.updateBasicHotelInfo(1, "TestHotel2", "This is a test hotel 2", 1236798, 1);	
		}
		catch(Exception ex)
		{
			exception = ex;
		}
		assertNotNull(exception);
		assertTrue(exception.getMessage().contains("Tel number has wrong number of digits"));
	}

	@Test
	void testAddressValidators() throws Exception
	{
		// mock preparation

		BasicHotelInfo testHotel = new BasicHotelInfo();
		testHotel.setId(1);

		List<BasicHotelInfo> ownerHotels = new ArrayList<BasicHotelInfo>();
		ownerHotels.add(testHotel);

		when(hotelDao.getHotelsByOwnerId(1)).thenReturn(ownerHotels);

		doAnswer(AdditionalAnswers.answerVoid((operand, name) -> testHotel.setAddress((Address) name) )
		).when(hotelDao).updateAddress(anyInt(), any(Address.class));

		Address address = new Address();
		address.setCity("Warszawa");
		address.setRegion("Mazowieckie");
		address.setCountry("Polska");
		address.setStreet("Grodzka");
		address.setNumber(25);
		address.setPostalcode("02-123");
		
		// test valid data
		hotelsService.updateAddress(1, address, 1);
		assertEquals(testHotel.getAddress(), address);

		// test invalid data


		// empty city
		Exception exception = null;
		address = new Address();
		address.setCity("");
		address.setRegion("Mazowieckie");
		address.setCountry("Polska");
		address.setStreet("Grodzka");
		address.setNumber(25);
		address.setPostalcode("02-123");

		try{
			hotelsService.updateAddress(1, address, 1);
		}
		catch(Exception ex)
		{
			exception = ex;
		}
		assertNotNull(exception);
		assertTrue(exception.getMessage().contains("City can't be empty"));

		// empty country
		exception = null;
		address = new Address();
		address.setCity("Warszawa");
		address.setRegion("Mazowieckie");
		address.setCountry("");
		address.setStreet("Grodzka");
		address.setNumber(25);
		address.setPostalcode("02-123");

		try{
			hotelsService.updateAddress(1, address, 1);
		}
		catch(Exception ex)
		{
			exception = ex;
		}
		assertNotNull(exception);
		assertTrue(exception.getMessage().contains("Country can't be empty"));

		// empty region
		exception = null;
		address = new Address();
		address.setCity("Warszawa");
		address.setRegion("");
		address.setCountry("Polska");
		address.setStreet("Grodzka");
		address.setNumber(25);
		address.setPostalcode("02-123");

		try{
			hotelsService.updateAddress(1, address, 1);
		}
		catch(Exception ex)
		{
			exception = ex;
		}
		assertNotNull(exception);
		assertTrue(exception.getMessage().contains("Region can't be empty"));


		// empty street
		exception = null;
		address = new Address();
		address.setCity("Warszawa");
		address.setRegion("Mazowieckie");
		address.setCountry("Polska");
		address.setStreet("");
		address.setNumber(25);
		address.setPostalcode("02-123");

		try{
			hotelsService.updateAddress(1, address, 1);
		}
		catch(Exception ex)
		{
			exception = ex;
		}
		assertNotNull(exception);
		assertTrue(exception.getMessage().contains("Street can't be empty"));


		// empty region
		exception = null;
		address = new Address();
		address.setCity("Warszawa");
		address.setRegion("Mazowieckie");
		address.setCountry("Polska");
		address.setStreet("grodzka");
		address.setNumber(25);
		address.setPostalcode("");

		try{
			hotelsService.updateAddress(1, address, 1);
		}
		catch(Exception ex)
		{
			exception = ex;
		}
		assertNotNull(exception);
		assertTrue(exception.getMessage().contains("Postal code can't be empty"));

		// empty region
		exception = null;
		address = new Address();
		address.setCity("Warszawa");
		address.setRegion("Mazowieckie");
		address.setCountry("Polska");
		address.setStreet("grodzka");
		address.setNumber(0);
		address.setPostalcode("02-432");

		try{
			hotelsService.updateAddress(1, address, 1);
		}
		catch(Exception ex)
		{
			exception = ex;
		}
		assertNotNull(exception);
		assertTrue(exception.getMessage().contains("Building num cant be empty"));
	}

	@Test
	void testRoomsValidators() throws Exception
	{
		// mock preparation
		BasicHotelInfo testHotel = new BasicHotelInfo();
		testHotel.setId(1);

		List<Room> roomsRepo = new ArrayList<Room>();

		List<BasicHotelInfo> ownerHotels = new ArrayList<BasicHotelInfo>();
		ownerHotels.add(testHotel);

		when(hotelDao.getHotelsByOwnerId(1)).thenReturn(ownerHotels);

		doAnswer(new Answer<Integer>() {
			public Integer answer(InvocationOnMock invocation) {
			   Room room = (Room) invocation.getArguments()[0];
			   roomsRepo.add(room);
			   return roomsRepo.size() - 1;
			}
		}).when(hotelDao).insertRoom(any(Room.class));

		
		doAnswer(new Answer<List<Room>>() {
			public List<Room> answer(InvocationOnMock invocation) {
			   return roomsRepo;
			}
		}).when(hotelDao).getRoomsByHotelsId(anyInt());

		// test valid data
		List<Room> newRooms = new ArrayList<Room>();
		Room room = new Room();
		room.setPrice(BigDecimal.valueOf(200));
		room.setNumber(1);
		room.setCapacity(2);
		newRooms.add(room);

		hotelsService.addRooms(1, newRooms, 1);
		Room addedRoom = roomsRepo.stream().filter(r -> r.getNumber() == 1).findFirst().orElse(null);
		assertNotNull(addedRoom);
		assertEquals(addedRoom, room);

		// test invalid data - numbers not unique
		Exception exception = null;
		roomsRepo.clear();
		newRooms = new ArrayList<Room>();
		room = new Room();
		room.setPrice(BigDecimal.valueOf(200));
		room.setNumber(1);
		room.setCapacity(2);
		newRooms.add(room);
		newRooms.add(room);

		try{
			hotelsService.addRooms(1, newRooms, 1);
		}
		catch(Exception ex)
		{
			exception = ex;
		}
		assertNotNull(exception);
		assertTrue(exception.getMessage().contains("Room numbers are not unique"));

		// test invalid data
		exception = null;
		roomsRepo.clear();
		newRooms = new ArrayList<Room>();
		room = new Room();
		room.setPrice(BigDecimal.valueOf(200));
		room.setNumber(1);
		room.setCapacity(2000);
		newRooms.add(room);

		try{
			hotelsService.addRooms(1, newRooms, 1);
		}
		catch(Exception ex)
		{
			exception = ex;
		}
		assertNotNull(exception);
		assertTrue(exception.getMessage().contains("capacity is to big"));
	}

	// mvn test -Dtest=com.travelnow.TravelNowHotelsServiceTest test
}
