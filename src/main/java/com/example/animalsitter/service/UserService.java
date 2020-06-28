package com.example.animalsitter.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.example.animalsitter.controller.UserController;
import com.example.animalsitter.domain.Animal;
import com.example.animalsitter.domain.User;
import com.example.animalsitter.dto.PositionStackApiDto;
import com.example.animalsitter.dto.PositonStackApiWrapperDto;
import com.example.animalsitter.dto.UserInfoFullDto;
import com.example.animalsitter.dto.request.AnimalWithUserId;
import com.example.animalsitter.exception.UserNotFoundException;
import com.example.animalsitter.repository.Animalrepository;
import com.example.animalsitter.repository.UserRepository;

import javassist.tools.reflect.CannotCreateException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ae.de-donno 
 * 
 * @apiNote Service linked to the {@link UserController}
 *
 */
@Service
@Slf4j
public class UserService {
	
	@Value("${positionstack.apikey}")
	private String POSITION_STACK_API_KEY;
	
	@Value("${positionstack.baseurl}")
	private String POSITION_STACK_API_BASE_URL;

	UserRepository userRepository;

	Animalrepository animalRepository;
	
	PhotoService photoService;
	
	@Autowired
	public UserService(UserRepository userRepository, Animalrepository animalRepository, PhotoService photoService) {
		this.userRepository = userRepository;
		this.animalRepository = animalRepository;
		this.photoService = photoService;
	}

	/**
	 * Retrives all {@link Animal} from {@link User}
	 * 
	 * @param id
	 * @return
	 */
	public List<Animal> getUsersAnimals(UUID id) {
		User user = checkIfUserIsPresent(id);
		return user.getAnimals();
	}
	
	/**
	 * Decompress bytes for each animal
	 * 
	 * @param id
	 * @return
	 */
	public List<Animal> getUsersAnimalsAndDecompressPhotos(UUID id) {
		List<Animal> userAnimals = getUsersAnimals(id);
		userAnimals.stream().filter(a -> a.getPhoto() != null && a.getPhoto().getImage() != null).forEach(a -> photoService.decompressBytes(a.getPhoto().getImage()));
		return userAnimals;
	}
	 

	/**
	 * Creates an {@link Animal} and add it to the list of given {@link User}
	 * 
	 * @param dto
	 * @return
	 * @throws CannotCreateException
	 */
	public User addAnimalToUser(AnimalWithUserId dto) throws CannotCreateException {
		User user = checkIfUserIsPresent(dto.getUserId());
		checkHowManyAnimalsUsersGot(user);
		Animal animal = Animal.of(dto);
		animalRepository.save(animal);
		user.getAnimals().add(animal);
		return userRepository.save(user);
	}

	/**
	 * Check how many animals a {@link User} has, maximum authorized is 3, if answer id more than 3 it throw an exception
	 * 
	 * @param user {@link User}
	 * @throws CannotCreateException
	 */
	public void checkHowManyAnimalsUsersGot(User user) throws CannotCreateException {
		List<Animal> usersAnimals = user.getAnimals();
		if (usersAnimals != null && usersAnimals.size() > 2) {
			throw new CannotCreateException("User already have three animals created");
		}
	}

	/** Update an existing User 
	 * @param uifdto {@link UserInfoFullDto}
	 * @return {@link User}
	 */
	public User updateUser(UserInfoFullDto uifdto) {
		User fromBase = checkIfUserIsPresent(uifdto.getId());

		if (fromBase.getEmail().equals(uifdto.getEmail())) {
			// send mail
		}
		fromBase.setAge(uifdto.getAge());
		fromBase.setEmail(uifdto.getEmail());
		fromBase.setFirstName(uifdto.getFirstName());
		fromBase.setLastName(uifdto.getLastName());
		fromBase.setPhone(uifdto.getPhone());
		// Test voir s'il existe
		fromBase.setPseudo(uifdto.getPseudo());
		fromBase.getAdress().setCity(uifdto.getAdress().getCity());
		fromBase.getAdress().setCountry(uifdto.getAdress().getCountry());
		fromBase.getAdress().setPostalcode(uifdto.getAdress().getPostalcode());
		fromBase.getAdress().setStreet(uifdto.getAdress().getStreet());
		
		// Check that either street or city ou postal code are not empty before calling positionstack API
		if(!StringUtils.isEmpty(uifdto.getAdress().getCity()) && !StringUtils.isEmpty(uifdto.getAdress().getPostalcode()) && !StringUtils.isEmpty(uifdto.getAdress().getStreet())) {
			setLongitudeAndLatitude(fromBase, uifdto.getAdress().getStreet(), uifdto.getAdress().getCity());
		}

		return userRepository.save(fromBase);
	}

	/**
	 * THis method calls position stack API and set longitude and latitude into user's address
	 * 
	 * @param fromBase
	 * @param address
	 * @param city
	 */
	private void setLongitudeAndLatitude(User fromBase, String address, String city) {
		RestTemplate rt = new RestTemplate();
		String url = POSITION_STACK_API_BASE_URL+"?access_key="+POSITION_STACK_API_KEY+
				"&query="+address+
				"&region="+city+
				"&country=FR&limit=5&output=json&fields=results.longitude,results.latitude";
		ResponseEntity<PositonStackApiWrapperDto> response = rt.getForEntity(url , PositonStackApiWrapperDto.class);
		
		// Verify its HTTP response is OK
		if(HttpStatus.OK.equals(response.getStatusCode())) {
			Optional<PositionStackApiDto> longitudeAndLatitude = response.getBody().getData().stream().findFirst();
			if(longitudeAndLatitude.isPresent()) {
				log.info("HHTP OK RESULT, longitude {}, latitude{}", longitudeAndLatitude.get().getLatitude(), longitudeAndLatitude.get().getLongitude());
				fromBase.getAdress().setLongitude(longitudeAndLatitude.get().getLongitude());
				fromBase.getAdress().setLatitude(longitudeAndLatitude.get().getLatitude());
			}
		}else {
			log.info("Error while calling PositionStack API");
		}
	}

	/** Check if user has registered some animals
	 * @param id
	 * @return {@link Boolean}
	 */
	public boolean checkIfUserHasAnimals(UUID id) {
		User user = checkIfUserIsPresent(id);
		boolean response = false;
		if (user.getAnimals() != null && user.getAnimals().size() > 0 && !user.getAnimals().isEmpty()) {
			response = true;
		}
		return response;
	}

	/**
	 * Checks if the profile is complete, or if it's missing some informations
	 * 
	 * @param id
	 * @return
	 */
	public boolean isProfileComplete(UUID id) {
		User user = checkIfUserIsPresent(id);
		return false;
	}

	
	/** 
	 * Simple check in database to see if User from given id is present
	 * 
	 * @param id
	 * @return User from given ID, or throw a {@link UserNotFoundException}
	 */
	public User checkIfUserIsPresent(UUID id) {
		Optional<User> optionalUser = userRepository.findById(id);
		if (!optionalUser.isPresent()) {
			throw new UserNotFoundException("User not found");
		}
		User user = optionalUser.get();
		return user;
	}

}
