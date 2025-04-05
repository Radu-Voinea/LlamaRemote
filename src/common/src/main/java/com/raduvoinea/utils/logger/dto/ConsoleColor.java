package com.raduvoinea.utils.logger.dto;

import org.jetbrains.annotations.NotNull;


public enum ConsoleColor {
	RESET("\033[0m"),
	BLACK("\033[30m"),
	DARK_RED("\033[31m"),
	DARK_GREEN("\033[32m"),
	DARK_YELLOW("\033[33m"),
	DARK_BLUE("\033[34m"),
	DARK_PURPLE("\033[35m"),
	DARK_CYAN("\033[36m"),
	DARK_WHITE("\033[37m"),
	BRIGHT_BLACK("\033[90m"),
	BRIGHT_RED("\033[91m"),
	BRIGHT_GREEN("\033[92m"),
	BRIGHT_YELLOW("\033[93m"),
	BRIGHT_BLUE("\033[94m"),
	BRIGHT_MAGENTA("\033[95m"),
	BRIGHT_CYAN("\033[96m"),
	WHITE("\033[97m");

	private final @NotNull String code;

	ConsoleColor(@NotNull String code) {
		this.code = code;
	}

	@SuppressWarnings("unused")
	public static @NotNull String clearString(@NotNull String log) {
		for (ConsoleColor value : ConsoleColor.values()) {
			log = log.replace(value.toString(), "");
		}
		return log;
	}

	@Override
	public @NotNull String toString() {
		return code;
	}
}