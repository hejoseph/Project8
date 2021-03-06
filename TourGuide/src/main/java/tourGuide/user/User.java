package tourGuide.user;

import tourGuide.model.Provider;
import tourGuide.model.VisitedLocation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class User {
	private final UUID userId;
	private final String userName;
	private String phoneNumber;
	private String emailAddress;
	private Date latestLocationTimestamp;
	private List<VisitedLocation> visitedLocations = new ArrayList<>();
	private List<UserReward> userRewards = new ArrayList<>();
	private UserPreferences userPreferences = new UserPreferences();
	private List<Provider> tripDeals = new ArrayList<>();

	private String debug;

	private boolean rewardCalled;

	public User(UUID userId, String userName, String phoneNumber, String emailAddress) {
		this.userId = userId;
		this.userName = userName;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.rewardCalled = false;
		this.debug = "";
	}

	public synchronized void addDebug(String value){
		this.debug += value;
	}

	public String getDebug(){
		return this.debug;
	}

	public UUID getUserId() {
		return userId;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setLatestLocationTimestamp(Date latestLocationTimestamp) {
		this.latestLocationTimestamp = latestLocationTimestamp;
	}
	
	public Date getLatestLocationTimestamp() {
		return latestLocationTimestamp;
	}

	public boolean isRewardCalled() {
		return rewardCalled;
	}

	public void setRewardCalled(boolean rewardCalled) {
		this.rewardCalled = rewardCalled;
	}

	public void addToVisitedLocations(VisitedLocation visitedLocation) {
		visitedLocations.add(visitedLocation);
	}
	
	public List<VisitedLocation> getVisitedLocations() {
		return visitedLocations;
	}
	
	public void clearVisitedLocations() {
		visitedLocations.clear();
	}

	public boolean hasRewardForAttraction(String attractionName){
		boolean hasReward = false;

		for(int i=0; i<this.getUserRewards().size();i++){
			UserReward reward = this.getUserRewards().get(i);
			if(reward
					.attraction
					.attractionName
					.equals(attractionName)){
				return true;
			}
		}

		return hasReward;
	}

	public void addUserReward(UserReward userReward) {
		if(!this.hasRewardForAttraction(userReward.attraction.attractionName)){
			userRewards.add(userReward);
		}
	}
	
	public List<UserReward> getUserRewards() {
		return userRewards;
	}
	
	public UserPreferences getUserPreferences() {
		return userPreferences;
	}
	
	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	public VisitedLocation getLastVisitedLocation() {
		return visitedLocations.get(visitedLocations.size() - 1);
	}
	
	public void setTripDeals(List<Provider> tripDeals) {
		this.tripDeals = tripDeals;
	}
	
	public List<Provider> getTripDeals() {
		return tripDeals;
	}

	@Override
	public String toString() {
		return "User{" +
				"userId=" + userId +
				", userName='" + userName + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", emailAddress='" + emailAddress + '\'' +
				", latestLocationTimestamp=" + latestLocationTimestamp +
				", visitedLocations=" + visitedLocations +
				", userRewards=" + userRewards +
				", userPreferences=" + userPreferences +
				", tripDeals=" + tripDeals +
				", debug='" + debug + '\'' +
				", rewardCalled=" + rewardCalled +
				'}';
	}
}
