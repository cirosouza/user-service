package br.edu.infnet.user_service.service;

import br.edu.infnet.user_service.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    public List<User> getAll();

    public User create(User user);

    public Optional<User> getById(int id);

    public User update(int id, User userToUpdate);

    public void delete(int id);

    public void deleteAll();

}
