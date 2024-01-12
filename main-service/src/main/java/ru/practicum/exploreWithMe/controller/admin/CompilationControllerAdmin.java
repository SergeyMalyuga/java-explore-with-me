package ru.practicum.exploreWithMe.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.dto.CompilationDto;
import ru.practicum.exploreWithMe.dto.NewCompilationDto;
import ru.practicum.exploreWithMe.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
public class CompilationControllerAdmin {

    @Autowired
    private CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto postCompilation(@Valid @RequestBody NewCompilationDto newCompilation) {
        return compilationService.addCompilation(newCompilation);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable(value = "compId") int compId,
                                            @Valid @RequestBody NewCompilationDto newCompilation) {
        return compilationService.updateCompilation(compId, newCompilation);
    }

    @DeleteMapping("/compId")
    public void deleteCompilation(@PathVariable(value = "compId") int compId) {
        compilationService.removeCompilation(compId);
    }
}
