package com.example.task_for_VitaSoft.controller;

import com.example.task_for_VitaSoft.dto.AppDto;
import com.example.task_for_VitaSoft.mapper.AppMapper;
import com.example.task_for_VitaSoft.model.User;
//import com.example.task_for_VitaSoft.security.UserDetailsImpl;
import com.example.task_for_VitaSoft.service.AppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    /**
     * оператор может просматривать все отправленные на рассмотрение заявки
     * с сортировкой по дате ASC/DESC и пагинацией по 5 элементов
     */
//    @GetMapping()
//    public List<AppDto> getListsApplications(@PathVariable Long userId,
//                                             @RequestParam(value = "direction", defaultValue = "INCREASING") String direct,
//                                             @RequestParam(required = false, defaultValue = "0") int page) {
//
//        return AppMapper.toAppDtoList(appService.viewAllApplications(userId, AppMapper.toDirect(direct), page));
//    }

    @GetMapping()
    @Secured({"OPERATOR"})
    public List<AppDto> getListsApplications(@PathVariable Long userId,
                                             @RequestParam(value = "direction", defaultValue = "INCREASING") String direct,
                                             @RequestParam(required = false, defaultValue = "0") int page) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentPrincipalName = authentication.getName();
//        SecurityContext userDetails = SecurityContextHolder.getContext();
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("КОНТРОЛЬ_1 getName() " + authentication.getName());
        System.out.println("КОНТРОЛЬ_2 getPrincipal() " + authentication.getPrincipal());
        System.out.println("КОНТРОЛЬ_3 getAuthorities() " + authentication.getAuthorities());
        System.out.println("КОНТРОЛЬ_4 getCredentials() " + authentication.getCredentials());
//        System.out.println("КОНТРОЛЬ_2 " + userDetails.getAuthentication());
//        System.out.println("КОНТРОЛЬ_3 " + userDetails.getAuthentication().getPrincipal());
        //.getAuthentication();
//                .getPrincipal();
//        User requestor = userDetails.getUser();
//        User requestor = (User) userDetails.getAuthorities();
//        System.out.println("КОНТРОЛЬ_2 " + requestor);

        return AppMapper.toAppDtoList(appService.viewAllApplications(userId, AppMapper.toDirect(direct), page));
    }

    /**
     * оператор может просматривать отправленные заявки только конкретного пользователя по его имени/части имени
     * с сортировкой по дате ASC/DESC и пагинацией по 5 элементов
     */
    @GetMapping("/search")
    public List<AppDto> getListsApplicationsByName(@PathVariable Long userId,
                                                   @RequestParam String name,
                                                   @RequestParam(value = "direction", defaultValue = "INCREASING") String direct,
                                                   @RequestParam(required = false, defaultValue = "0") int page) {
        return AppMapper.toAppDtoList(appService.viewUsersSubmittedApplications(userId, name, AppMapper.toDirect(direct), page));
    }

    /**
     * оператор может принимать/отклонять заявки
     */
    @PatchMapping("/{appId}")
    public AppDto changeStatusOfApp(@PathVariable("userId") long userId,
                                    @PathVariable("appId") long appId,
                                    @RequestParam(value = "status") String status) {
        return AppMapper.toApplicationsDto(appService.changeStatusOfApp(userId, appId, AppMapper.toStatus(status)));
    }
}
