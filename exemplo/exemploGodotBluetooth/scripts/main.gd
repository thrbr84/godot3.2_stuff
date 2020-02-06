extends Control

onready var btn_device = preload("res://scenes/devices.tscn")
var colorOff = Color(.82,.08,.07,1)
var colorOn = Color(.23,.82,.07,1)
export(bool) var native_bluetooth = false

func _ready():
	common._startBluetooth(native_bluetooth)
	_resetBtnOption(false)
	common.connect("bluetooth_connected", self, "_on_bluetooth_connected")
	common.connect("bluetooth_connected_error", self, "_on_bluetooth_connected_error")
	common.connect("bluetooth_disconnected", self, "_on_bluetooth_disconnected")
	common.connect("bluetooth_data_received", self, "_on_bluetooth_data_received")
	
	# custom
	common.connect("bluetooth_single_device_found", self, "_on_bluetooth_single_device_found")
	common.connect("bluetooth_connecting", self, "_on_bluetooth_connecting")
	
func _on_bluetooth_connected():
	$connection.set_text("Disconnect")
	_resetBtnOption(true)
	if !native_bluetooth:
		$container.hide()
		
func _on_bluetooth_disconnected():
	$connection.set_text("Connect")
	_resetBtnOption(false)
	if !native_bluetooth:
		$container.show()
		
func _on_bluetooth_connected_error():
	$connection.set_text("Connect")
	_on_bluetooth_disconnected()

func _on_bluetooth_single_device_found(device_name, device_address, device_id):
	var d = btn_device.instance()
	d.device_index = device_id
	d.device_name = device_name
	d.device_address = device_address
	$container.add_child(d)

func _on_bluetooth_connecting():
	clean_container()
	$connection.set_text("Connecting...")
	
func clean_container():
	for node in $container.get_children():
		if node is Control:
			node.queue_free()
	
func _on_bluetooth_data_received(data_received):
	if "op1_on" in data_received:
		$op_1/led.color = colorOn
	elif "op1_off" in data_received:
		$op_1/led.color = colorOff

	if "op2_on" in data_received:
		$op_2/led.color = colorOn
	elif "op2_off" in data_received:
		$op_2/led.color = colorOff

func _on_op_1_toggled(button_pressed):
	if !common.bluetooth: 
		$op_1.pressed = false
		return
	if button_pressed:
		$op_1/led.color = colorOn
		common.bluetooth.sendData("op1_on")
	else:
		$op_1/led.color = colorOff
		common.bluetooth.sendData("op1_off")

func _on_op_2_toggled(button_pressed):
	if !common.bluetooth: 
		$op_2.pressed = false
		return
	if button_pressed:
		$op_2/led.color = colorOn
		common.bluetooth.sendData("op2_on")
	else:
		$op_2/led.color = colorOff
		common.bluetooth.sendData("op2_off")

func _on_connection_toggled(button_pressed):
	if !common.bluetooth: 
		$connection.pressed = false
		return
	clean_container()
	common.bluetooth.getPairedDevices(native_bluetooth)
	
func _resetBtnOption(_visible=true):
	for each in get_tree().get_nodes_in_group("btn_opt"):
		each.get_node('led').color = colorOff
		each.pressed = false
		
		if _visible:
			each.show()
		else:
			each.hide()

