package br.com.lalsberg.coffee.club;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface Clubs extends CrudRepository<Club, Long> {

	List<Club> findByOwnerIdOrMembersUserId(long ownerId, long memberId);

}
