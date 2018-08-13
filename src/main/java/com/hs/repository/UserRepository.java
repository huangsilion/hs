package com.hs.repository;

import com.hs.model.User;

public interface UserRepository {
	public User getUser(String id,String password);
	public boolean updateUser(User user);
	public boolean createUser(String id,String password);
}
