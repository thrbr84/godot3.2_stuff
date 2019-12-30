extends Node2D

var google = null

func _ready():
	if OS.get_name() == "Android":
		if Engine.has_singleton("GooglePlay"):
			google = Engine.get_singleton("GooglePlay")
			google.init(get_instance_id()) # use get_instance_id () for Godot 3.X
		
	"""
	# Google play achievements
	google.unlock_achievement("achievementID") # unlock achievement;
	google.increse_achievement("achievementID", int(n)) # increse achievements by step.
	google.show_achievements() # show achievements;
	
	#Google play Leaderboards
	google.submit_leaderboard(int(score), "leaderboardID") # submit score to leaderboard
	google.show_leaderboard("leaderboardID") # show leaderboard
	google.show_leaderboards() # show all available leaderboard
"""

func _receive_message(from, key, data):
	if from == "GooglePlay":
		print("Key: ", key, " Data: ", data)


func _on_Button_toggled(pressed):
	if google != null:
		if pressed: google.login()
		else: google.logout()
