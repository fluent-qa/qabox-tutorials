package io.fluent.service.demo.spec;

import io.fluent.service.demo.dto.User;
import io.fluent.service.demo.service.UserService;
import net.bytebuddy.implementation.bind.annotation.Argument;

import java.util.List;

public  class UserControllerMethodsImplementation {

        private static UserService userService;

        /**
         * Delegates to:
         * {@link UserService#save(User)}
         */
        public static List<User> getAll() {
            return userService.getAll();
        }

        /**
         * Delegates to:
         * {@link UserService#getById(Long)}
         */
        public static User get(@Argument(0) Long id) { // don't use primitive types
            return userService.getById(id);
        }

        /**
         * Delegates to:
         * {@link UserService#save(User)}
         */
        public static void save(@Argument(0) User user) {
            userService.save(user);
        }

        /**
         * Delegates to:
         * {@link UserService#update(Long, User)}
         */
        public static void update(@Argument(0) Long id, // don't use primitive types
                                  @Argument(1) User user) {
            userService.update(id, user);
        }

        /**
         * Delegates to:
         * {@link UserService#updateNickName(Long, String)}
         */
        public static void updateNickName(@Argument(0) Long id, // don't use primitive types
                                          @Argument(1) String nickName) {
            userService.updateNickName(id, nickName);
        }

        /**
         * Delegates to:
         * {@link UserService#delete(Long)}
         */
        public static void delete(@Argument(0) Long id) { // don't use primitive types
            userService.delete(id);
        }

    }
