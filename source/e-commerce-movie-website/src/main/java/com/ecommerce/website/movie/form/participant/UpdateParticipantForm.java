package com.ecommerce.website.movie.form.participant;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
@Data
public class UpdateParticipantForm {
    @NotNull(message = "Participant id can not be null!")
    private Long id;
    @NotEmpty(message = "Participant name can not be null!")
    private String name;
    private String image;
    private Integer kind;
    private String filmCharacter;
}
