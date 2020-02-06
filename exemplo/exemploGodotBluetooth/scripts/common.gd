extends Node

signal bluetooth_connected
signal bluetooth_disconnected
signal bluetooth_data_received
signal bluetooth_connected_error

# custom
signal bluetooth_connecting
signal bluetooth_single_device_found

var bluetooth
var connecting = false setget _on_connecting

func _startBluetooth(_native=true):
	if(Engine.has_singleton("GodotBluetooth")):
		bluetooth = Engine.get_singleton("GodotBluetooth")
		bluetooth.init(get_instance_id(), true)

#Godot Simple Signal
func _on_connecting(new_boolean):
	connecting = new_boolean
	if new_boolean == true:
		emit_signal("bluetooth_connecting")
	
func _on_bluetooth_connected(device_name, device_adress):
	emit_signal("bluetooth_connected")

func _on_bluetooth_single_device_found(device_name, device_address, device_id):
	emit_signal("bluetooth_single_device_found", device_name, device_address, device_id)

func _on_bluetooth_disconnected():
	emit_signal("bluetooth_disconnected")

func _on_bluetooth_connected_error():
	emit_signal("bluetooth_connected_error")

func _on_bluetooth_data_received(data_received):
	emit_signal("bluetooth_data_received", data_received)
