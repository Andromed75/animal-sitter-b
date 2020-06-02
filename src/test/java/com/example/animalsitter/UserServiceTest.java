package com.example.animalsitter;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.animalsitter.domain.Admin;
import com.example.animalsitter.domain.User;
import com.example.animalsitter.enums.ERole;
import com.example.animalsitter.repository.UserRepository;
import com.example.animalsitter.service.UserService;

public class UserServiceTest {

	@Mock
	UserRepository userRepoMock;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@InjectMocks
	UserService target;

	@Captor
	private ArgumentCaptor<User> userCaptor;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		target = spy(new UserService());
	}

	@Test
	public void checkHowManyAnimalsUsersGot_Test() {
	    UUID uuid = UUID.randomUUID();
	    
	}

	@Test
	public void checkIfUserHasAnimals() {
		// GIVEN
		// WHEN 
		
		// THEN
		
	}
	
	  @Test
	  public void findUsersTest_findAll_is_null() {
	    // Given
	  
	  }
	  
	  @Test
	  public void createUser_Test() {
	
	  }

}
