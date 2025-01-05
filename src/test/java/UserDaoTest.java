import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import com.user.dao.UserDao;
import com.user.model.User;

public class UserDaoTest {
    private UserDao userDao;
    private User testUser;

    @BeforeEach
    void setUp() {
        userDao = new UserDao();
        
        // Create test user
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
        try {
            userDao.insertUser(testUser);
            
            // Verify user was inserted by trying to retrieve it
            User retrievedUser = userDao.selectUserByEmailAndPassword(testUser.getEmail(), testUser.getPassword());
            assertNotNull(retrievedUser);
            assertEquals(testUser.getUsername(), retrievedUser.getUsername());
            assertEquals(testUser.getEmail(), retrievedUser.getEmail());
        } catch (SQLException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    void testSelectUserByEmailAndPassword() {
        try {
            // First insert a user
            userDao.insertUser(testUser);
            
            // Try to retrieve the user
            User retrievedUser = userDao.selectUserByEmailAndPassword(testUser.getEmail(), testUser.getPassword());
            assertNotNull(retrievedUser);
            assertEquals(testUser.getEmail(), retrievedUser.getEmail());
            assertEquals(testUser.getPassword(), retrievedUser.getPassword());
        } catch (SQLException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    void testSelectAllUsers() {
        try {
            // First insert a test user
            userDao.insertUser(testUser);
            
            // Get all users
            List<User> users = userDao.selectAllUsers();
            assertNotNull(users);
            assertFalse(users.isEmpty());
            
            // Verify our test user is in the list
            boolean found = users.stream()
                .anyMatch(user -> user.getEmail().equals(testUser.getEmail()));
            assertTrue(found);
        } catch (SQLException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    void testUpdateUser() {
        try {
            // First insert a user
            userDao.insertUser(testUser);
            
            // Get the user to get their ID
            User insertedUser = userDao.selectUserByEmailAndPassword(testUser.getEmail(), testUser.getPassword());
            assertNotNull(insertedUser);
            
            // Update user details
            insertedUser.setName("Updated Name");
            insertedUser.setCountry("Updated Country");
            
            boolean updated = userDao.updateUser(insertedUser);
            assertTrue(updated);
            
            // Verify the update
            User updatedUser = userDao.selectUserByEmailAndPassword(testUser.getEmail(), testUser.getPassword());
            assertEquals("Updated Name", updatedUser.getName());
            assertEquals("Updated Country", updatedUser.getCountry());
        } catch (SQLException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    void testDeleteUser() {
        try {
            // First insert a user
            userDao.insertUser(testUser);
            
            // Get the user to get their ID
            User insertedUser = userDao.selectUserByEmailAndPassword(testUser.getEmail(), testUser.getPassword());
            assertNotNull(insertedUser);
            
            // Delete the user
            boolean deleted = userDao.deleteuser(insertedUser.getUser_id());
            assertTrue(deleted);
            
            // Verify the deletion
            User deletedUser = userDao.selectUserByEmailAndPassword(testUser.getEmail(), testUser.getPassword());
            assertNull(deletedUser);
        } catch (SQLException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    void testInvalidEmailFormat() {
        User invalidUser = new User();
        invalidUser.setEmail("invalid-email");
        invalidUser.setPassword("Test@123");
        
        assertThrows(IllegalArgumentException.class, () -> {
            userDao.insertUser(invalidUser);
        });
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Clean up test data
		User user = userDao.selectUserByEmailAndPassword(testUser.getEmail(), testUser.getPassword());
		if (user != null) {
		    userDao.deleteuser(user.getUser_id());
		}
    }
}
