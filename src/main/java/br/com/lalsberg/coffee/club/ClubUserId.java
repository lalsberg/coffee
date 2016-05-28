package br.com.lalsberg.coffee.club;

import java.io.Serializable;

public class ClubUserId implements Serializable {

	private static final long serialVersionUID = -9072838720564049038L;

	private int club;
	private int user;

	public long getClub() {
		return club;
	}

	public void setClub(int club) {
		this.club = club;
	}

	public int getUser() {
		return user;
	}

	public void setUser(int user) {
		this.user = user;
	}

	@Override
    public int hashCode() {
        return club + user;
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
