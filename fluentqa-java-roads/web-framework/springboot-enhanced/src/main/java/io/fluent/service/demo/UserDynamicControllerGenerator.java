package io.fluent.service.demo;

import io.fluent.service.demo.dto.User;
import io.fluent.service.demo.service.UserService;
import io.fluent.service.demo.spec.UserControllerMethodsImplementation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.implementation.MethodDelegation;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Modifier;
import java.util.List;

/**
 * Generates rest controller for {@link User} at runtime:
 * {@code
 *
 * package com.example.dynamic.controller.demo;
 *
 * import com.example.dynamic.controller.demo.UserDynamicControllerGenerator.UserControllerMethodsImplementation;
 *
 * import java.util.List;
 *
 * import org.springframework.web.bind.annotation.PathVariable;
 * import org.springframework.web.bind.annotation.RequestParam;
 * import org.springframework.web.bind.annotation.RestController;
 *
 * @RestController
 * public class UserDynamicController {
 *     public List getAll() {
 *         return UserControllerMethodsImplementation.getAll();
 *     }
 *
 *     public User getById(@RequestParam(name = "id") Long id) {
 *         return UserControllerMethodsImplementation.get(id);
 *     }
 *
 *     public void save(@RequestBody User user) {
 *         UserControllerMethodsImplementation.save(user);
 *     }
 *
 *     public void update(@RequestParam(name = "id") Long id, @RequestBody User user) {
 *         UserControllerMethodsImplementation.update(id, user);
 *     }
 *
 *     public void updateNickName(@RequestParam(name = "id") Long id, @RequestParam(name = "nickName") String nickName) {
 *         UserControllerMethodsImplementation.updateNickName(id, nickName);
 *     }
 *
 *     public void delete(@PathVariable(name = "id") Long id) {
 *         UserControllerMethodsImplementation.delete(id);
 *     }
 *
 *     public UserDynamicController() {
 *     }
 * }
 *
 * }
 */
@Slf4j
@Component
@DependsOn(value = {"userService"})
@RequiredArgsConstructor
public class UserDynamicControllerGenerator {

    private final ApplicationContext applicationContext;

    @SneakyThrows
    public Object generateUserController() {
        // init static implementation to avoid reflection usage

        // creates builder with unique `class` name and `@RestController` annotation
        Object userController = new ByteBuddy()
                .subclass(Object.class)
                .name("UserDynamicController")
                .annotateType(AnnotationDescription.Builder
                        .ofType(RestController.class) // don't use `request` mapping here
                        .build())

                // do some `if/else` business logic here...

                /**
                 * Delegates to:
                 * {@link UserControllerMethodsImplementation#getAll()}
                 */
                .defineMethod("getAll", List.class, Modifier.PUBLIC)
                .intercept(MethodDelegation.to(UserControllerMethodsImplementation.class))

                /**
                 * Delegates to:
                 * {@link UserControllerMethodsImplementation#get(Long)}
                 */
                .defineMethod("getById", User.class, Modifier.PUBLIC)
                .withParameter(Long.class, "id")
                .annotateParameter(AnnotationDescription.Builder
                        .ofType(RequestParam.class)
                        .define("name", "id")
                        .build())
                .intercept(MethodDelegation.to(UserControllerMethodsImplementation.class))

                /**
                 * Delegates to:
                 * {@link UserControllerMethodsImplementation#save(User)}
                 */
                .defineMethod("save", void.class, Modifier.PUBLIC)
                .withParameter(User.class, "user")
                .annotateParameter(AnnotationDescription.Builder
                        .ofType(RequestBody.class)
                        .build())
                .intercept(MethodDelegation.to(UserControllerMethodsImplementation.class))

                /**
                 * Delegates to:
                 * {@link UserControllerMethodsImplementation#update(Long, User)}
                 */
                .defineMethod("update", void.class, Modifier.PUBLIC)
                .withParameter(Long.class, "id")
                .annotateParameter(AnnotationDescription.Builder
                        .ofType(RequestParam.class)
                        .define("name", "id")
                        .build())
                .withParameter(User.class, "user")
                .annotateParameter(AnnotationDescription.Builder
                        .ofType(RequestBody.class)
                        .build())
                .intercept(MethodDelegation.to(UserControllerMethodsImplementation.class))

                /**
                 * Delegates to:
                 * {@link UserControllerMethodsImplementation#updateNickName(Long, String)}
                 */
                .defineMethod("updateNickName", void.class, Modifier.PUBLIC)
                .withParameter(Long.class, "id")
                .annotateParameter(AnnotationDescription.Builder
                        .ofType(RequestParam.class)
                        .define("name", "id")
                        .build())
                .withParameter(String.class, "nickName")
                .annotateParameter(AnnotationDescription.Builder
                        .ofType(RequestParam.class)
                        .define("name", "nickName")
                        .build())
                .intercept(MethodDelegation.to(UserControllerMethodsImplementation.class))

                /**
                 * Delegates to:
                 * {@link UserControllerMethodsImplementation#delete(Long)}
                 */
                .defineMethod("delete", void.class, Modifier.PUBLIC)
                .withParameter(Long.class, "id")
                .annotateParameter(AnnotationDescription.Builder
                        .ofType(PathVariable.class)
                        .define("name", "id")
                        .build())
                .intercept(MethodDelegation.to(UserControllerMethodsImplementation.class))

                // creates instance of generated `controller`
                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .newInstance();

        log.info("Generated `DynamicController`: {}", userController.getClass().getName());
        return userController;
    }

    /**
     * Methods implementation for {@link User} controller by {@link UserService}
     */

}
