extends Node2D

var camera = null

func _ready():
	if(Engine.has_singleton("GodotCamera")):
		camera = Engine.get_singleton("GodotCamera")
		if camera:
			camera.setCameraCallbackId(get_instance_id())

func _on_Button_pressed():
	# reset image
	$picture.texture = ResourceLoader.load("res://camera.png")
	
	if camera:
		# open native preview camera
		camera.setImageSize(628) # width / height ascpect ratio
		camera.setImageRotated(90) # rotation
		camera.openCamera()

func _base64texture(image64):
	var image = Image.new()
	image.load_png_from_buffer(Marshalls.base64_to_raw(image64))
	var texture = ImageTexture.new()
	texture.create_from_image(image)
	return texture

func _on_GodotCamera_success(_fileName):
	# photo taken
	$picture.texture = _base64texture(_fileName)

func _on_Reset_pressed():
	$picture.texture = ResourceLoader.load("res://camera.png")
	
func _input(event):
	if event is InputEventScreenDrag:
		$picture.rect_position.x = event.position.x - ($picture.rect_size.x/2)
		$picture.rect_position.y = event.position.y - ($picture.rect_size.y/2)
	
