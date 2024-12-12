package com.example.Backend.Entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@AllArgsConstructor
@Data
@DiscriminatorValue("Administration")
public class Administration extends Utilisateur {
}
