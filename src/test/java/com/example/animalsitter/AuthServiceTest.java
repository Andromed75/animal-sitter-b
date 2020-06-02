package com.example.animalsitter;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.animalsitter.domain.Role;
import com.example.animalsitter.domain.User;
import com.example.animalsitter.dto.UserDto;
import com.example.animalsitter.enums.ERole;
import com.example.animalsitter.repository.RoleRepository;
import com.example.animalsitter.repository.UserRepository;
import com.example.animalsitter.service.AuthService;

public class AuthServiceTest {

	@Mock
	UserRepository userRepoMock;

	@Mock
	RoleRepository roleRepoMock;

	@Mock
	PasswordEncoder encoder;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@InjectMocks
	AuthService target;

	@Captor
	private ArgumentCaptor<User> userCaptor;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		target = spy(new AuthService(userRepoMock, roleRepoMock, encoder));
	}

	@Test
	public void createUser_Test() {
		// Given
		UserDto userDto = new UserDto("Pseudo", "password", "email@email.com");
		Role role = new Role(ERole.ROLE_USER);
		Mockito.when(roleRepoMock.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(role));
		Mockito.when(encoder.encode(userDto.getPassword())).thenReturn("password");

		// When
		target.createUser(userDto);

		// Then
		verify(userRepoMock).save(userCaptor.capture());
		User result = userCaptor.getValue();

		assertNotNull(result);
		assertEquals("Pseudo", result.getPseudo());
	}

	@Test
	public void createsUser_ThrowError() {
		// Given
		UserDto userDto = new UserDto("Pseudo", "password", "email@email.com");
		Role role = new Role(ERole.ROLE_USER);
		Mockito.when(roleRepoMock.findByName(ERole.ROLE_USER)).thenThrow(RuntimeException.class);
		Mockito.when(encoder.encode(userDto.getPassword())).thenReturn("password");

		// When
		expectedException.expect(RuntimeException.class);
		target.createUser(userDto);
		
		// Then
		// Exception is thrown
	}

}
