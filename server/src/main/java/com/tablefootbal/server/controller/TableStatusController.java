package com.tablefootbal.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TableStatusController
{
	@GetMapping("/")
	public String showTableStatusPage()
	{
		return "status";
	}
}
