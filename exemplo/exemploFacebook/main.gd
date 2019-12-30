extends Node2D

var fb = null

func _ready():
	if(Engine.has_singleton("GodotFacebook")):
		fb = Engine.get_singleton("GodotFacebook")
		fb.init("0000000000") #colocar o ID do seu APP do Facebook
		fb.setFacebookCallbackId(get_instance_id())

func login_success(token):
	print('Facebook login success: %s'%token)

func login_cancelled():
	print('Facebook login cancelled')

func login_failed(error):
	print('Facebook login failed: %s'%error)

func _on_Button_pressed():
	if fb != null:
		fb.login(["public_profile"])
