package com.basicauth.app.enums;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public enum Role {
    ADMIN,
    PERSONNEL,
    CHEF,
    EMPLOYE
}