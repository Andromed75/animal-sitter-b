package com.example.animalsitter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.animalsitter.domain.Animal;
import com.example.animalsitter.domain.User;
import com.example.animalsitter.repository.Animalrepository;
import com.example.animalsitter.repository.UserRepository;
import com.example.animalsitter.service.PhotoService;
import com.example.animalsitter.service.UserService;

import javassist.tools.reflect.CannotCreateException;

public class UserServiceTest {

	@Mock
	UserRepository userRepoMock;

	@Mock
	Animalrepository animalRepoMock;
	
	@Mock
	PhotoService photoServiceMock;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@InjectMocks
	UserService target;

	@Captor
	private ArgumentCaptor<User> userCaptor;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		target = spy(new UserService(userRepoMock, animalRepoMock, photoServiceMock));
	}

	@Test
	public void checkHowManyAnimalsUsersGot_Test() throws CannotCreateException {
		// Given
		User user = new User();
		List<Animal> animalList = new ArrayList<>();
		user.setAnimals(animalList);
		animalList.add(new Animal());

		// When
		target.checkHowManyAnimalsUsersGot(user);

		// Then

		// Nothing happens because the check went through

	}

	@Test
	public void checkHowManyAnimalsUsersGot_TestFail() throws CannotCreateException {
		// Given
		User user = new User();
		List<Animal> animalList = new ArrayList<>();
		user.setAnimals(animalList);
		animalList.add(new Animal());
		animalList.add(new Animal());
		animalList.add(new Animal());

		// When
		expectedException.expect(CannotCreateException.class);
		target.checkHowManyAnimalsUsersGot(user);

		// Then

		// Exception is thrown

	}

	@Test
	public void getUsersAnimals_Test() {
		// GIVEN
		UUID uuid = UUID.randomUUID();
		User user = new User();
		List<Animal> animalList = new ArrayList<>();
		user.setAnimals(animalList);
		animalList.add(Animal.builder().name("animal1").build());
		animalList.add(Animal.builder().name("animal2").build());
		Mockito.when(userRepoMock.findById(uuid)).thenReturn(Optional.of(user));

		// WHEN
		List<Animal> expectedList = target.getUsersAnimals(uuid);

		// THEN
		assertEquals(expectedList, animalList);
	}

	@Test
	public void checkIfUserIsPresent() {
		// Given
		UUID uuid = UUID.randomUUID();
		User user = new User();
		Mockito.when(userRepoMock.findById(uuid)).thenReturn(Optional.of(user));
		
		
		// When
		User expectedUser = target.checkIfUserIsPresent(uuid);
		
		// Then
		
		assertEquals(expectedUser, user);
	}

}
