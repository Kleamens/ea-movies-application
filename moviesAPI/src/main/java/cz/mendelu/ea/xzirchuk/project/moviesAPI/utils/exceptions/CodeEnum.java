package cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CodeEnum {
    ALREADY_EXISTS("ALREADY_EXISTS"),
    NOT_FOUND("NOT_FOUND"),
    BAD_INPUT("BAD_INPUT");

    private final String label;
}
