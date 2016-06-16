package br.com.lalsberg.coffee.club;

import java.io.Serializable;

public class ClubUserId implements Serializable {

	private static final long serialVersionUID = -9072838720564049038L;

	private long club;
	private long user;

	public long getClub() {
		return club;
	}

	public void setClub(long club) {
		this.club = club;
	}

	public long getUser() {
		return user;
	}

	public void setUser(long user) {
		this.user = user;
	}

	@Override
    public int hashCode() {
        return Long.valueOf(club).intValue() + Long.valueOf(user).intValue();
    }
 
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ClubUserId){
        	ClubUserId clubUserId = (ClubUserId) obj;
            return clubUserId.club == club && clubUserId.user == user;
        }
 
        return false;
    }

}
