extends Node2D

var qrcode = null

func _ready():
	if(Engine.has_singleton("GodotQRCode")):
		qrcode = Engine.get_singleton("GodotQRCode")
		if qrcode:
			qrcode.setCallbackId(get_instance_id())

func _base64texture(image64):
	var image = Image.new()
	image.load_png_from_buffer(Marshalls.base64_to_raw(image64))
	var texture = ImageTexture.new()
	texture.create_from_image(image)
	return texture

func _on_godotQRCodeScanned_error(_msg, _err):
	$qrcode.text = str("")
	$error.text = str(_msg)
	print(_err)
	
func _on_godotQRCodeScanned_success(_qrcode, _image):
	$picture.texture = _base64texture(_image)
	$qrcode.text = str(_qrcode)
	$error.text = str("")

func _on_godotQRCodeGenerated_success(_code, _image):
	$picture.texture = _base64texture(_image)

func _on_godotQRCodeGenerated_error(_err):
	print("Error:", _err)

func _on_btnScan_pressed():
	_reset()
	
	if qrcode:
		# open native preview camera
		qrcode.setTitle("Place the QRCode inside the rectangle to scan it")
		qrcode.setCodeSize(200) # width / height ascpect ratio
		qrcode.setImageRotated(90) # rotation
		qrcode.scanCode()

func _on_btnGenerate_pressed():
	_reset()
	var code = $code.text
	
	if qrcode && code != "":
		qrcode.generateCode(code)

func _reset():
	$qrcode.text = "..."
	$error.text = ""
	$picture.texture = ResourceLoader.load("res://icon.png")
