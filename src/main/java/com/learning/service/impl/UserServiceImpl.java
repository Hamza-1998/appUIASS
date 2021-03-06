package com.learning.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.learning.dao.RoleRepository;
import com.learning.dao.UserRepository;
import com.learning.dao.UserRepositorySearchCriteria;
import com.learning.dto.UserDTO;
import com.learning.exceptions.BusinessException;
import com.learning.model.Role;
import com.learning.model.User;
import com.learning.model.base.ConstantBase;
import com.learning.model.base.Demande;
import com.learning.model.base.PartialList;
import com.learning.security.services.UserDetailsImpl;
import com.learning.service.RoleService;
import com.learning.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private RoleService roleService;


	@Autowired
	private UserRepositorySearchCriteria userRepositorySearchCriteria;


	

	@Override
	public UserDTO saveU(UserDTO userDTO) throws BusinessException {
		// id=5;
		Optional<User> user = userRepository.findByEmail(userDTO.getEmail());
		User existingUser = user.isPresent() ? user.get() : null;
		if (userDTO.getId() != null
				&& (existingUser == null || (existingUser != null && userDTO.getId() == existingUser.getId()))) {
			existingUser = existingUser != null ? existingUser : userRepository.findById(userDTO.getId()).get();
			return updateUser(userDTO, existingUser);
		} else {
			if (existingUser == null) {

				return saveUser(userDTO, existingUser);
			} else {
				throw new BusinessException(ConstantBase.USER_EXIST);
			}
		}
	}

	private UserDTO updateUser(UserDTO userDTO, User existingUser) throws BusinessException {
		User user = convertDTOtoModel(userDTO);
		user.setPassword(existingUser.getPassword());

		user = userRepository.saveAndFlush(user);

		return convertModelToDTO(user);
	}

	private UserDTO saveUser(UserDTO userDTO, User existingUser) throws BusinessException {

		User user = convertDTOtoModel(userDTO);
		user.setPassword(passwordEncoder.encode("Afak@1234"));
		user = userRepository.saveAndFlush(user);

		return userDTO;
	}

	@Override
	public UserDTO findById(long idOut) {
		Optional<User> optional = userRepository.findById(idOut);

		if (optional.isPresent()) {
			User user = optional.get();
			return convertModelToDTO(user);
		}
		return null;
	}

	@Override
	public void delete(User t) {
		// TODO Auto-generated method stub

	}

	@Override
	public PartialList<UserDTO> findByCriteres(Demande<UserDTO> demande) {
		List<User> users = userRepositorySearchCriteria.findByCriteres(demande);
		Long count = userRepositorySearchCriteria.countByCriteres(demande);
		return new PartialList<UserDTO>(count, convertEntitiesToDtos(users));
	}

	@Override
	public User convertDTOtoModel(UserDTO userDTO) {
		User user = new User();
		user.setId(userDTO.getId());
		user.setEmail(userDTO.getEmail());
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setPhone(userDTO.getPhone());
		user.setPassword(userDTO.getPassword());
		if (userDTO.getRefRole() != null) {
			user.setRefRole(roleService.convertDTOtoModel(userDTO.getRefRole()));
		}
		

		return user;
	}

	@Override
	public UserDTO convertModelToDTO(User user) {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setFirstName(user.getFirstName());
		userDTO.setLastName(user.getLastName());
		userDTO.setEmail(user.getEmail());
		userDTO.setPhone(user.getPhone());
		// userDTO.setPassword(user.getPassword());
	
		Role role = user.getRefRole();
	
		if (role != null) {
			userDTO.setRefRole(roleService.convertModelToDTO(role));
		}

		userDTO.setCreatedAt(user.getCreatedAt());
		userDTO.setUpdatedAt(user.getUpdatedAt());

		return userDTO;
	}

	@Override
	public PartialList<UserDTO> convertToListDTO(PartialList<User> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserDTO> convertEntitiesToDtos(List<User> list) {
		List<UserDTO> users = new ArrayList<UserDTO>();

		for (User user : list) {
			users.add(convertModelToDTO(user));
		}
		return users;
	}

	@Override
	public List<User> convertDtosToEntities(List<UserDTO> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteById(Long id) {
		userRepository.deleteById(id);
	}



	@Override
	public User convertDTOtoModelWithOutRelation(UserDTO userDTO) {
		User user = new User();
		user.setId(userDTO.getId());
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setIsOffline(false);

		return user;
	}

	@Override
	public UserDTO convertModelToDTOWithOutRelation(User user) {

		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setFirstName(user.getFirstName());
		userDTO.setLastName(user.getLastName());
		userDTO.setEmail(user.getEmail());
		userDTO.setLastName(user.getLastName());
		userDTO.setCreatedAt(user.getCreatedAt());
		userDTO.setUpdatedAt(user.getUpdatedAt());

		return userDTO;
	}

	@Override
	public List<UserDTO> convertEntitiesToDtosWithOutRelation(List<User> list) {
		List<UserDTO> users = new ArrayList<UserDTO>();

		for (User user : list) {
			users.add(convertModelToDTOWithOutRelation(user));
		}
		return users;
	}

	@Override
	public List<User> convertDtosToEntitiesWithOutRelation(List<UserDTO> list) {
		List<User> users = new ArrayList<User>();

		for (UserDTO user : list) {
			users.add(convertDTOtoModel(user));
		}
		return users;
	}

	
	@Override
	public UserDTO convertFromUserDetailsToDTO(UserDetailsImpl userDetail, String token) {
		UserDTO userDTO = findById(userDetail.getId());
		userDTO.setToken(token);
		return userDTO;
	}

	@Override
	public UserDTO save(UserDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public List<UserDTO> findByNameContainingByExam(String name, Long idExam) {

		return convertEntitiesToDtosWithOutRelation(userRepository.findByNameContainingByExam(name, idExam));
	}

	
	@Override
	public UserDetailsImpl getUserPrincipal() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetailsImpl userPrinciPal;
		if (principal instanceof UserDetailsImpl) {
			userPrinciPal = ((UserDetailsImpl) principal);
			return userPrinciPal;
		}
		return null;
	}





}
