package user;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test");
        jdbi.installPlugin(new SqlObjectPlugin());

        try (Handle handle = jdbi.open()) {

            UserDao dao = handle.attach(UserDao.class);
            dao.createTable();

            User exampleuser1 = User.builder()
                    .username("mrexample")
                    .password("password")
                    .name("Johnny Example")
                    .email("example@examplemail.com")
                    .gender(User.Gender.MALE)
                    .dob(LocalDate.parse("1999-10-10"))
                    .enabled(true)
                    .build();

            User exampleuser2 = User.builder()
                    .username("bob")
                    .password("1234")
                    .name("Bob Johnson")
                    .email("bob@bobmail.com")
                    .gender(User.Gender.MALE)
                    .dob(LocalDate.parse("1979-11-11"))
                    .enabled(true)
                    .build();

            dao.insert(exampleuser1);
            dao.insert(exampleuser2);

            System.out.println("User with id 1:");
            System.out.println(dao.findById(1).get());
            System.out.println("User with username \"bob\":");
            System.out.println(dao.findByUsername("bob").get());
            System.out.println("All users:");
            dao.list().stream().forEach(System.out::println);
            System.out.println("Deleting user 2");
            dao.delete(exampleuser2);
            System.out.println("All users again:");
            dao.list().stream().forEach(System.out::println);
        }
    }
}