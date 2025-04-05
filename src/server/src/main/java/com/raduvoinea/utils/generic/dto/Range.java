package com.raduvoinea.utils.generic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Range {

	private int min;
	private int max;

	public static Range exact(int n) {
		return new Range(n, n);
	}

	public static Range of(int min, int max) {
		return new Range(min, max);
	}

}