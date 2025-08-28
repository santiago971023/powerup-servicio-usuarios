//package co.com.powerup.usecase;
//
//import co.com.powerup.model.role.Role;
//import co.com.powerup.model.role.gateways.RoleRepository;
//import co.com.powerup.model.user.User;
//import co.com.powerup.model.user.gateways.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class UserUseCaseTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private RoleRepository roleRepository;
//
//    @InjectMocks
//    private UserUseCase userUseCase;
//
//    @Test
//    void shouldSaveAnUserSuccessfully() {
//
//        // GIVEN
//        User userWithOutRole = User.builder().email("user@test.com").build();
//        Role role = Role.builder().id(1L).name("SOLICITANTE").build();
//        User savedUser
//        // WHEN
//
//        // THEN
//
//    }
//
//}
