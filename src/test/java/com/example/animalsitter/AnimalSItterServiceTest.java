package com.example.animalsitter;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.animalsitter.domain.Animal;
import com.example.animalsitter.dto.AnimalDTO;
import com.example.animalsitter.repository.Animalrepository;
import com.example.animalsitter.service.AnimalService;

public class AnimalSItterServiceTest {

	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@InjectMocks
	AnimalService target;
	
	@Mock
	Animalrepository animalRepositoryMock;

	@Captor
	private ArgumentCaptor<Animal> animalCaptor;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		target = spy(new AnimalService(animalRepositoryMock));
	}
	
	@Test
	public void createsAnimal_Test() throws IOException {
		// Given
		AnimalDTO dto = AnimalDTO.builder().name("Name").age(2).eyesColor("eyesColor").furColor("furColor").sick(true).tatoo("tatoo").build();
		
		// When
		target.createAnimal(dto, null);
		
		// Then
		verify(animalRepositoryMock).save(animalCaptor.capture());
		Animal result = animalCaptor.getValue();

		assertNotNull(result);
		assertEquals("Name", result.getName());
	}
	
}
