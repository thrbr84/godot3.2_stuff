extends Control

var device_index
var device_name
var device_address

func _ready():
	$devices.set_text(device_name)

func _on_devices_pressed():
	if common.bluetooth:
		common.connecting = true
		common.bluetooth.connect(device_index)
	else:
		print("Module not initialized!")
