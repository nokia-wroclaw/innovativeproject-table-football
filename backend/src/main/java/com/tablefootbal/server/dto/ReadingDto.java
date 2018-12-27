package com.tablefootbal.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadingDto
{
	double [] x;
	double [] y;
	double [] z;
	long timestamp;
}
