package br.com.lalsberg.coffee.club;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Clubs extends JpaRepository<Club, Long> {

	List<Club> findByOwnerIdOrMembersUserId(long ownerId, long memberId);

}
