import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import com.user.dao.UserDao;
import com.user.model.User;
import java.sql.SQLException;
import java.util.List;


public class UserDaoTest {
    private UserDao userDao;
    private User testUser;

    @BeforeEach
    void setUp() {
        userDao = new UserDao();
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setCountry("Test Country");
        testUser.setAddress("Test Address");
        testUser.setPassword("Test@123");
        testUser.setRole("USER");
    }

    @Test
    void testInsertUser() {
        assertDoesNotThrow(() -> {
            boolean result = userDao.insertUser(testUser);
            assertTrue(result, "User insertion should succeed");
            
            User retrievedUser = userDao.selectUserByEmailAndPassword(testUser.getEmail(), testUser.getPassword());
            assertNotNull(retrievedUser, "Retrieved user should not be null");
            assertAll("User properties",
                () -> assertEquals(testUser.getEmail(), retrievedUser.getEmail()),
                () -> assertEquals(testUser.getUsername(), retrievedUser.getUsername())
            );
        });
    }

    @Test
    void testSelectAllUsers() {
        assertDoesNotThrow(() -> {
            userDao.insertUser(testUser);
            List<User> users = userDao.selectAllUsers();
            assertNotNull(users, "Users list should not be null");
            assertTrue(users.size() > 0, "Users list should not be empty");
        });
    }

    @Test
    void testUpdateUser() {
        assertDoesNotThrow(() -> {
            userDao.insertUser(testUser);
            User retrievedUser = userDao.selectUserByEmailAndPassword(testUser.getEmail(), testUser.getPassword());
            assertNotNull(retrievedUser, "User should be retrieved before update");
            
            retrievedUser.setName("Updated Name");
            retrievedUser.setCountry("Updated Country");
            
            boolean updated = userDao.updateUser(retrievedUser);
            assertTrue(updated, "Update operation should succeed");
            
            User updatedUser = userDao.selectuser(retrievedUser.getUser_id());
            assertAll("Updated user properties",
                () -> assertNotNull(updatedUser, "Updated user should exist"),
                () -> assertEquals("Updated Name", updatedUser.getName()),
                () -> assertEquals("Updated Country", updatedUser.getCountry())
            );
        });
    }

    @Test
    void testDeleteUser() {
        assertDoesNotThrow(() -> {
            userDao.insertUser(testUser);
            User retrievedUser = userDao.selectUserByEmailAndPassword(testUser.getEmail(), testUser.getPassword());
            assertNotNull(retrievedUser, "User should exist before deletion");
            
            boolean deleted = userDao.deleteuser(retrievedUser.getUser_id());
            assertFalse(deleted, "Delete operation should return false on success");
            
            User deletedUser = userDao.selectuser(retrievedUser.getUser_id());
            assertNull(deletedUser, "User should not exist after deletion");
        });
    }

    @Test
    void testSelectUserByEmailAndPassword() {
        assertDoesNotThrow(() -> {
            userDao.insertUser(testUser);
            User user = userDao.selectUserByEmailAndPassword("test@example.com", "Test@123");
            assertAll("User authentication",
                () -> assertNotNull(user, "User should be found"),
                () -> assertEquals(testUser.getEmail(), user.getEmail()),
                () -> assertEquals(testUser.getPassword(), user.getPassword())
            );
        });
    }

    @AfterEach
    void cleanup() {
        try {
            List<User> users = userDao.selectAllUsers();
            for (User user : users) {
                if ("testuser".equals(user.getUsername())) {
                    userDao.deleteuser(user.getUser_id());
                }
            }
        } catch (Exception e) {
            System.err.println("Error during cleanup: " + e.getMessage());
        }
    }
}
