package com.xgame.common.viewmodels;

import java.util.List;

public class ProfileViewModel {
	
	private String userNickname;
	private List<MatchHistoryViewModel> matchHistory;
	
	public ProfileViewModel(String userNickname, List<MatchHistoryViewModel> matchHistory) {
		this.userNickname = userNickname;
		this.matchHistory = matchHistory;
	}
	
	public String getUser() {
		return this.userNickname;
	}
	
	public void setUser(String userNickname) {
		this.userNickname = userNickname;
	}
	
	public List<MatchHistoryViewModel> getMatchHistory(){
		return matchHistory;
	}
	
	public void setMatchHistory(List<MatchHistoryViewModel> matchHistory) {
		this.matchHistory = matchHistory;
	}
}
