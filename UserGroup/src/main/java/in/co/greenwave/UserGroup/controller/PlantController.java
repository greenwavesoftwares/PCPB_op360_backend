package in.co.greenwave.UserGroup.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Import necessary DAO and Model classes
import in.co.greenwave.UserGroup.dao.RestDAO;
import in.co.greenwave.UserGroup.model.PlantModel;

// This is the PlantController class which handles plant-related requests.
// It is marked as a REST API controller and listens for requests under the "/plants" URL.
@RestController
@RequestMapping("/plants")
public class PlantController {

	// Injecting the RestDAO dependency to interact with the database.
	@Autowired
	private RestDAO restDao;

	// This method handles GET requests to fetch information about all plants.
	// It retrieves plant data from the database and returns it in a response.
	@GetMapping("/")
	public ResponseEntity<?> getPlantInfo() {
		// Create a list to hold plant information.
		List<PlantModel> plantList = new ArrayList<>();
		// Fetch plant information using DAO.
		plantList = restDao.getPlantInfo();
		// If plant data is found, return the list of plants with HTTP status 200 (OK).
		if(plantList != null) {
			return new ResponseEntity<>(plantList, HttpStatus.OK);
		}
		// If no plant data is found, return an error message with HTTP status 400 (BAD REQUEST).
		return new ResponseEntity<>("Could not get Plant Details", HttpStatus.BAD_REQUEST);
	}

	// This method handles GET requests to fetch information about a specific plant by its ID.
	// It accepts the plant ID as a request parameter and returns the corresponding plant's details.
	@GetMapping("/plant")
	public ResponseEntity<?> getAnPlantInfo(@RequestParam String plantId) {
		// Create a PlantModel object to hold the fetched plant data.
		PlantModel plant = new PlantModel();
		// Fetch plant information based on the plant ID.
		plant = restDao.getAnPlantInfo(plantId);
		// If plant data is found, return it with HTTP status 200 (OK).
		if(plant != null) {
			return new ResponseEntity<>(plant, HttpStatus.OK);
		}
		// If no plant data is found, return an error message with HTTP status 400 (BAD REQUEST).
		return new ResponseEntity<>("Could not get Plant Details", HttpStatus.BAD_REQUEST);
	}

	// This method handles POST requests to add a new plant to the system.
	// The plant data is sent in the request body and is used to add a new plant entry in the database.
	@PostMapping("/plant")
	public ResponseEntity<String> addNewPlant(@RequestBody PlantModel plantModel) {
		// Call the DAO method to add the new plant and store the result (true if successful, false otherwise).
		boolean result = restDao.addNewPlant(plantModel);
		System.out.println("result : " + result);
		// If the plant was added successfully, return a success message with HTTP status 201 (CREATED).
		if(result) {
			System.out.println("Plant added successfully");
			return new ResponseEntity<>("Plant added successfully", HttpStatus.CREATED);
		}
		// If the plant was not added, return an error message with HTTP status 400 (BAD REQUEST).
		System.out.println("Plant not added");
		return new ResponseEntity<>("Plant not added", HttpStatus.BAD_REQUEST);
	}

	// This method handles DELETE requests to remove a plant from the system.
	// It accepts the plant ID as a request parameter and deletes the corresponding plant.
	@DeleteMapping("/plant")
	public ResponseEntity<String> addNewUser(@RequestParam String plantId) {
		// Call the DAO method to delete the plant by its ID and store the result.
		boolean result = restDao.deletePlantByPlantId(plantId);
		// If the plant was not deleted, return an error message with HTTP status 400 (BAD REQUEST).
		if(!result) {
			return new ResponseEntity<>("Plant not deleted", HttpStatus.BAD_REQUEST);
		}
		// If the plant was successfully deleted, return a success message with HTTP status 200 (OK).
		return new ResponseEntity<>("Plant deleted successfully", HttpStatus.OK);
	}
}
