package com.vms2.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vms2.dto.VisitormeetingDTO;
import com.vms2.entity.Visitor;
import com.vms2.response.Response;
import com.vms2.service.VisitorService;

@RestController
@RequestMapping("/api/visitor/")
public class VisitorController {

	@Autowired
	private VisitorService visitorService;

	@PostMapping("/add")
	public ResponseEntity<?> addVisitor(@RequestBody @Valid VisitormeetingDTO visitorDto) {

		Response<?> addVisitor = visitorService.addVisitor(visitorDto);
		if (addVisitor.getStatus() == HttpStatus.CREATED.value()) {
			return ResponseEntity.status(HttpStatus.CREATED).body(addVisitor);
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(addVisitor);
	}

	@GetMapping("/getall")
	public ResponseEntity<List<Visitor>> getAllVisitors() {
		List<Visitor> visitors = visitorService.getAllVisitors();
		return ResponseEntity.ok(visitors);
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<Visitor> getVisitorById(@PathVariable Integer id) {
		Visitor visitor = visitorService.getVisitorById(id);
		return ResponseEntity.ok(visitor);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Visitor> updateVisitor(@PathVariable Integer id,
			@RequestBody @Valid VisitormeetingDTO visitorDto) {
		Visitor updatedVisitor = visitorService.updateVisitor(id, visitorDto);
		return ResponseEntity.ok(updatedVisitor);
	}

	// add Visitor By Company User//

	@PostMapping("/add/byuser")
	public ResponseEntity<?> addVisitorUser(@RequestBody VisitormeetingDTO visitormeetingDTO) {

		Response<?> addVisitorByuser = visitorService.addVisitorByuser(visitormeetingDTO);
		if (addVisitorByuser.getStatus() == HttpStatus.OK.value()) {
			return ResponseEntity.ok(addVisitorByuser);
		}
		return new ResponseEntity<>(addVisitorByuser, HttpStatus.BAD_REQUEST);

	}

	@GetMapping("/checkout")
	public ResponseEntity<?> checkOut(@RequestParam("phone") String phone) {

		Response<?> updateCheckoutStatus = visitorService.updateCheckoutStatus(phone);

		if(updateCheckoutStatus.getStatus()==HttpStatus.OK.value())
		{
		return ResponseEntity.ok(updateCheckoutStatus);
		}
		else {
			return new ResponseEntity<>(updateCheckoutStatus,HttpStatus.BAD_REQUEST);
		}
		
	}

}
