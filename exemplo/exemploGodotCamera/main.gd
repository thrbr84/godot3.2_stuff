extends Node2D

onready var nativeCamera = $picture/nativeCamera
var facing = false
var is_open = true

func _ready():
	OS.request_permissions()
	nativeCamera.connect("picture_taken", self, "on_picture_taken")
	
func _on_btnTakePicture_pressed():
	$picture/anim.play_backwards("open")
	if nativeCamera.camera:
		nativeCamera.take_picture()

func _on_btnOpenNativeCam_pressed():
	_reset()
	if nativeCamera.camera and !is_open:
		nativeCamera.camera.setImageSize(300) # width / height ascpect ratio
		nativeCamera.camera.setImageRotated(90) # rotation
		nativeCamera.camera.openCamera()

func _on_btnPreview_pressed():
	if !nativeCamera.camera:
		nativeCamera._initialize()
	
	yield(get_tree().create_timer(1), "timeout")
	if nativeCamera.camera: 
		is_open = true
		nativeCamera.set_view_visibilty(true)
		$picture/anim.play("open")

func on_picture_taken(error, image_texture, extras):
	if nativeCamera.camera:
		if error == nativeCamera.ERROR.NONE:
			$picture.texture = image_texture
			
	$picture/anim.play_backwards("open")
	is_open = false
	nativeCamera.set_view_visibilty(false)

func _reset():
	$picture.texture = ResourceLoader.load("res://camera.png")
	is_open = false
	nativeCamera.set_view_visibilty(false)

func _on_btnTakePicture3_pressed():
	if nativeCamera.camera:
		nativeCamera.set_camera_facing(facing)
		facing = !facing
		
		if facing:
			$picture/nativeCamera/btnTakePicture3.text = "B" #back
		else:
			$picture/nativeCamera/btnTakePicture3.text = "F" #front

func _on_btnTakePicture2_pressed():
	if nativeCamera.camera:
		$picture/anim.play_backwards("open")
		_reset()
