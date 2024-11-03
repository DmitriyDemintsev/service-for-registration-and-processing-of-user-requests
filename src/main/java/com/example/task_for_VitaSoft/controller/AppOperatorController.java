package com.example.task_for_VitaSoft.controller;

import com.example.task_for_VitaSoft.dto.AppDto;
import com.example.task_for_VitaSoft.mapper.AppMapper;
import com.example.task_for_VitaSoft.service.AppService;
import com.example.task_for_VitaSoft.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Component
@RestController
@RequiredArgsConstructor
@RequestMapping("/operators/{userId}/applications")
public class AppOperatorController {

    private final AppService appService;
    private final CurrentUserService currentUserService;

    /**
     * оператор может просматривать все отправленные на рассмотрение заявки
     * с сортировкой по дате ASC/DESC и пагинацией по 5 элементов
     */
    @GetMapping()
    @Secured({"OPERATOR"})
    public List<AppDto> getListsApplications(@RequestParam(value = "direction", defaultValue = "INCREASING")
                                             String direct,
                                             @RequestParam(required = false, defaultValue = "0") int page) {
        final long userId = currentUserService.getCurrentUser().getUserId();
        log.debug("Viewing applications sorted by date");
        return AppMapper.toAppDtoList(appService.viewAllApplications(userId, AppMapper.toDirect(direct), page));
    }

    /**
     * оператор может просматривать отправленные заявки только конкретного пользователя по его имени/части имени
     * с сортировкой по дате ASC/DESC и пагинацией по 5 элементов
     */
    @GetMapping("/search")
    @Secured({"OPERATOR"})
    public List<AppDto> getListsApplicationsByName(@RequestParam String name,
                                                   @RequestParam(value = "direction", defaultValue = "INCREASING")
                                                   String direct,
                                                   @RequestParam(required = false, defaultValue = "0") int page) {
        final long userId = currentUserService.getCurrentUser().getUserId();
        log.debug("Viewing applications of a specific user sorted by date");
        return AppMapper.toAppDtoList(appService.viewUsersSubmittedApplications(userId, name,
                AppMapper.toDirect(direct), page));
    }

    /**
     * оператор может принимать/отклонять заявки
     */
    @PatchMapping("/{appId}")
    @Secured({"OPERATOR"})
    public AppDto changeStatusOfApp(@PathVariable("appId") long appId,
                                    @RequestParam(value = "status") String status) {
        final long userId = currentUserService.getCurrentUser().getUserId();
        log.debug("Acceptance/rejection of applications by the operator");
        return AppMapper.toApplicationsDto(appService.changeStatusOfApp(userId, appId, AppMapper.toStatus(status)));
    }
}
