package com.example.task_for_VitaSoft.controller;

import com.example.task_for_VitaSoft.dto.AppCreateDto;
import com.example.task_for_VitaSoft.dto.AppDto;
import com.example.task_for_VitaSoft.dto.AppUpdateDto;
import com.example.task_for_VitaSoft.mapper.AppMapper;
import com.example.task_for_VitaSoft.service.AppService;
import com.example.task_for_VitaSoft.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Component
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class AppPrivateController {

    private final AppService appService;
    private final CurrentUserService currentUserService;

    /**
     * пользователь может создавать заявки
     */
    @PostMapping("/applications")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"USER"})
    public AppDto createApp(@Validated @RequestBody AppCreateDto appCreateDto) {
        log.debug("Creating an application by the user");
        final long userId = currentUserService.getCurrentUser().getUserId();
        return AppMapper.toApplicationsDto(appService.createApp(userId, AppMapper.toCreateApp(appCreateDto)));
    }

    /**
     * пользователь может просматривать созданные им заявки с сортировкой
     * по дате ASC/DESC и пагинацией по 5 элементов
     */
    @GetMapping("/applications")
    @Secured({"USER"})
    public List<AppDto> getListsApplications(@RequestParam(value = "direction", defaultValue = "INCREASING")
                                             String direct,
                                             @RequestParam(required = false, defaultValue = "0") int page) {
        log.debug("Viewing user requests sorted by date");
        final long userId = currentUserService.getCurrentUser().getUserId();
        return AppMapper.toAppDtoList(appService.getUserApplications(userId, AppMapper.toDirect(direct), page));
    }


    /**
     * пользователь может редактировать созданные им заявки в статусе "черновик"
     */
    @PutMapping("/applications/{appId}")
    @Secured({"USER"})
    public AppDto updateApp(@PathVariable("appId") long appId,
                            @Valid @RequestBody AppUpdateDto appUpdateDto) {
        log.debug("Editing the application by the user");
        final long userId = currentUserService.getCurrentUser().getUserId();
        return AppMapper.toApplicationsDto(appService.updateApp(userId, AppMapper.toUpdateApp(appUpdateDto, appId)));
    }


    /**
     * пользователь может отправлять заявки на рассмотрение оператору
     */
    @PatchMapping("/applications/{appId}")
    @Secured({"USER"})
    public AppDto sendApp(@PathVariable("appId") long appId) {
        log.debug("Sending an application for consideration");
        final long userId = currentUserService.getCurrentUser().getUserId();
        return AppMapper.toApplicationsDto(appService.sendApp(userId, appService.getAppById(appId)));
    }
}
