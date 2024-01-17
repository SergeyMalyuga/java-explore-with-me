package ru.practicum.exploreWithMe.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.dto.CompilationDto;
import ru.practicum.exploreWithMe.dto.Marker;
import ru.practicum.exploreWithMe.dto.NewCompilationDto;
import ru.practicum.exploreWithMe.service.CompilationService;

import javax.validation.Valid;


@Validated
@RestController
@RequestMapping("/admin/compilations")
public class CompilationControllerAdmin {

    @Autowired
    private CompilationService compilationService;

    @PostMapping
    @Validated(Marker.OnCreate.class)
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto postCompilation(@Valid @RequestBody NewCompilationDto newCompilation) {
        return compilationService.addCompilation(newCompilation);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable(value = "compId") int compId,
                                            @Valid @RequestBody NewCompilationDto newCompilation) {
        return compilationService.updateCompilation(compId, newCompilation);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable(value = "compId") int compId) {
        compilationService.removeCompilation(compId);
    }
}
