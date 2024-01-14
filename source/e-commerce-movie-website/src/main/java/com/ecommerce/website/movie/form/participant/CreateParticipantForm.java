package com.ecommerce.website.movie.form.participant;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateParticipantForm {
    @NotEmpty(message = "Participant name can not be null!")
    private String name;
    @NotEmpty
    private String image;
    @NotNull
    private Integer kind;
    private String filmCharacter;
}
