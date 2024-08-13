package com.basicauth.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.basicauth.app.entity.UserProfile;
import com.basicauth.app.enums.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RegisterNewUserRepository extends JpaRepository<UserProfile,Long>{

	Optional<UserProfile> findByEmail(String email);
	List<UserProfile> findByRole(Role role);
	Optional<UserProfile> findById(Long id);

	List<UserProfile> findByChefHierarchiqueId(Long chefHierarchiqueId);
	@Query("SELECT COUNT(u) FROM UserProfile u WHERE u.chefHierarchique.id = :idchef")
	Long countPersonnelByChefHierarchique(@Param("idchef") Long idchef);
}
