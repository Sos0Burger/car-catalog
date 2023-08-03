package com.sosoburger.carcatalog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AlreadyExists extends Exception{
    private String message;
}
