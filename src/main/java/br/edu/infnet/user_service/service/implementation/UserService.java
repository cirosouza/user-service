package br.edu.infnet.user_service.service.implementation;

import br.edu.infnet.user_service.exception.ResourceNotFoundException;
import br.edu.infnet.user_service.model.User;
import br.edu.infnet.user_service.repository.UserRepository;
import br.edu.infnet.user_service.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public User update(int id, User userToUpdate) {
        Optional<User> existingUserOpt = getById(id);

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            existingUser.setUsername(userToUpdate.getUsername());
            existingUser.setEmail(userToUpdate.getEmail());
            return userRepository.save(existingUser);
        }
        else{
            throw new ResourceNotFoundException("Usuário não encontrado.");
        }

    }

    @Override
    public void delete(int id) {
        Optional<User> userToDelete = getById(id);

        if (userToDelete.isEmpty()) {
            throw new ResourceNotFoundException("Não encontrado usuario com id " + id);
        }
        userRepository.delete(userToDelete.get());
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }
}
