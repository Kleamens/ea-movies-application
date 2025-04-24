package cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CodeEnum {
    ALREADY_EXISTS,
    NOT_FOUND,
    BAD_INPUT;

}
